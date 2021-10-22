package com.octal.actorpay

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.databinding.ActivityMainBinding
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    var navController: NavController? = null
    private val viewModel: ActorPayViewModel by  inject()
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


    override fun onBackPressed() {
        when (navController!!.currentDestination?.id) {
            R.id.homeFragment -> navController!!.navigateUp()
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