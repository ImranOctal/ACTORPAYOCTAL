package com.octal.actorpay.ui.content

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.octal.actorpay.R
import com.octal.actorpay.base.BaseActivity
import com.octal.actorpay.databinding.ActivityContentBinding
import com.octal.actorpay.repositories.retrofitrepository.models.content.ContentResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ContentActivity : BaseActivity() {

    private val contentViewModel: ContentViewModel by inject()
    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_content)


        apiResponse()
        installation()

    }


    private fun installation() {
        contentViewModel.getContent(ContentViewModel.type.toString())
        binding.toolbar.backIcon.setOnClickListener {
           onBackPressed()
        }
    }

    private fun apiResponse() {
        lifecycleScope.launch {
            contentViewModel.contentResponseLive.collect{
                when(it){
                    is ContentViewModel.ResponseContentSealed.loading->{
                        showLoadingDialog()
                    }
                    is ContentViewModel.ResponseContentSealed.Success -> {
                        hideLoadingDialog()
                        if (it.response is ContentResponse) {
                            binding.toolbar.title.text=it.response.data.title
                            binding.contentWebview.loadDataWithBaseURL("",it.response.data.contents,"text/html","UTF-8","")

                        }
                        else {
                            showCustomAlert(
                                getString(R.string.please_try_after_sometime),
                                binding.root
                            )
                        }

                    }
                    is ContentViewModel.ResponseContentSealed.ErrorOnResponse -> {
                        hideLoadingDialog()
                        showCustomAlert(
                            it.message!!.message,
                            binding.root
                        )
                    }
                    else -> {
                        hideLoadingDialog()
                    }
                }

            }
        }
    }

    override fun onBackPressed() {
      finish()
    }
}