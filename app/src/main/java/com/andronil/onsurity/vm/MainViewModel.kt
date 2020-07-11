package com.andronil.onsurity.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class MainViewModel(app:Application):AndroidViewModel(app) {

    private val contactRepo = ContactRepo(app)

    fun subscribeToContacts() = contactRepo.findContactsLiveData()

    fun getContacts(fromStorage:Boolean = true) = contactRepo.fetchContacts(fromStorage)
}