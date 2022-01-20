package com.octal.actorpay.ui.myOrderList

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.octal.actorpay.R
import com.octal.actorpay.databinding.OrderFilterDialogBinding
import com.octal.actorpay.repositories.methods.MethodsRepo
import com.octal.actorpay.repositories.retrofitrepository.models.order.OrderListParams
import java.text.DecimalFormat
import java.util.*

class OrderFilterDialog(
    private val params: OrderListParams,
    val mContext: Activity,
    val methodsRepo: MethodsRepo,
    val onClick: (OrderListParams) -> Unit
) : Dialog(mContext) {
    override fun show() {
        super.show()
        window?.setGravity(Gravity.BOTTOM)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.order_filter_dialog)
        val binding = DataBindingUtil.inflate<OrderFilterDialogBinding>(
            mContext.layoutInflater,
            R.layout.order_filter_dialog,
            null,
            false
        )
        setContentView(binding.root)
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        ArrayAdapter.createFromResource(
            mContext,
            R.array.status_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Apply the adapter to the spinner
            binding.spinnerStatus.adapter = adapter
        }
        binding.spinnerStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if(position==0){
                    (view as TextView).setTextColor(ContextCompat.getColor(mContext,R.color.gray))
                }
            }

        }

        if (params.orderNo != null)
            binding.orderNumber.setText(params.orderNo)
        if (params.totalPrice != null)
            binding.total.setText(params.totalPrice.toString())
        if (params.startDate != null)
            binding.startDate.setText(params.startDate)
        if (params.endDate != null)
            binding.endDate.setText(params.endDate)
        val array = mContext.resources.getStringArray(R.array.status_array).toMutableList()
        if (params.orderStatus != null) {
            if (array.contains(methodsRepo.getStatus(params.orderStatus!!))) {
                val pos = array.indexOfFirst { it.equals(methodsRepo.getStatus(params.orderStatus!!)) }
                binding.spinnerStatus.setSelection(pos)
            }
        }

        binding.startDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(mContext,  { _, yearR, monthOfYear, dayOfMonth ->

                val f =  DecimalFormat("00")
                val dayMonth=f.format(dayOfMonth)
                val monthYear=f.format(monthOfYear+1)

                binding.startDate.setText("" + yearR + "-" + (monthYear) + "-" + dayMonth)

            }, year, month, day)
            dpd.show()
            dpd.datePicker.maxDate = Date().time
            dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }
        binding.endDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(mContext,  { _, yearR, monthOfYear, dayOfMonth ->

                val f =  DecimalFormat("00")
                val dayMonth=f.format(dayOfMonth)
                val monthYear=f.format(monthOfYear+1)

                binding.endDate.setText("" + yearR + "-" + (monthYear) + "-" + dayMonth)

            }, year, month, day)

            dpd.show()
            dpd.datePicker.maxDate = Date().time
            dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        }

        binding.apply.setOnClickListener {
            var orderNo: String? = null
            var total: Double? = null
            var startDate: String? = null
            var endDate: String? = null
            var status: String? = null
            if ((binding.orderNumber.text.toString().trim() == "").not())
                orderNo = binding.orderNumber.text.toString().trim()
            if ((binding.total.text.toString().trim() == "").not())
                total = binding.total.text.toString().trim().toDouble()
            if ((binding.startDate.text.toString().trim() == "").not())
                startDate = binding.startDate.text.toString().trim()
            if ((binding.endDate.text.toString().trim() == "").not())
                endDate = binding.endDate.text.toString().trim()


            val statusPosition = binding.spinnerStatus.selectedItemPosition
            if (statusPosition != 0) {
                status = array[statusPosition]
                status=methodsRepo.setStatus(status)
            }
            onClick(
                OrderListParams(
                    total,
                    null,
                    startDate,
                    endDate,
                    orderNo,
                    status
                )
            )
            dismiss()
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.reset.setOnClickListener {
            binding.orderNumber.setText("")
            binding.total.setText("")
            binding.startDate.setText("")
            binding.endDate.setText("")
            binding.spinnerStatus.setSelection(0)

        }


    }
}