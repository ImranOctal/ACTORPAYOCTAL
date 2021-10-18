package com.octal.actorpay.ui.navController

import androidx.navigation.NavController
import androidx.navigation.NavOptions

class NavController {
    fun navigateWithId(id: Int, navigationController: NavController) {
        navigationController.navigate(id, null, NavOptions.Builder().apply {
            setLaunchSingleTop(true)
            setPopUpTo(navigationController.graph.startDestination, true)
            //navigationController.popBackStack()
        }.build())
    }

    fun navigateWithIdBack(forwardID: Int, navigationController: NavController, backwordID: Int) {
        navigationController.navigate(forwardID, null, NavOptions.Builder().apply {
            setLaunchSingleTop(true)
            setPopUpTo(navigationController.graph.startDestination, true)
            navigationController.popBackStack(backwordID, true)
        }.build())
    }
}