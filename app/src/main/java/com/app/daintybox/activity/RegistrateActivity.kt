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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class RegistrateActivity: DaintyBoxBaseActivity() {

    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    private lateinit var et_username: EditText
    private lateinit var btn_registrarme: Button
    private lateinit var btn_iniciar_sesion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrate)

        // Initialize Firebase Auth
        auth = Firebase.auth

        initViews()
        bindEvents()
    }

    fun initViews(){
        et_username = findViewById(R.id.et_username)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        btn_registrarme = findViewById(R.id.btn_registrarme)
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion)
    }

    fun bindEvents(){


        btn_registrarme.setOnClickListener {
            val username = et_username.text.toString()
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if( !email.isEmpty() && !password.isEmpty() && !username.isEmpty() ) {

                UtilsDainty.show_loading_dialog(this,"Creando cuenta..")
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        UtilsDainty.hide_loading_dialog()
                        if (task.isSuccessful) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("register", "createUserWithEmail:success")
                            val user = auth.currentUser

                            user.let {

                                val profileUpdates = userProfileChangeRequest {
                                    displayName = username
                                }

                                it!!.updateProfile(profileUpdates)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d("register", "User profile updated.")
                                        }
                                    }

                                sendBroadcast(Intent(SplashLoginActivity.ACTION_CLOSE))
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            }



                        } else {

                            if(task.exception is FirebaseAuthUserCollisionException){
                                UtilsDainty.show_accept_msg(this,"Ya existe un usuario con este email o usuario")
                            }else {
                                UtilsDainty.show_accept_msg(this,"Hubo un problema al crear el usuario")
                            }

                            // If sign in fails, display a message to the user.
                            Log.d("register", "createUserWithEmail:failure", task.exception)
                        }
                    }


            }else{
                // mostrar popup de error
                UtilsDainty.show_accept_msg(this,"Debes llenar todos los campos")
            }

        }


        btn_iniciar_sesion.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


    }
}