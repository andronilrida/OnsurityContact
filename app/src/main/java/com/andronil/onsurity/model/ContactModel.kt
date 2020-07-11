package com.andronil.onsurity.model

import com.google.gson.annotations.SerializedName

/**
 * @author ANIL
 * A generic model used for holding the data for any contact. This is used both in API and local contact.
 * All the serialized names are same as in the json response.
 * */
data class ContactModel(@SerializedName("first_name") val firstName: String,
                        @SerializedName("last_name") val lastName:String,
                        @SerializedName("mobile_number") val mobile:String,
                        @SerializedName("email_id") val email:String,
                        @SerializedName("profile_image") val imageUrl: String)