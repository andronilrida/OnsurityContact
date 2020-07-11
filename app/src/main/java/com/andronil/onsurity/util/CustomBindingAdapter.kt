package com.andronil.onsurity.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("android:url")
fun loadUrl(iv:ImageView,url:String?){

    if (url?.trim()?.isNotEmpty() == true)
    Picasso.get().load(url).into(iv)
}

@BindingAdapter("android:visible")
fun setVisible(view:View, isVisible:Boolean){
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}