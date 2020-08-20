package com.kine.android.connections

import android.content.Context
import com.kine.connections.ConnectionChecker

class SimpleConnectionChecker(private val context: Context) : ConnectionChecker {
    override fun isConnected(): Boolean {
        return Utils.isConnected(context)
    }
}