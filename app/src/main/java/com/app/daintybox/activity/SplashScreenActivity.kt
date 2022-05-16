package com.app.daintybox.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity: DaintyBoxBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth

        // Si ya esta logeado paso al home
        if(auth.currentUser!=null){
            startActivity(Intent(this, HomeActivity::class.java))
        }else {
            startActivity(Intent(this, SplashLoginActivity::class.java))
        }

        finish()
    }

}