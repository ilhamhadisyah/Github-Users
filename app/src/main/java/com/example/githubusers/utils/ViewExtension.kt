package com.example.githubusers.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadUrl(url : String){
    Glide.with(this).clear(this)
    Glide.with(this)
        .load(url)
        .into(this)
}

val <T> T.exhaustive: T
    get() = this