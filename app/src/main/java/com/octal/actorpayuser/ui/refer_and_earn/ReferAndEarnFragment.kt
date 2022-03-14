package com.octal.actorpayuser.ui.refer_and_earn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.octal.actorpayuser.R
import com.octal.actorpayuser.base.BaseFragment
import com.octal.actorpayuser.databinding.FragmentReferAndEarnBinding
import com.octal.actorpayuser.repositories.AppConstance.AppConstance


class ReferAndEarnFragment : BaseFragment() {

    private lateinit var binding:FragmentReferAndEarnBinding

    private var key = ""
    private var contact = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString(AppConstance.KEY_KEY)!!
            contact = it.getString(AppConstance.KEY_CONTACT)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refer_and_earn, container, false)

        if(key != ""){
            binding.btnShare.visibility=View.GONE
            binding.btnInvite.visibility=View.VISIBLE
            binding.contactInvite.visibility=View.VISIBLE
            binding.contactInvite.text="Contact $contact in not on ActorPay, Invite him and earn rewards"
        }
        binding.btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "ActorPay")
            intent.putExtra(Intent.EXTRA_TEXT, "Hey check out new app for transferring money, use my code MART10123 when signup and earn rewards")
            startActivity(Intent.createChooser(intent, "choose one"))
        }
        binding.btnInvite.setOnClickListener {


            if(key != ""){
                if(key == AppConstance.KEY_EMAIL) {


                    val mailto = "mailto:$contact"
                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hey check out new app for transferring money, use my code MART10123 when signup and earn rewards")
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    emailIntent.data = Uri.parse(mailto)
                    startActivity(emailIntent)
                }
                else if(key == AppConstance.KEY_MOBILE){

                    val uri: Uri = Uri.parse("smsto:$contact")
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    intent.putExtra("sms_body", "Hey check out new app for transferring money, use my code MART10123 when signup and earn rewards")
                    startActivity(intent)
                }

            }


        }

        return binding.root
    }



}