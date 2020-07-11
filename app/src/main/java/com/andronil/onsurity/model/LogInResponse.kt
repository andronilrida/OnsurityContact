package com.andronil.onsurity.model

import com.auth0.android.result.Credentials

/**
 * @author ANIL
 *
 * A model holding data for the
 * */
data class LogInResponse(val isForLogin:Boolean , val isSuccess:Boolean, val credentials:Credentials? = null, val errorMessage:String? = null)