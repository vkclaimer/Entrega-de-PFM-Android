package com.app.daintybox.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.app.daintybox.R
import com.app.daintybox.libs.UtilsDainty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : DaintyBoxBaseActivity() {

    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_iniciar_sesion: Button
    private lateinit var btn_registrate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = Firebase.auth

        initViews()
        bindEvents()
    }

    fun initViews(){
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion)
        btn_registrate = findViewById(R.id.btn_registrate)
    }

    fun bindEvents(){


        btn_iniciar_sesion.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if(!email.isEmpty() && !password.isEmpty()) {

                UtilsDainty.show_loading_dialog(this,"Logueando..")
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        UtilsDainty.hide_loading_dialog()
                        if (task.isSuccessful) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithEmail:success")
                            val user = auth.currentUser

                            if(user!=null) {
                                sendBroadcast(Intent(SplashLoginActivity.ACTION_CLOSE))
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            }

                        } else {
                            UtilsDainty.show_accept_msg(this,"El usuario o contraseña no son correctos")

                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithEmail:failure", task.exception)
                        }
                    }


            }else{
                // mostrar popup de error
                UtilsDainty.show_accept_msg(this,"Debes llenar todos los campos")
            }

        }


        btn_registrate.setOnClickListener {
            startActivity(Intent(this, RegistrateActivity::class.java))
            finish()
        }

    }

}