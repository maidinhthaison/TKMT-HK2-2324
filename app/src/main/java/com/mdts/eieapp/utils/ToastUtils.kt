package com.mdts.eieapp.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    fun showToast(context: Context,resMessageId: Int, duration : Int){
        Toast.makeText(context, context.resources.getString(resMessageId), duration).show()
    }
    fun showToast(context: Context,resMessageId: String, duration : Int){
        Toast.makeText(context,resMessageId, duration).show()
    }
}
