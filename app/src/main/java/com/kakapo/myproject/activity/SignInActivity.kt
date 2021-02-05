package com.kakapo.myproject.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.kakapo.myproject.R
import com.kakapo.myproject.firebase.FireStoreClass
import com.kakapo.myproject.models.User
import kotlinx.android.synthetic.main.activity_sign_in.*

@Suppress("DEPRECATION")
class SignInActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        toolbar = findViewById(R.id.toolbar_sign_in_activity)
        auth = FirebaseAuth.getInstance()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
        setupBtnSignIn()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_ios_24)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBtnSignIn(){
        btn_sign_in.setOnClickListener {
            signInRegisteredUser()
        }
    }

    private fun signInRegisteredUser(){
        val email: String = et_email_sign_in.text.toString().trim{ it <= ' ' }
        val password: String = et_password_sign_in.text.toString().trim{ it <= ' ' }

        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            FireStoreClass().signInUser(this@SignInActivity)
                        }else{
                            hideProgressDialog()
                            Log.w("SignIn", "SignIn with email:Fail", task.exception)
                            Toast.makeText(
                                    this@SignInActivity,
                                    "Authentication failed. $email, $password",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
        }
    }

    private fun validateForm(email: String, password: String): Boolean{
        return when{
            TextUtils.isEmpty(email) ->{
                showErrorSnackBar("Please enter an Email")
                false
            }
            TextUtils.isEmpty(password) ->{
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> true
        }
    }

    fun signInSuccess(user: User){
        hideProgressDialog()
        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}