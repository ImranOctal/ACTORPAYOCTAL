package com.octal.actorpayuser

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpayuser.base.BaseActivity
import com.octal.actorpayuser.base.ResponseSealed
import com.octal.actorpayuser.databinding.ActivityMainBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.IS_APP_FROM_SPLASH
import com.octal.actorpayuser.repositories.AppConstance.AppConstance.Companion.IS_APP_IN_BACKGROUD
import com.octal.actorpayuser.repositories.retrofitrepository.models.SuccessResponse
import com.octal.actorpayuser.ui.adapter.FeaturesAdapter
import com.octal.actorpayuser.ui.adapter.MenuAdapter
import com.octal.actorpayuser.ui.addmoney.AddMoneyFragment
import com.octal.actorpayuser.ui.auth.biometric.AuthBottomSheetDialog
import com.octal.actorpayuser.ui.auth.viewmodel.LoginViewModel
import com.octal.actorpayuser.ui.cart.CartViewModel
import com.octal.actorpayuser.ui.dashboard.bottomnavfragments.HistoryBottomFragment
import com.octal.actorpayuser.ui.dashboard.bottomnavfragments.HomeBottomFragment
import com.octal.actorpayuser.ui.dashboard.bottomnavfragments.ProfileBottomFragment
import com.octal.actorpayuser.ui.dashboard.bottomnavfragments.WalletBottomFragment
import com.octal.actorpayuser.ui.dashboard.models.DrawerItems
import com.octal.actorpayuser.ui.dispute.DisputeFragment
import com.octal.actorpayuser.ui.misc.MiscFragment
import com.octal.actorpayuser.ui.mobileanddth.MobileAndDTHFragment
import com.octal.actorpayuser.ui.myOrderList.MyOrdersListFragment
import com.octal.actorpayuser.ui.productList.ProductsListFragment
import com.octal.actorpayuser.ui.promocodes.PromoListFragment
import com.octal.actorpayuser.ui.refer_and_earn.ReferAndEarnFragment
import com.octal.actorpayuser.ui.rewards_points.RewardsPointsFragment
import com.octal.actorpayuser.ui.settings.SettingsFragment
import com.octal.actorpayuser.utils.CommonDialogsUtils
import com.octal.actorpayuser.utils.OnFilterClick
import com.octal.actorpayuser.viewmodel.ActorPayViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(), DuoMenuView.OnMenuClickListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var mTitles = ArrayList<DrawerItems>()
    private var mViewHolder: ViewHolder? = null
    private var mMenuAdapter: MenuAdapter? = null
    private lateinit var activity: Context
    private val viewModel: ActorPayViewModel by inject()
    private val cartViewModel: CartViewModel by inject()
    private lateinit var filterClick: OnFilterClick

    private lateinit var navController: NavController
    private lateinit var navHostFragment: Fragment

    private var isMenuOrBack = false

    private var biometricPrompt: BiometricPrompt? = null
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var authSheet: AuthBottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding here
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.container)!!
        navController = findNavController(R.id.container)
        setupNavigation()

        activity = this
        val view = binding.root
        setContentView(view)


        initialization()
        setBottomNavigationView()
        features()
        title = "ActorPay"
    }

    private fun features() {
        LoginViewModel.isFromContentPage = false
        binding.layoutMainID.rvItemsID.apply {
            val arraylist: ArrayList<String> =
                arrayListOf(
                    "Add Money",
                    "Send Money",
                    "Mobile & DTH",
                    "Utility Bill",
                    "Online Payment",
                    "Product List"
                )
            adapter = FeaturesAdapter(arraylist) { position ->
                onItemClickListener(position, arraylist)
            }
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
        binding.layoutMainID.scan.setOnClickListener {
            navController.navigate(R.id.transferMoneyFragment)
        }

    }

    private fun setBottomNavigationView() {

        binding.layoutMainID.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = navHostFragment.childFragmentManager.fragments[0]
            when (item.itemId) {
                R.id.home_fragment -> {
                    if ((fragment is HomeBottomFragment).not()) {
                        navController.navigate(R.id.homeBottomFragment)
                    }
                }
                R.id.history_fragment -> {
                    if ((fragment is HistoryBottomFragment).not()) {
                        navController.navigate(R.id.historyBottomFragment)
                    }
                }
                R.id.wallet_fragment -> {
                    if (fragment !is WalletBottomFragment) {
                        navController.navigate(R.id.walletBottomFragment)
                    }
                }
                R.id.profile_fragment -> {
                    if (fragment !is ProfileBottomFragment) {
                        navController.navigate(R.id.profileBottomFragment)
                    }
                }
                else -> {
                    navController.navigate(R.id.homeBottomFragment)
                }
            }
            true
        }
    }

    private inner class ViewHolder {
        val mDuoDrawerLayout: DuoDrawerLayout = binding.drawer
        val mDuoMenuView: DuoMenuView = mDuoDrawerLayout.menuView as DuoMenuView
        val mToolbar: Toolbar = binding.toolbarLayout.toolbar
        val mFooterLayout: RelativeLayout = mDuoMenuView.footerView as RelativeLayout
        val mHeaderLayout: RelativeLayout = mDuoMenuView.headerView as RelativeLayout

        init {
            val name = mHeaderLayout.findViewById<TextView>(R.id.duo_view_header_text_title)
            lifecycleScope.launchWhenCreated {
                viewModel.methodRepo.dataStore.getFirstName().collect { first ->
                    viewModel.methodRepo.dataStore.getLastName().collect { last ->
                        name.text = "$first $last"
                    }
                }
            }
            mFooterLayout.setOnClickListener {

                logout(viewModel.methodRepo)
            }

        }
    }


    private fun initialization() {
//        apiResponse()
        cartResponse()
        cartViewModel.getCartItems()

        mTitles = ArrayList()
        mTitles.add(
            DrawerItems(
                getString(R.string.my_orders),
                ContextCompat.getDrawable((this), R.drawable.my_orders)!!

            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.wallet_statement),
                ContextCompat.getDrawable((this), R.drawable.wallet_statement)!!

            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.my_loyalty_and_rewards),
                ContextCompat.getDrawable((this), R.drawable.my_profile)!!

            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.referral),
                ContextCompat.getDrawable((this), R.drawable.my_profile)!!

            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.disputes),
                ContextCompat.getDrawable((this), R.drawable.wallet_statement)!!
            )
        )

        mTitles.add(
            DrawerItems(
                getString(R.string.promo_offers),
                ContextCompat.getDrawable((this), R.drawable.my_orders)!!

            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.my_profile),
                ContextCompat.getDrawable((this), R.drawable.my_profile)!!

            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.settings),
                ContextCompat.getDrawable((this), R.drawable.settings)!!

            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.more), ContextCompat.getDrawable((this), R.drawable.more)!!

            )
        )
        // Initialize the views
        mViewHolder = ViewHolder()

        // Handle toolbar actions
        handleToolbar()

        // Handle menu actions
        handleMenu()

        // Handle drawer actions
        handleDrawer()

        //actionView here
        actionView()

        binding.menuBack.setOnClickListener {
            if (isMenuOrBack)
                mViewHolder?.mDuoDrawerLayout?.openDrawer()
            else
                onBackPressed()
        }

    }

    private fun actionView() {

    }

    private fun handleToolbar() {
        setSupportActionBar(mViewHolder?.mToolbar)
        mViewHolder?.mToolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        binding.cart.setOnClickListener {
            navController.navigate(R.id.cartFragment)
        }
        binding.filter.setOnClickListener {
            filterClick.onClick()
        }
        binding.notification.setOnClickListener {
            navController.navigate(R.id.notificationFragment)
        }
    }


    private fun handleMenu() {
        mMenuAdapter = MenuAdapter(this, mTitles)
        mViewHolder?.mDuoMenuView?.setOnMenuClickListener(this)
        mViewHolder?.mDuoMenuView?.adapter = mMenuAdapter
    }

    private fun handleDrawer() {
        val duoDrawerToggle = DuoDrawerToggle(
            this,
            mViewHolder?.mDuoDrawerLayout,
            mViewHolder?.mToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        mViewHolder?.mDuoDrawerLayout?.setDrawerListener(duoDrawerToggle)
        duoDrawerToggle.syncState()
    }

    override fun onFooterClicked() {

    }

    override fun onHeaderClicked() {
        mViewHolder?.mDuoDrawerLayout?.closeDrawer()
    }

    override fun onOptionClicked(position: Int, objectClicked: Any?) {


        title = mTitles[position].mTitle
        mMenuAdapter?.setViewSelected(position, true)
        val fragment = navHostFragment.childFragmentManager.fragments[0]
        when (position) {

            0 -> {
                if (fragment !is MyOrdersListFragment) {
                    navController.navigate(R.id.myOrderFragment)
                }
            }
            1 -> {
                if (fragment !is WalletBottomFragment) {
                    navController.navigate(R.id.walletBottomFragment)
                }
            }
            2 -> {
                if (fragment !is RewardsPointsFragment) {
                    navController.navigate(R.id.rewardsFragment)
                }
            }
            3 -> {
                if (fragment !is ReferAndEarnFragment) {
                    navController.navigate(R.id.referFragment)
                }
            }
            4 -> {
                if (fragment !is DisputeFragment) {
                    navController.navigate(R.id.disputeFragment)
                }
            }
            5 -> {

                if (fragment !is PromoListFragment) {
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                    navController.navigate(R.id.promoListFragment)
                }
            }
            6 -> {
                if (fragment !is ProfileBottomFragment) {
                    navController.navigate(R.id.profileBottomFragment)
                }

            }
            7 -> {
                if (fragment !is SettingsFragment) {
                    navController.navigate(R.id.settingsFragment)
                }
            }
            8 -> {
                if (fragment !is MiscFragment) {
                    navController.navigate(R.id.miscFragment)
                }
            }

        }
        mViewHolder?.mDuoDrawerLayout?.closeDrawer()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


    private fun onItemClickListener(position: Int, mList: List<String>) {

        val fragment = navHostFragment.childFragmentManager.fragments[0]
        when (mList[position]) {

            "Add Money" -> {
                if (fragment !is AddMoneyFragment) {
                    navController.navigate(R.id.addMoneyFragment)
                }
            }
            "Send Money" -> {
                if (fragment !is WalletBottomFragment) {
                    navController.navigate(R.id.transferMoneyFragment)
                }
            }
            "Mobile & DTH" -> {
                if (fragment !is MobileAndDTHFragment) {
                    navController.navigate(R.id.mobileAndDTHFragment)
                }
            }
            "Utility Bill" -> {
                if (fragment !is WalletBottomFragment) {
//                    title = "Utility Bill"
                    navController.navigate(R.id.walletBottomFragment)
                }
            }
            "Online Payment" -> {
                if (fragment !is WalletBottomFragment) {
//                    title = "Online Payment"
                    navController.navigate(R.id.walletBottomFragment)
                }
            }
            "Product List" -> {
                if (fragment !is ProductsListFragment) {
                    navController.navigate(R.id.productListFragment)
                }
            }

        }
    }


    private fun apiResponse() {
        lifecycleScope.launch {
            viewModel.actorResponseLive.collect {
                when (it) {
                    is ResponseSealed.loading -> {
                        showLoadingDialog()
                    }
                    is ResponseSealed.Success -> {
                        hideLoadingDialog()
                        if (it.response is SuccessResponse) {
                            CommonDialogsUtils.showCommonDialog(
                                this@MainActivity,
                                viewModel.methodRepo,
                                "Success",
                                it.response.message
                            )
                        } else {
                            showCustomAlert(
                                getString(R.string.please_try_after_sometime),
                                binding.root
                            )
                        }
                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoadingDialog()
                        showCustomAlert(
                            it.message!!.message,
                            binding.root
                        )
                    }
                    is ResponseSealed.Empty -> {
                        hideLoadingDialog()
                    }
                }
            }
        }
    }

    private fun cartResponse() {
        lifecycleScope.launchWhenCreated {

            cartViewModel.responseLive.collect { event ->

                when (event) {
                    is ResponseSealed.loading -> {
                        showLoadingDialog()
                    }
                    is ResponseSealed.Success -> {
                        hideLoadingDialog()

                    }
                    is ResponseSealed.ErrorOnResponse -> {
                        hideLoadingDialog()
                        cartViewModel.responseLive.value = ResponseSealed.Empty

                        if (event.message!!.code == 403) {
                            forcelogout(viewModel.methodRepo)
                        }
                    }
                    is ResponseSealed.Empty -> {
                        hideLoadingDialog()

                    }

                }
            }
        }
    }

    private fun setupNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeBottomFragment -> {
                    binding.layoutMainID.bottomNavigationView.menu.getItem(0).isChecked = true
                    binding.layoutMainID.constraintLayout.setBackgroundResource(R.drawable.layout_bg)
                    updateUI("ActorPay",true,true,true,true,false,true,true)
                }
                R.id.historyBottomFragment -> {
                    binding.layoutMainID.bottomNavigationView.menu.getItem(1).isChecked = true
                    updateUI("My History",true,true,true,true,false,true,false)
                }
                R.id.walletBottomFragment -> {
                    binding.layoutMainID.bottomNavigationView.menu.getItem(3).isChecked = true
                    updateUI("My Wallet",true,true,true,true,false,true,false)
                }
                R.id.profileBottomFragment -> {
                    binding.layoutMainID.bottomNavigationView.menu.getItem(4).isChecked = true
                    updateUI("My Profile",true,true,true,true,false,true,false)
                }
                R.id.referFragment -> {
                    updateUI("My Refer",false,false,false,false,false,false,false)
                }
                R.id.rewardsFragment -> {
                    updateUI("My Rewards",false,false,false,false,false,false,false)
                }
                R.id.productListFragment -> {
                    updateUI("Products",false,false,false,true,false,false,false)
                }
                R.id.miscFragment -> {
                    updateUI("More",false,false,false,false,false,false,false)
                }
                R.id.myOrderFragment -> {
                    updateUI("My Orders",false,false,false,false,true,false,false)
                }
                R.id.promoListFragment -> {
                    updateUI("Promos",false,false,false,false,false,false,false)
                }
                R.id.settingsFragment -> {
                    updateUI("Settings",false,false,false,false,false,false,false)
                }
                R.id.faqFragment -> {
                    updateUI("FAQ",false,false,false,false,false,false,false)
                }
                R.id.remittance -> {
                    updateUI(getString(R.string.change_payment_option),false,false,false,false,false,false,false)
                }
                R.id.shippingAddressFragment -> {
                    updateUI("My Addresses",false,false,false,false,false,false,false)
                }
                R.id.orderDetailsFragment -> {
                    updateUI("Order Summary",false,false,false,false,false,false,false)
                }
                R.id.addMoneyFragment -> {
                    updateUI("Add Money In Wallet",false,false,false,false,false,false,false)

                }
                R.id.transferMoneyFragment -> {
                    updateUI("Transfer Money",false,false,false,false,false,false,false)
                    binding.layoutMainID.constraintLayout.setBackgroundResource(R.color.white)
                }
                R.id.payFragment -> {
                    updateUI("Transfer Money",false,false,false,false,false,false,false)
                    binding.layoutMainID.constraintLayout.setBackgroundResource(R.color.white)
                }
                R.id.mobileAndDTHFragment -> {
                    updateUI("Mobile and DTH Recharge",false,false,false,false,false,false,false)
                }
                R.id.notificationFragment -> {
                    updateUI("Notifications",false,false,false,false,false,false,false)
                }
                R.id.disputeFragment -> {
                    updateUI("Disputes",false,false,false,false,false,false,false)
                }
                R.id.disputeDetailsFragment -> {
                    updateUI("Dispute Details",false,false,false,false,false,false,false)
                }
                R.id.cartFragment -> {
                    updateUI("My Cart",false,false,false,false,false,false,false)

                }
                R.id.productDetailsFragment -> {
                    updateUI("Product Details",false,false,false,false,false,false,false)

                }
                R.id.placeOrderFragment -> {
                    updateUI("Place Order",false,false,false,false,false,false,false)
                }
            }
        }
    }

    fun updateUI(
        title: String,
        isMenu: Boolean,
        isDrawerUnLock: Boolean,
        showHideBottomNav: Boolean,
        showHideCartIcon: Boolean,
        showHideFilterIcon: Boolean,
        showNotificationIcon: Boolean,
        isRvItemsVisible: Boolean
    ) {
        this.title = title
        this.isMenuOrBack = isMenu
        if (isDrawerUnLock)
            mViewHolder?.mDuoDrawerLayout?.setDrawerLockMode(DuoDrawerLayout.LOCK_MODE_UNLOCKED)
        else
            mViewHolder?.mDuoDrawerLayout?.setDrawerLockMode(DuoDrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        if (isMenu)
            binding.menuBack.setImageResource(R.drawable.menu)
        else
            binding.menuBack.setImageResource(R.drawable.back_icon)

        showHideBottomNav(showHideBottomNav)
        showHideCartIcon(showHideCartIcon)
        showHideFilterIcon(showHideFilterIcon)
        showNotificationIcon(showNotificationIcon)
        if (isRvItemsVisible)
            binding.layoutMainID.rvItemsID.visibility = View.VISIBLE
        else
            binding.layoutMainID.rvItemsID.visibility = View.GONE
    }


    private fun showHideBottomNav(showHide: Boolean) {
        if (showHide) {
            binding.layoutMainID.bottomNavigation.visibility = View.VISIBLE
            binding.layoutMainID.scan.visibility = View.VISIBLE
        } else {
            binding.layoutMainID.bottomNavigation.visibility = View.GONE
            binding.layoutMainID.scan.visibility = View.GONE
        }
    }

    private fun showHideCartIcon(showHide: Boolean) {
        if (showHide) {
            binding.cart.visibility = View.VISIBLE
        } else {
            binding.cart.visibility = View.GONE
        }
    }

    private fun showHideFilterIcon(showHide: Boolean) {
        if (showHide) {
            binding.filter.visibility = View.VISIBLE
        } else {
            binding.filter.visibility = View.GONE
        }
    }

    private fun showNotificationIcon(showHide: Boolean) {
        if (showHide) {
            binding.notification.visibility = View.VISIBLE
        } else {
            binding.notification.visibility = View.GONE
        }
    }

    fun onFilterClick(filterClick: OnFilterClick) {
        this.filterClick = filterClick
    }

    override fun onBackPressed() {
        val fragment = navHostFragment.childFragmentManager.fragments[0]
        if (fragment is HomeBottomFragment) {
            CommonDialogsUtils.showCommonDialog(
                this,
                viewModel.methodRepo,
                "Exit App",
                "Are you sure?",
                autoCancelable = true,
                isCancelAvailable = true,
                callback = object : CommonDialogsUtils.DialogClick {
                    override fun onClick() {
                        finishAffinity()
                    }

                    override fun onCancel() {}
                })
        } else
            super.onBackPressed()

    }

    fun isBioMetricAvailable(): Boolean {
        val biometricManager: BiometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                return true

            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                return false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                return false

            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                return false
            }
        }
        return false

    }

    override fun onResume() {
        super.onResume()
        if (IS_APP_FROM_SPLASH) {
            IS_APP_FROM_SPLASH = false

            if (!isBioMetricAvailable())
                return


            try {
                biometricAuth()

                if (authSheet == null) {
                    authSheet = AuthBottomSheetDialog {
                        biometricPrompt?.authenticate(promptInfo)
                    }
                    authSheet?.isCancelable = false
                    authSheet?.show(supportFragmentManager, "auth sheet")
                    biometricPrompt?.authenticate(promptInfo)
                } else {
                    if (authSheet?.isVisible!!.not())
                        authSheet?.show(supportFragmentManager, "auth sheet")
                }

            } catch (ex: Exception) {
                Log.e("MainActivity--ERROR--", "${ex.message}")
            }

        }

    }

    fun keyGuard() {
        val km = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        if (km.isKeyguardSecure) {
            val i =
                km.createConfirmDeviceCredentialIntent("Authentication required", "password")
            // startActivityForResult(i, Constants.CODE_AUTHENTICATION_VERIFICATION)
            resultLauncher.launch(i)

        } else {
//            authSheet!!.dismiss()
//            showCustomToast("No any security setup. please setup it.")
        }

    }

    private fun biometricAuth() {


        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    keyGuard()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)

                    authSheet?.dismiss()
//                    isBioAuth = true
                    IS_APP_IN_BACKGROUD = false

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
//                    isBioAuth = false
                    showCustomToast("Authentication failed")

                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock ActorPay app")
            .setSubtitle("Log in using biometric credential")
            .setNegativeButtonText("Use Password")
            .setConfirmationRequired(false)
            .build()


    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                authSheet?.dismiss()

            }
            if (result.resultCode == Activity.RESULT_CANCELED) {

            }
        }

}