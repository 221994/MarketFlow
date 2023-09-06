package com.example.marketflow.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.marketflow.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(onSendClick: (String) -> Unit) {
    val dialog = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()
    val etResetEmail: EditText = view.findViewById(R.id.etResetPasswordSheetBottomDialog)
    val buttonSendResetPassword: Button = view.findViewById(R.id.buttonSendSheetDialog)
    val buttonCancelResetPassword: Button = view.findViewById(R.id.buttonCancelSheetDialog)

    buttonSendResetPassword.setOnClickListener {
        val email = etResetEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    buttonCancelResetPassword.setOnClickListener {
        dialog.dismiss()
    }

}