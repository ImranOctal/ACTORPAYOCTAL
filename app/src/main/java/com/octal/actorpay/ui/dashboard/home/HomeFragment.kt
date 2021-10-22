package com.octal.actorpay.ui.dashboard.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.ui.navController.NavController
import com.octal.actorpay.R
import com.octal.actorpay.databinding.FragmentHomeBinding
import com.octal.actorpay.ui.dashboard.`interface`.ItemListenr
import com.octal.actorpay.ui.adapter.FeaturesAdapter
import com.octal.actorpay.ui.adapter.MenuAdapter
import com.octal.actorpay.ui.dashboard.bottomnavfragments.HistoryBottomFragment
import com.octal.actorpay.ui.dashboard.bottomnavfragments.HomeBottomFragment
import com.octal.actorpay.ui.dashboard.bottomnavfragments.ProfileBottomFragment
import com.octal.actorpay.ui.dashboard.bottomnavfragments.WalletBottomFragment
import com.octal.actorpay.ui.dashboard.models.DrawerItems
import com.octal.actorpay.viewmodel.ActorPayViewModel
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), DuoMenuView.OnMenuClickListener,
    AdapterView.OnItemSelectedListener, ItemListenr {
    private var mTitles = ArrayList<DrawerItems>()
    private var mViewHolder: ViewHolder? = null
    private var mMenuAdapter: MenuAdapter? = null
    private lateinit var rootView: View
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorPayViewModel by  inject()
    private val backStack = Stack<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        loadFragment(HomeBottomFragment())
        initiliation()
        setBottomNavigationView()
        features()
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            adapter = FeaturesAdapter(arraylist, requireActivity(), this@HomeFragment)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }

    }

    private fun setBottomNavigationView() {
        /*val bottomnav: BottomNavigationView
        bottomnav = rootView.findViewById(R.id.bottomNavigationView)*/
        binding.layoutMainID.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_fragment -> {
                    val fragment = HomeBottomFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fragment, fragment.javaClass.getSimpleName())
                        .commit()

                }
                R.id.history_fragment -> {
                    //NavController().navigateWithId(R.id.homeFragment, findNavController())
                    val fragment = HistoryBottomFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                }
                R.id.wallet_fragment -> {
                    //
                    val fragment = WalletBottomFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                }
                R.id.profile_fragment -> {
                    val fragment = ProfileBottomFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, fragment, fragment.javaClass.getSimpleName())
                        .commit()

                }
                else -> {
                    loadFragment(HomeBottomFragment())
                }
            }
            true
        }
    }

    private inner class ViewHolder internal constructor() {
        val mDuoDrawerLayout: DuoDrawerLayout = binding.drawer
        val mDuoMenuView: DuoMenuView
        val mToolbar: Toolbar

        init {
            mDuoMenuView = mDuoDrawerLayout.menuView as DuoMenuView
            mToolbar = binding.toolbarLayout.toolbar

        }
    }

    private fun initiliation() {
        mTitles.add(
            DrawerItems(
                getString(R.string.my_orders),
                this.resources.getDrawable(R.drawable.my_orders)!!
            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.wallet_statement),
                this.resources.getDrawable(R.drawable.wallet_statement)!!
            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.my_loyalty_and_rewards),
                this.resources.getDrawable(R.drawable.my_profile)!!
            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.referral),
                this.resources.getDrawable(R.drawable.my_profile)!!
            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.view_available_money_in_wallet),
                this.resources.getDrawable(R.drawable.my_profile)!!
            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.my_profile),
                this.resources.getDrawable(R.drawable.my_profile)!!
            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.promo_offers),
                this.resources.getDrawable(R.drawable.my_orders)!!
            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.change_payment_option),
                this.resources.getDrawable(R.drawable.my_orders)!!
            )
        )
        mTitles.add(
            DrawerItems(
                getString(R.string.more),
                this.resources.getDrawable(R.drawable.more)!!
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
        (activity as AppCompatActivity?)!!.setSupportActionBar(mViewHolder?.mToolbar)
        mViewHolder?.mToolbar?.setTitleTextColor(resources.getColor(R.color.white))

    }


    private fun handleMenu() {
        mMenuAdapter = MenuAdapter(requireActivity(), mTitles)
        mViewHolder?.mDuoMenuView?.setOnMenuClickListener(this)
        mViewHolder?.mDuoMenuView?.adapter = mMenuAdapter
    }

    private fun handleDrawer() {
        val duoDrawerToggle = DuoDrawerToggle(
            requireActivity(),
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
        requireActivity().title = mTitles[position].mTitle
        // Set the right options selected
        mMenuAdapter?.setViewSelected(position, true)
        // Navigate to the right fragment
        when (position) {

            0 -> {
               // NavController().navigateWithId(R.id.myOrderFragment, findNavController())
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_myOrderFragment)
            }
            1 -> {
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_walletFragment)
            }
            2 -> {
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_rewardsFragment)
              //  NavController().navigateWithId(R.id.productListFragment, findNavController())

            }
            3 -> {
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_referFragment)
               // NavController().navigateWithId(R.id.rewardsFragment, findNavController())
            }
            4 -> {
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_walletFragment)
               // NavController().navigateWithId(R.id.walletFragment, findNavController())
            }
            5 -> {
                val fragment = ProfileBottomFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.framelayout, fragment, fragment.javaClass.getSimpleName())
                    .commit()
            }
            6 -> {
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_productListFragment)
            }
            7 -> {
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_remittance)
            }
            8->{
                Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_miscFragment)
               // NavController().navigateWithId(R.id.miscFragment, findNavController())
            }

        }

        // Close the drawer
        mViewHolder?.mDuoDrawerLayout?.closeDrawer()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(com.octal.actorpay.R.id.framelayout, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun on_ItemClickListner(position: Int, mList: List<String>, view: View) {
        when (mList[position]) {
            "Add Money" -> {
                //Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
                //NavController().navigateWithId(R.id.transferMoneyFragment, findNavController())
              /*  NavController().navigateWithIdBack(
                    R.id.transferMoneyFragment,
                    findNavController(),
                    R.id.action_homeFragment_to_transferMoneyFragment
                )*/
            }
            "Send Money" -> {
                //NavController().navigateWithId(R.id.walletFragment, findNavController())
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_walletFragment)
            }
            "Mobile & DTH" -> {
                //NavController().navigateWithId(R.id.transferMoneyFragment, findNavController())
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_walletFragment)
            }
            "Utility Bill" -> {
                //NavController().navigateWithId(R.id.transferMoneyFragment, findNavController())
               // Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_transferMoneyFragment)
            }
            "Online Payment" -> {
                //NavController().navigateWithId(R.id.action_homeFragment_to_transferMoneyFragment, findNavController())
               // Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_transferMoneyFragment)
            }
            "Product List" -> {
                //NavController().navigateWithId(R.id.productListFragment, findNavController())
               // Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_productListFragment)
            }

        }
    }
}