package com.example.task1

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyAlertDialog(private val context : Context, private val stopTheApp: StopTheApp) {

    private var stopAppListener : StopTheApp? = null
    fun showAlertDialog(title : String , msg : String, iconId : Int) {
        stopAppListener=stopTheApp
        val alterDialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setIcon(iconId)
            .setMessage(msg)
            .setNegativeButton(context.getString(R.string.quit)){ dialog,which ->
               // exitProcess(0)
                stopAppListener?.stopApp()
            }
            .setCancelable(false)


        alterDialog.show()
    }
}