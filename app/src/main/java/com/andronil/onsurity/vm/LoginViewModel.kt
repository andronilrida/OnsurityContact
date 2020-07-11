package com.andronil.onsurity.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andronil.onsurity.model.LogInResponse
import com.auth0.android.Auth0
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author ANIL
 *
 * useful for the login and sign up process.
 * */
class LoginViewModel(app:Application):AndroidViewModel(app) {

    private val loginLiveData = MutableLiveData<LogInResponse>()

    /**
     * By the help of this function view can easily observe the emitted data
     * */
    fun subscribeToAuth():LiveData<LogInResponse> = loginLiveData
}