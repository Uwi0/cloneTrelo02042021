package com.kakapo.myproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.kakapo.myproject.R

@Suppress("DEPRECATION")
class IntroActivity : BaseActivity() {

    private lateinit var btnSignUpIntro: Button
    private lateinit var btnSignInIntro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        btnSignUpIntro = findViewById(R.id.btn_sign_up_intro)
        btnSignInIntro = findViewById(R.id.btn_sign_in_intro)

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setSignUp()
        setSignIn()
    }

    private fun setSignUp(){
        btnSignUpIntro.setOnClickListener{
            val intent = Intent(this@IntroActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setSignIn(){
        btnSignInIntro.setOnClickListener{
            val intent = Intent(this@IntroActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}