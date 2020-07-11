package com.andronil.onsurity.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.andronil.onsurity.R
import com.andronil.onsurity.databinding.LoginLayoutBinding
import com.andronil.onsurity.vm.LoginViewModel
import com.auth0.android.Auth0
import com.auth0.android.Auth0Exception
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.VoidCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials

/**
 * @author ANIL
 *
 * Useful for both signIn and signUp as both these functions are divided into two fragments which is hosted by this activity.
 * */
class LogInActivity: BaseActivity() {

    private var auth:Auth0? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Auth0(this)
        auth?.isOIDCConformant = true


            supportActionBar?.title = "LogIn"
        val binding = DataBindingUtil.setContentView<com.andronil.onsurity.databinding.LoginLayoutBinding>(this,R.layout.login_layout)
            val vm by viewModels<LoginViewModel>()

        if (intent.getBooleanExtra(KEY_EXTRA_CLEAR_CREDENTIALS,false))
            signOut(binding)
        else{
            // data binding for handling the user action.
            binding.response = getString(R.string.welcome_hint)
            binding.loginVm = vm
            binding.btnSignIn.setOnClickListener { signIn(binding) }
            binding.btnSignUp.setOnClickListener { signUp(binding) }
        }
           /* // observes to the login and sign up response and acts accordingly.
            vm.subscribeToAuth().observe(this, Observer {
                if (it.isForLogin){
                    if (it.isSuccess){
                        preference.edit().apply {
                            putBoolean(KEY_USER_ACCESS_TOKEN,true)
                        }.apply()
                        openMainPage(it.credentials?.accessToken ?: "")
                        finish()
                    }
                    else{
                        binding.response = it.errorMessage
                        showToast("Unable to login. See details on the top of the screen.")
                    }
                }else{
                    if (it.isSuccess)
                        print("")
                    else{
                        binding.response = it.errorMessage
                        showToast("Unable to create account. See details on the top the screen.")
                    }
                }
            })*/
    }

    /**
     * opens the main page
     * @param userName the name of the logged user
     * */
    private fun openMainPage(userName: String) {
        startActivity(MainActivity.createIntentForUser(this,userName))
    }

    /**
     * sign in using AuthO
     * @param binding reference for updating UI
     * */
    private fun signIn(binding: LoginLayoutBinding) {
        openMainPage("")
        //todo uncomment this below code and comment the above line to run with the sign in option.
        /*if (auth != null)
        WebAuthProvider.login(auth!!).withScheme("demo").withAudience(String.format("https://%s/userinfo",getString(R.string.com_auth0_domain)))
            .start(this,object : AuthCallback{
                override fun onSuccess(credentials: Credentials) {
                    Log.d(logTag,"onSuccess: sign in done.")
                    openMainPage(credentials.accessToken ?: "")
                    finish()
                }

                override fun onFailure(dialog: Dialog) {
                    dialog.show()
                }

                override fun onFailure(exception: AuthenticationException?) {
                    Log.e(logTag, "onFailure: failed sign in due to \n $exception")
                    binding.response = "$exception"
                    showToast("failed to sign in due to AuthException")
                }

            })
        else
            showToast("failed to sign in as auth is null.")*/
    }

    private fun signUp(binding: LoginLayoutBinding) {

    }

    /**
     * sign out using AuthO
     * @param binding reference for UI update
     * */
    private fun signOut(binding: LoginLayoutBinding) {
        auth?.let {
            WebAuthProvider.logout(it)
                .withScheme("demo")
                .start(this, object : VoidCallback {
                    override fun onSuccess(payload: Void?) {
                        Log.d(logTag, "onSuccess: sign out complete.")
                        finish()
                    }
                    override fun onFailure(error: Auth0Exception?) {
                        Log.e(logTag, "onFailure: failed to sign out due to \n $error")
                        binding.response = "$error"
                        showToast("failed to do log out. Try again.")
                    }
                })
        }
    }

    companion object{

        private const val KEY_EXTRA_CLEAR_CREDENTIALS = "clear credentials"

        fun createIntentWithExtra(context:Context, isClearCredentials:Boolean ) = Intent(context,LogInActivity::class.java).apply {
            putExtra(KEY_EXTRA_CLEAR_CREDENTIALS,isClearCredentials)
        }
    }
}