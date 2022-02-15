package com.octal.actorpayuser.ui.dispute

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk.getCacheDir
import com.octal.actorpayuser.R
import com.octal.actorpayuser.databinding.RaiseDisputeDialogBinding
import com.octal.actorpayuser.repositories.methods.MethodsRepo
import com.octal.actorpayuser.repositories.retrofitrepository.models.order.OrderData
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException

class RaiseDisputeDialog(
    private val mContext: Activity,
    val methodsRepo: MethodsRepo,
    private val orderData: OrderData?,
    private val pos:Int,
    val onDone: (title:String,reason:String,file:File?) -> Unit,
): DialogFragment() {

    private var permissions = Manifest.permission.READ_EXTERNAL_STORAGE
    private var prodImage: File? = null
    lateinit var binding:RaiseDisputeDialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(mContext, R.style.MainDialog)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raise_dispute_dialog,
            null,
            false
        )
        dialog.setContentView(binding.root)

        binding.orderItem=orderData!!.orderItemDtos[pos]
        binding.orderNumber.text=orderData.orderNo
        binding.orderAmount.text=getString(R.string.rs).plus(orderData.totalPrice)
        binding.orderDateText.text =
            "Order Date: " + methodsRepo.getFormattedOrderDate(orderData.createdAt)


        binding.back.setOnClickListener {
            dismiss()
        }

        binding.uploadImage.setOnClickListener {
            if (!methodsRepo.checkPermission(
                    mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {

                permReqLauncher.launch(permissions)
            } else {
                fetchImage()
            }
        }
        binding.done.setOnClickListener {
            if(binding.editTitle.text.toString().trim() == "")
                Toast.makeText(mContext,"Please write reason",Toast.LENGTH_SHORT).show()
            else if(binding.editDesc.text.toString().trim() == "")
                Toast.makeText(mContext,"Please write details",Toast.LENGTH_SHORT).show()
            else{
                dismiss()
                onDone(binding.editTitle.text.toString().trim(),binding.editDesc.text.toString().trim(),prodImage)
            }
        }


        return dialog
    }

    private fun fetchImage() {

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        galleryForResult.launch(galleryIntent)
    }

    private val galleryForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    try {
                        val contentURI = data.data
                        cropImage(contentURI!!)

                    } catch (e: IOException) {
                        e.printStackTrace()

                    }

                }

            }
        }


    private fun cropImage(sourceUri: Uri) {
        val destinationUri: Uri = Uri.fromFile(
            File(
                getCacheDir(),
                queryName(mContext.contentResolver, sourceUri)
            )
        )
        val options: UCrop.Options = UCrop.Options()
        options.setCompressionQuality(80)
        options.setToolbarColor(ContextCompat.getColor(mContext, R.color.black))
        options.setStatusBarColor(ContextCompat.getColor(mContext, R.color.black))
        options.setToolbarWidgetColor(ContextCompat.getColor(mContext, R.color.white))

        options.withAspectRatio(1f, 1f)

        val uCrop = UCrop.of(sourceUri, destinationUri)
            .withOptions(options)

        val intent = uCrop.getIntent(mContext)
        cropResult.launch(intent)


    }

    private val cropResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data

                if (data != null) {
                    val resultUri = UCrop.getOutput(data)

                    prodImage = resultUri?.toFile()

                    binding.uploadImage.text = getString(R.string.edit_image)
                    Glide.with(this).load(resultUri).error(R.drawable.logo)
                        .into(binding.image)

                }
            }
        }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->

            if (permission) {
                fetchImage()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        mContext, permissions
                    )
                ) {
                    Toast.makeText(mContext,"Permission Denied, Go to setting to give access",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(mContext,"Permission Denied",Toast.LENGTH_SHORT).show()
                }

            }
        }

    private fun queryName(resolver: ContentResolver, uri: Uri): String {
        val returnCursor: Cursor? =
            resolver.query(uri, null, null, null, null)

        returnCursor.let {

            val nameIndex: Int = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name: String = returnCursor.getString(nameIndex)
            returnCursor.close()
            return name
        }
    }

}