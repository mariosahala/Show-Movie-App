package com.example.moviedb.util

import android.content.Context

fun Context.setIntPref(key: String, value: Int) {
    val sharedPref = this.getSharedPreferences("preperence", Context.MODE_PRIVATE)
    sharedPref.edit().putInt(key, value).apply()
}

fun Context.getIntPref(key: String): Int {
    this.getSharedPreferences("preperence", Context.MODE_PRIVATE).apply {
        return getInt(key, 1)
    }
}

fun Context.setStringPref(key: String, value: String) {
    val sharedPref = this.getSharedPreferences("string", Context.MODE_PRIVATE)
    sharedPref.edit().putString(key, value).apply()
}

fun Context.getStringPref(key: String): String? {
    this.getSharedPreferences("string", Context.MODE_PRIVATE).apply {
        return getString(key, "")
    }

}