package com.kakapo.myproject.activity

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import com.kakapo.myproject.R
import com.kakapo.myproject.firebase.FireStoreClass

class SplashActivity : AppCompatActivity() {

    private lateinit var tvAppName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvAppName = findViewById(R.id.tv_app_name)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        tvAppName.typeface = typeFace

        Handler().postDelayed({

            val currentUserId = FireStoreClass().getCurrentUserId()

            if(currentUserId.isNotEmpty()){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
            }

            finish()
        }, 2500)
    }
}