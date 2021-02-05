package com.kakapo.myproject.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.kakapo.myproject.R

@Suppress("DEPRECATION")
class SignUpActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        toolbar = findViewById(R.id.toolbar_sign_up_activity)
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnSignUp = findViewById(R.id.btn_sign_up)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()
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

        setUpBtnSignUp()
    }

    private fun setUpBtnSignUp(){
        btnSignUp.setOnClickListener{
            registerUser()
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean{
        return when{
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email) ->{
                showErrorSnackBar("Please enter an Email")
                false
            }
            TextUtils.isEmpty(password) ->{
                showErrorSnackBar("Please enter a passowrd")
                false
            }
            else -> true
        }
    }

    private fun registerUser(){
        val name: String = etName.text.toString().trim{ it <= ' ' }
        val email: String = etEmail.text.toString().trim{ it <= ' ' }
        val password: String = etPassword.text.toString().trim{ it <= ' ' }

        if(validateForm(name, email, password)){
            Toast.makeText(
                this@SignUpActivity,
                "Now we can register user",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}