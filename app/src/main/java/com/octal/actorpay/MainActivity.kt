package com.octal.actorpay

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.getBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.Utils.CommonDialogsUtils
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.databinding.ActivityMainBinding
import com.octal.actorpay.ui.adapter.FeaturesAdapter
import com.octal.actorpay.ui.adapter.MenuAdapter
import com.octal.actorpay.ui.auth.LoginActivity
import com.octal.actorpay.ui.dashboard.`interface`.ItemListenr
import com.octal.actorpay.ui.dashboard.bottomnavfragments.HistoryBottomFragment
import com.octal.actorpay.ui.dashboard.bottomnavfragments.HomeBottomFragment
import com.octal.actorpay.ui.dashboard.bottomnavfragments.ProfileBottomFragment
import com.octal.actorpay.ui.dashboard.bottomnavfragments.WalletBottomFragment
import com.octal.actorpay.ui.dashboard.models.DrawerItems
import com.octal.actorpay.ui.misc.MiscFragment
import com.octal.actorpay.ui.myOrderList.MyOrdersListFragment
import com.octal.actorpay.ui.productList.ProductsListFragment
import com.octal.actorpay.ui.refer_and_earn.ReferAndEarnFragment
import com.octal.actorpay.ui.remittance.RemittanceFragment
import com.octal.actorpay.ui.rewards_points.RewardsPointsFragment
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(), DuoMenuView.OnMenuClickListener,
    AdapterView.OnItemSelectedListener, ItemListenr {
    private lateinit var binding: ActivityMainBinding
    private var mTitles = ArrayList<DrawerItems>()
    private var mViewHolder: ViewHolder? = null
    private var mMenuAdapter: MenuAdapter? = null
    private var doubleBackToExitPressedOnce = false
    private lateinit var listner: ItemListenr
    var navController: NavController? = null
    private lateinit var activity: Context
    private val viewModel: ActorPayViewModel by inject()
    private var fragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding here
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activity = this
        listner = this
        val view = binding.root
        setContentView(view)
        setView(view)
        startFragment(
            HomeBottomFragment.newInstance(),
            HomeBottomFragment.toString(),
            addToBackStack = true
        )
        initiliation()
        setBottomNavigationView()
        features()
        title = "ActorPay"
    }

    private fun features() {
        binding.layoutMainID.rvItemsID.apply {
            var arraylist: ArrayList<String> =
                arrayListOf(
                    "Add Money",
                    "Send Money",
                    "Mobile & DTH",
                    "Utility Bill",
                    "Online Payment",
                    "Product List"
                )
            adapter = FeaturesAdapter(arraylist, activity, listner)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }

    }

    private fun setBottomNavigationView() {
        /*val bottomnav: BottomNavigationView
        bottomnav = rootView.findViewById(R.id.bottomNavigationView)*/
        binding.layoutMainID.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_fragment -> {
                    title = "ActorPay"
                    if (getCurrentFragment() !is HomeBottomFragment) {
                        startFragment(
                            HomeBottomFragment.newInstance(),
                            addToBackStack = true,
                            HomeBottomFragment.toString()
                        )
                        binding.layoutMainID.rvItemsID.visibility = View.VISIBLE
                    }
                    /*  Navigation.findNavController(binding.root).navigate(R.id.homeBottomFragment)*/

                    /* val fragment = HomeBottomFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fragment!!, fragment.javaClass.getSimpleName())
                        .commit()*/

                }
                R.id.history_fragment -> {
                    if (getCurrentFragment() !is HistoryBottomFragment) {
                        title = "My History"
                        startFragment(
                            HistoryBottomFragment.newInstance(),
                            addToBackStack = true,
                            HistoryBottomFragment.toString()
                        )
                        binding.layoutMainID.rvItemsID.visibility = View.GONE
                    }
                    // Navigation.findNavController(binding.root).navigate(R.id.historyBottomFragment)
                }
                R.id.wallet_fragment -> {
                    // Navigation.findNavController(binding.root).navigate(R.id.walletFragment)
                    if (getCurrentFragment() !is WalletBottomFragment) {
                        title = "My Wallet"
                        startFragment(
                            WalletBottomFragment.newInstance(),
                            addToBackStack = true,
                            WalletBottomFragment.toString()
                        )
                        binding.layoutMainID.rvItemsID.visibility = View.GONE
                    }
                }
                R.id.profile_fragment -> {
                    if (getCurrentFragment() !is ProfileBottomFragment) {
                        title = "My Profile"
                        startFragment(
                            ProfileBottomFragment.newInstance(),
                            addToBackStack = true,
                            ProfileBottomFragment.toString()
                        )
                        binding.layoutMainID.rvItemsID.visibility = View.GONE
                    }


                }
                else -> {
                    startFragment(
                        HomeBottomFragment.newInstance(),
                        addToBackStack = true,
                        HomeBottomFragment.toString()
                    )
                }
            }
            true
        }
    }

    private inner class ViewHolder internal constructor() {
        val mDuoDrawerLayout: DuoDrawerLayout = binding.drawer
        val mDuoMenuView: DuoMenuView
        val mToolbar: Toolbar
        val mFooterLayout: RelativeLayout

        init {
            mDuoMenuView = mDuoDrawerLayout.menuView as DuoMenuView
            mToolbar = binding.toolbarLayout.toolbar
            mFooterLayout = mDuoMenuView.footerView as RelativeLayout
            mFooterLayout.setOnClickListener {

                logout()
            }

        }
    }

    fun logout(){
        CommonDialogsUtils.showCommonDialog(this,viewModel.methodRepo, "Log Out ",
            "Are you sure?", true, true, true, false,
            object : CommonDialogsUtils.DialogClick {
                override fun onClick() {
                    viewModel.shared.Logout()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finishAffinity()
                }
                override fun onCancel() {
                }
            })
    }

    private fun initiliation() {
        mTitles = ArrayList<DrawerItems>()
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
                getString(R.string.view_available_money_in_wallet),
                ContextCompat.getDrawable((this), R.drawable.my_profile)!!
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
                getString(R.string.promo_offers),
                ContextCompat.getDrawable((this), R.drawable.my_orders)!!

            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.change_payment_option),
                ContextCompat.getDrawable((this), R.drawable.my_orders)!!

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

    }

    private fun actionView() {

    }

    private fun handleToolbar() {
        setSupportActionBar(mViewHolder?.mToolbar)
        mViewHolder?.mToolbar?.setTitleTextColor(resources.getColor(R.color.white))

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
        // Set the toolbar title
        title = mTitles[position].mTitle
        // Set the right options selected
        mMenuAdapter?.setViewSelected(position, true)
        // Navigate to the right fragment
        when (position) {

            0 -> {
                // NavController().navigateWithId(R.id.myOrderFragment, findNavController())
                if (getCurrentFragment() !is MyOrdersListFragment) {
                    title = "My Orders"
                    startFragment(
                        MyOrdersListFragment.newInstance(),
                        addToBackStack = true,
                        MyOrdersListFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
                // Navigation.findNavController(binding.root).navigate(R.id.myOrderFragment)
            }
            1 -> {
                if (getCurrentFragment() !is WalletBottomFragment) {
                    title = "My Wallet"
                    startFragment(
                        WalletBottomFragment.newInstance(),
                        addToBackStack = true,
                        WalletBottomFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
                //  Navigation.findNavController(binding.root).navigate(R.id.walletFragment)
            }
            2 -> {
                if (getCurrentFragment() !is RewardsPointsFragment) {
                    title = "Rewards"
                    startFragment(
                        RewardsPointsFragment.newInstance(),
                        addToBackStack = true,
                        RewardsPointsFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }

                //Navigation.findNavController(binding.root).navigate(R.id.rewardsFragment)
                //  NavController().navigateWithId(R.id.productListFragment, findNavController())

            }
            3 -> {
                if (getCurrentFragment() !is ReferAndEarnFragment) {
                    title = "Refer and Earn"
                    startFragment(
                        ReferAndEarnFragment.newInstance(),
                        addToBackStack = true,
                        ReferAndEarnFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
                // Navigation.findNavController(binding.root).navigate(R.id.referFragment)

            }
            4 -> {
                if (getCurrentFragment() !is WalletBottomFragment) {
                    title = "My Wallet"
                    startFragment(
                        WalletBottomFragment.newInstance(),
                        addToBackStack = true,
                        WalletBottomFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
                // NavController().navigateWithId(R.id.walletFragment, findNavController())
            }
            5 -> {
                if (getCurrentFragment() !is ProfileBottomFragment) {
                    title = "My Profile"
                    startFragment(
                        ProfileBottomFragment.newInstance(),
                        addToBackStack = true,
                        ProfileBottomFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
            }
            6 -> {
                if (getCurrentFragment() !is ProductsListFragment) {
                    title = "Products"
                    startFragment(
                        ProductsListFragment.newInstance(),
                        addToBackStack = true,
                        ProductsListFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
               /* Navigation.findNavController(binding.root).navigate(R.id.productListFragment)
                binding.layoutMainID.rvItemsID.visibility = View.GONE*/
            }
            7 -> {
                if (getCurrentFragment() !is RemittanceFragment) {
                    title=getString(R.string.change_payment_option)
                    startFragment(
                        RemittanceFragment.newInstance(),
                        addToBackStack = true,
                        RemittanceFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
               // Navigation.findNavController(binding.root).navigate(R.id.remittance)
               // binding.layoutMainID.rvItemsID.visibility = View.GONE
            }
            8 -> {
                if (getCurrentFragment() !is MiscFragment) {
                    title="More"
                    startFragment(
                        MiscFragment.newInstance(),
                        addToBackStack = true,
                        MiscFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
               // Navigation.findNavController(binding.root).navigate(R.id.miscFragment)
               // binding.layoutMainID.rvItemsID.visibility = View.GONE
            }

        }

        // Close the drawer
        mViewHolder?.mDuoDrawerLayout?.closeDrawer()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


    override fun on_ItemClickListner(position: Int, mList: List<String>, view: View) {
        when (mList[position]) {
            "Add Money" -> {
                if (getCurrentFragment() !is WalletBottomFragment) {
                    title = "Add Money"
                    startFragment(
                        WalletBottomFragment.newInstance(),
                        addToBackStack = true,
                        WalletBottomFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
            }
            "Send Money" -> {
                //NavController().navigateWithId(R.id.walletFragment, findNavController())
                if (getCurrentFragment() !is WalletBottomFragment) {
                    title = "Send Money"
                    startFragment(
                        WalletBottomFragment.newInstance(),
                        addToBackStack = true,
                        WalletBottomFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
            }
            "Mobile & DTH" -> {
                //NavController().navigateWithId(R.id.transferMoneyFragment, findNavController())
                if (getCurrentFragment() !is WalletBottomFragment) {
                    title = "Mobile and DTH"
                    startFragment(
                        WalletBottomFragment.newInstance(),
                        addToBackStack = true,
                        WalletBottomFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
            }
            "Utility Bill" -> {
                if (getCurrentFragment() !is ProductsListFragment) {
                    title = "Utility Bill"
                    startFragment(
                        ProductsListFragment.newInstance(),
                        addToBackStack = true,
                        ProductsListFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
            }
            "Online Payment" -> {
                if (getCurrentFragment() !is ProductsListFragment) {
                    title = "Online Payment"
                    startFragment(
                        ProductsListFragment.newInstance(),
                        addToBackStack = true,
                        ProductsListFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
            }
            "Product List" -> {
                if (getCurrentFragment() !is ProductsListFragment) {
                    title = "Products"
                    startFragment(
                        ProductsListFragment.newInstance(),
                        addToBackStack = true,
                        ProductsListFragment.toString()
                    )
                    binding.layoutMainID.rvItemsID.visibility = View.GONE
                }
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (navController!!.navigateUp() == false) {
            onBackPressed()
        }
        return navController!!.navigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = getCurrentFragment()
        if (fragment is HomeBottomFragment) {
            title = "ActorPay"
            binding.layoutMainID.rvItemsID.visibility = View.VISIBLE
            binding.layoutMainID.bottomNavigationView.setSelectedItemId(R.id.home_fragment)
        } else if (fragment is HistoryBottomFragment) {
            title = "My History"
            binding.layoutMainID.bottomNavigationView.setSelectedItemId(R.id.history_fragment)
        } else if (fragment is WalletBottomFragment) {
            title = "My Wallet"
            binding.layoutMainID.bottomNavigationView.setSelectedItemId(R.id.wallet_fragment)
        } else if (fragment is ProfileBottomFragment) {
            title = "My Profile"
            binding.layoutMainID.bottomNavigationView.setSelectedItemId(R.id.profile_fragment)
        } else if (getCurrentFragment() is ReferAndEarnFragment) {
            title = "My Refer"
        }else if (getCurrentFragment() is RewardsPointsFragment) {
            title = "My Rewards"
        }else  if (getCurrentFragment() is ProductsListFragment) {
            title = "Products"
        }else  if (getCurrentFragment() is RemittanceFragment){
            title=getString(R.string.change_payment_option)
        }else   if (getCurrentFragment() is MiscFragment) {
            title = "More"
        }
    }


}