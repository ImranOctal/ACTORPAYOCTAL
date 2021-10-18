package com.octal.actorpay.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.octal.actorpay.databinding.FragmentLoginBinding
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val viewModel: ActorPayViewModel by  inject()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun init() {
        binding.apply {
            val viewPagerAdapter = activity?.let { ViewPagerAdapter(it.supportFragmentManager) }
            viewPager.adapter = viewPagerAdapter
            tabs.setupWithViewPager(viewPager)

            val params =
                LinearLayout.LayoutParams(
                    ViewPager.LayoutParams.WRAP_CONTENT,
                    ViewPager.LayoutParams.WRAP_CONTENT
                )

            viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            viewPager.invalidate();
                            viewPagerAdapter?.notifyDataSetChanged()
                        }
                        1 -> {
                            viewPager.invalidate();
                            viewPagerAdapter?.notifyDataSetChanged()

                        }
                    }
                }
            })
        }


    }
}