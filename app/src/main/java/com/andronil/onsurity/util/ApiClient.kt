package com.andronil.onsurity.util

import com.andronil.onsurity.model.ContactApiModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * derives all the functions for API call with given endpoints.
 * */
interface ApiClient {

    @GET("/prod/contact-list")
    fun fetchContacts(): Call<ContactApiModel>
}