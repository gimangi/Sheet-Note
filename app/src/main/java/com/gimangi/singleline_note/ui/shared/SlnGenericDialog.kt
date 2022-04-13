package com.gimangi.singleline_note.ui.shared

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.gimangi.singleline_note.R
import com.gimangi.singleline_note.data.DialogData
import com.gimangi.singleline_note.databinding.ItemDialogGenericBinding

class SlnGenericDialog(private val context: Context) {

    private val dialog = Dialog(context)

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun show(
        dialogText: DialogData,
        onConfirm: () -> Unit,
        onCancel: () -> Unit
    ) {
        val binding = DataBindingUtil.inflate<ItemDialogGenericBinding>(
            LayoutInflater.from(context),
            R.layout.item_dialog_generic,
            null,
            false
        )
        binding.dialogText = dialogText
        binding.btnDialogCancel.setOnClickListener {
            onCancel()
            dialog.dismiss()
        }
        binding.btnDialogConfirm.setOnClickListener {
            onConfirm()
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(R.drawable.rectangle_fill_white_12)

        adjustViewWidth(binding.btnDialogCancel, binding.btnDialogConfirm)     // 버튼 길이를 긴 쪽에 맞춤

        dialog.show()

    }

    private fun adjustViewWidth(view1: TextView, view2: TextView) {

        dialog.setOnShowListener {
            val minView = if (view1.width < view2.width) view1 else view2
            val maxView = if (minView == view1) view2 else view1

            minView.width = maxView.width
        }
    }
}