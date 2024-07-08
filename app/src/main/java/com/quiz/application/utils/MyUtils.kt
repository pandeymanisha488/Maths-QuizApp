package com.quiz.application.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import java.io.IOException

fun Dialog.setupDialog(layoutResID: Int) {
    setContentView(layoutResID)
    window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    setCancelable(false)
}


fun Activity.LoadJsonFromAssets(fileName: String): String {
    return try {
        val inputStream = assets.open(fileName)
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()
        String(byteArray, Charsets.UTF_8)
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

fun setfullscreen(activity: Activity){
    activity.window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.longToastShow(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}