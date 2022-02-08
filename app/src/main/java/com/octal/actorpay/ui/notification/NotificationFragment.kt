package com.octal.actorpay.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.octal.actorpay.base.BaseFragment
import com.octal.actorpay.databinding.FragmentNotificationBinding
import org.koin.android.ext.android.inject


class NotificationFragment : BaseFragment() {


    lateinit var binding: FragmentNotificationBinding
    private val notificationViewModel: NotificationViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }
}