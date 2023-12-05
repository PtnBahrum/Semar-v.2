package com.example.semarv2.util

import android.content.Context
import android.widget.Toast

fun Context.displayToast(massage: String) {
    Toast.makeText(this, massage, Toast.LENGTH_SHORT).show()
}