package com.octal.actorpay

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.getBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.databinding.ActivityMainBinding
import com.octal.actorpay.ui.dashboard.bottomnavfragments.HomeBottomFragment
import com.octal.actorpay.ui.dashboard.home.HomeFragment
import com.octal.actorpay.viewmodel.ActorPayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    var navController: NavController? = null
    private val viewModel: ActorPayViewModel by  inject()
    private var fragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding here
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val view = binding.root
        setContentView(view)
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_Container) as NavHostFragment).navController

    }


    override fun onSupportNavigateUp(): Boolean {
        if (navController!!.navigateUp() == false) {
            onBackPressed()
        }
        return navController!!.navigateUp()
    }


    @SuppressLint("ResourceType")
    override fun onBackPressed() {


            when (navController!!.currentDestination?.id) {
                R.id.homeFragment -> {
                    if(getCurrentBottomFragment()is HomeBottomFragment){
                        if (doubleBackToExitPressedOnce) {
                            finish()
                            return
                        }
                        doubleBackToExitPressedOnce = true
                        showCustomToast("Press back again");
                        //Toast.makeText(this, "Press back again", Toast.LENGTH_SHORT)
                        lifecycleScope.launch(Dispatchers.Default) {
                            delay(2000)
                            doubleBackToExitPressedOnce = false
                        }
                    }else{
                        loadFragment(HomeBottomFragment.newInstance())
                        if(navController!!.currentDestination!!.id==R.id.homeFragment){

                        }
                    }

                }
                R.id.walletFragment -> navController!!.navigateUp()
                R.id.profileBottomFragment -> navController!!.navigateUp()
                R.id.history_fragment -> navController!!.navigateUp()
                R.id.walletFragment -> navController!!.navigateUp()
                R.id.myOrderFragment -> navController!!.navigateUp()
                R.id.rewardsFragment -> navController!!.navigateUp()
                R.id.miscFragment -> navController!!.navigateUp()
                R.id.remittance -> navController!!.navigateUp()
                R.id.productListFragment -> navController!!.navigateUp()
                R.id.transferMoneyFragment -> navController!!.navigateUp()
                R.id.notificationFragment -> navController!!.navigateUp()
                else -> super.onBackPressed()
            }


       // super.onBackPressed()
    }




}