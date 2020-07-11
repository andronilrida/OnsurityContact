package com.andronil.onsurity.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andronil.onsurity.R
import com.andronil.onsurity.databinding.ActivityMainBinding
import com.andronil.onsurity.vm.MainViewModel

/**
 * @author ANIL
 * After logged in successfully this activity displays and loads the contacts from API and phone.
 * It also has an option to log out. After log out user will redirect to the login page again.
 * */
class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        supportActionBar?.title = "Hi, ${intent.getStringExtra(KEY_USER_NAME) ?: "Unknown"}"

        val mainViewModel by viewModels<MainViewModel>()
        val contactAdapter = ContactAdapter(arrayListOf())

        // setting up recyclerview for displaying contacts
        binding?.isInProgress = false
        binding?.rvContacts?.let {
            it.layoutManager = LinearLayoutManager(this@MainActivity)
            it.addItemDecoration(DividerItemDecoration(this,RecyclerView.VERTICAL))
            it.adapter = contactAdapter
        }
        // observing contacts
        mainViewModel.subscribeToContacts().observe(this, Observer {
            binding?.isInProgress = false
            contactAdapter.appendNewContacts(it.contacts)
            showToast("${it.contacts.size} contacts loaded from ${if (it.isFromStorage) "storage" else "api"}")
        })
        // request to loading contacts
        checkPermissionAndLoadContacts(mainViewModel)
    }

    private fun checkPermissionAndLoadContacts(
        mainViewModel: MainViewModel
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),READ_CONTACT_PERMISSION_REQUEST_CODE)
            else{
                binding?.isInProgress = true
                mainViewModel.getContacts()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACT_PERMISSION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val mainViewModel by viewModels<MainViewModel>()
                binding?.isInProgress = true
                mainViewModel.getContacts()
            }else
                showToast("Permission must be granted in order to read the contacts.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        startActivity(LogInActivity.createIntentWithExtra(this,true))
        finish()
    }

    companion object{
        private const val READ_CONTACT_PERMISSION_REQUEST_CODE: Int = 11
        private const val KEY_USER_NAME = "u name"

        /**
         * creates an intent with the given params.
         * @param context from where this activity starts.
         * @param userName is the userName of the logged user.
         * */
        fun createIntentForUser(context: Context,userName:String) = Intent(context,
            MainActivity::class.java).apply {
            putExtra(KEY_USER_NAME,userName)
        }

    }
}