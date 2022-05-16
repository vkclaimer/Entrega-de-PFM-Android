package com.app.daintybox.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.app.daintybox.R
import com.app.daintybox.activity.LoginActivity
import com.app.daintybox.activity.SplashLoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PerfilFragment: FragmentBaseDaintyBox() {
    companion object{
        val TAG: String = "PERFIL_FRAGMENT"
    }

    var tw_username: TextView? = null
    var tw_email: TextView? = null
    var btn_cerrar_sesion: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tw_username = view.findViewById(R.id.tw_username)
        tw_email = view.findViewById(R.id.tw_email)
        btn_cerrar_sesion = view.findViewById(R.id.btn_cerrar_sesion)

        populateUser()
        bindEvents()
    }

    fun populateUser(){
        tw_username?.text = auth.currentUser?.displayName
        tw_email?.text = auth.currentUser?.email
    }

    fun bindEvents(){


        btn_cerrar_sesion?.setOnClickListener {
            auth.signOut()

            mActivity?.startActivity(Intent(mActivity,SplashLoginActivity::class.java))
            mActivity?.finish()
        }

    }
}