package com.kine.android.connections

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

object Utils {
    /**
     * checks Network connectivity
     *
     * @param context the context to check
     * @return true/false
     */
    @Suppress("DEPRECATION")
    fun isConnected(context: Context?): Boolean {
        context ?: return true
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission") val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

}