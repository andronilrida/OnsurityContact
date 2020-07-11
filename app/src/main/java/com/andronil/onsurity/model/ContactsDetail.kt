package com.andronil.onsurity.model

import com.andronil.onsurity.model.ContactModel

data class ContactsDetail(val contacts:ArrayList<ContactModel>, val isFromStorage:Boolean, val isFailedToLoad:Boolean, val errorMessage:String? = null)