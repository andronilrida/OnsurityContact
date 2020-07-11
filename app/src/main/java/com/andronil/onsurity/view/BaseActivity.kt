package com.andronil.onsurity.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
* @author ANIL
 *
 * A parent for all child activities so that common features can be declared at on place.
* */
open class BaseActivity:AppCompatActivity() {
    protected val logTag:String = this.javaClass.simpleName
    private lateinit var preference:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = getSharedPreferences("Preference",Context.MODE_PRIVATE)
        Log.d(logTag, "onCreate: call finished from base.")
    }

    fun showToast(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show();
    }
}