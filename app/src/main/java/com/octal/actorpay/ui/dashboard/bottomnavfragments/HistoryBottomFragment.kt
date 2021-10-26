package com.octal.actorpay.ui.dashboard.bottomnavfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.octal.actorpay.databinding.FragmentHistoryBottomBinding
import com.octal.actorpay.ui.adapter.AdapterHistory
import com.octal.actorpay.viewmodel.ActorPayViewModel
import org.koin.android.ext.android.inject


class HistoryBottomFragment : Fragment() {
    private var _binding: FragmentHistoryBottomBinding? = null
    private val viewModel: ActorPayViewModel by  inject()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBottomBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Inflate the layout for this fragment
        getHistory()
        init()
        return root
    }
    companion object {
        private var instance: HistoryBottomFragment? = null


        @JvmStatic
        fun newInstance(): HistoryBottomFragment? {

            if (instance == null) {
                instance = HistoryBottomFragment()
            }
            return instance
        }
    }
    fun init(){
        binding.apply {
           /* buttonLogin.setOnClickListener {
                NavController().navigateWithId(R.id.homeFragment,findNavController())
            }*/
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getHistory() {
        binding.rvItemsHistoryID.apply {
            var arraylist: ArrayList<String> = arrayListOf("AddMoney", "AddMoney", "AddMoney")
            adapter = AdapterHistory(arraylist, requireActivity())
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }
    }
}