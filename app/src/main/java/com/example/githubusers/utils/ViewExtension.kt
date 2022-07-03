package com.example.githubusers.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * This file is used to extend kotlin View class
 */

fun ImageView.loadUrl(url : String){
    Glide.with(this).clear(this)
    Glide.with(this)
        .load(url)
        .into(this)
}

val <T> T.exhaustive: T
    get() = this