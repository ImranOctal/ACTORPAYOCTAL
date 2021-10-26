package com.octal.actorpay.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.octal.actorpay.MainActivity
import com.octal.actorpay.R
import com.octal.actorpay.databinding.SignUpScreenFragmentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginActivity.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpScreenFragment : Fragment() {

    private var _binding: SignUpScreenFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = SignUpScreenFragmentBinding.inflate(inflater, container, false)
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
            buttonSignUp.setOnClickListener {
                //NavController().navigateWithId(R.id.homeFragment, findNavController())
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
    }
}