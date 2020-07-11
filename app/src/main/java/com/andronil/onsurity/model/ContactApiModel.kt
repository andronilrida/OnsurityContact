package com.andronil.onsurity.model

import com.google.gson.annotations.SerializedName

/**
 * @author ANIL
 * A POJO class for the representation of API response.
 * */
data class ContactApiModel(@SerializedName("success") val success:Int,
                      @SerializedName("message") val message:String,
                      @SerializedName("data") val data:List<ContactModel>)