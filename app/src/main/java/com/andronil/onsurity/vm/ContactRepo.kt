package com.andronil.onsurity.vm

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andronil.onsurity.model.ContactApiModel
import com.andronil.onsurity.model.ContactModel
import com.andronil.onsurity.model.ContactsDetail
import com.andronil.onsurity.util.ApiBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author ANIL
 * Manges the data loading part both from API and local.
 * */
class ContactRepo(private val app:Application) {

    private val contactsLiveData = MutableLiveData<ContactsDetail>()

    fun findContactsLiveData(): LiveData<ContactsDetail> = contactsLiveData

    /**
     * fetches contacts from both API and phone.
     * @param fromStorage if <true> load from phone.
    * */
    fun fetchContacts(fromStorage: Boolean) =
        CoroutineScope(Dispatchers.IO).launch {
            if (fromStorage)
                fetchContactsFromStorage()
            fetchContactsFromApi()
        }

    /**
     * fetches contact from the phone storage and push the data to the [contactsLiveData]
     * */
    private fun fetchContactsFromStorage(){
        val contacts = arrayListOf<ContactModel>()
        val contentResolver = app.contentResolver
        val isFound = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null, null)?.use {cursor ->
            val isContactsFound = if (cursor.count > 0 ){
                while (cursor.moveToNext()){
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()
                    val mobile = StringBuilder()
                    val email = StringBuilder()
                    var imageUrl = ""
                    if (phone > 0 ){
                        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id),null)?.use { cursorPhone ->
                            if (cursorPhone.count > 0 )
                                while (cursorPhone.moveToNext()){
                                    if (imageUrl.isEmpty())
                                        imageUrl = try{
                                            cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                                        }catch (e:Exception){
                                            ""
                                        }
                                    mobile.append(cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "\n")
                                }

                        }
                    }
                    contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", arrayOf(id), null)?.use {cursorEmail ->
                        if (cursorEmail.count > 0 )
                            while (cursorEmail.moveToNext())
                                email.append(cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)))
                    }
                    contacts.add(ContactModel(name,"",mobile.toString(),email.toString(),imageUrl))
                }
                true
            }
            else
                false
            isContactsFound
        }
        contactsLiveData.postValue(
            ContactsDetail(
                contacts,
                isFromStorage = true,
                isFailedToLoad = !(isFound ?: true),
                errorMessage = "No contacts found in the phone"
            )
        )
    }

    /**
    * fetches contacts from the API and waits for the response. After that push it to the [contactsLiveData]
    * */
    private suspend fun fetchContactsFromApi() =
            ApiBuilder.getApiClient().fetchContacts().enqueue(object : Callback<ContactApiModel> {
                override fun onFailure(call: Call<ContactApiModel>, t: Throwable) {
                    contactsLiveData.postValue(
                        ContactsDetail(
                            arrayListOf(),
                            isFromStorage = false,
                            isFailedToLoad = true,
                            errorMessage = "failed due to \n $t"
                        )
                    )
                }

                override fun onResponse(
                    call: Call<ContactApiModel>,
                    response: Response<ContactApiModel>
                ) {
                    val contactDetail = if (response.isSuccessful){
                        val contacts = arrayListOf<ContactModel>()
                        contacts.addAll(response.body()?.data ?: emptyList() )
                        ContactsDetail(
                            contacts,
                            isFromStorage = false,
                            isFailedToLoad = false
                        )
                    }else
                        ContactsDetail(
                            arrayListOf(),
                            isFromStorage = false,
                            isFailedToLoad = true,
                            errorMessage = "response unsuccessful"
                        )

                    contactsLiveData.postValue(contactDetail)
                }

            })
}