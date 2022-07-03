package com.example.githubusers.utils

import android.content.Context
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.example.githubusers.R
import com.google.android.material.snackbar.Snackbar

object Utility {
    fun displayErrorSnackBar(view: View, s: String, context: Context) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG)
            .withColor(ContextCompat.getColor(context, R.color.red))
            .setTextColor(ContextCompat.getColor(context, R.color.white))
            .show()
    }

    /**
     * This function helps to set the color of the snackBar as used in 'displayErrorSnackBar' function above
     */

    private fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar {
        this.view.setBackgroundColor(colorInt)
        return this
    }
}
