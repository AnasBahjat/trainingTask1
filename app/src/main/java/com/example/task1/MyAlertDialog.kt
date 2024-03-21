package com.example.task1

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess

class MyAlertDialog(private val context : Context) {
    fun showAlertDialog(title : String , msg : String, iconId : Int) {
        val alterDialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setIcon(iconId)
            .setMessage(msg)
            .setNegativeButton("Quit"){ dialog,which ->
                exitProcess(0)
            }
            .setCancelable(false)


        alterDialog.show()
    }
}