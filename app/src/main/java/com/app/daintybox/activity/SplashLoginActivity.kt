package com.app.daintybox.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import com.app.daintybox.R

class SplashLoginActivity : DaintyBoxBaseActivity() {

    companion object {
        val ACTION_CLOSE = "SplashLoginActivity.ACTION_CLOSE"
    }

    lateinit var receiver: SplashReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_login)

        /** Creacion de objectos genericos para cualquier actividad daintybox **/
        baseCreate(savedInstanceState)


        /** broadcast intent **/
        /** broadcast intent  */
        val filter = IntentFilter()
        filter.addAction(ACTION_CLOSE)
        receiver = SplashReceiver()
        registerReceiver(receiver, filter)

        val btn_registrarme: Button = findViewById(R.id.btn_registrarme)
        val btn_iniciar_sesion: Button = findViewById(R.id.btn_iniciar_sesion)


        btn_iniciar_sesion.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btn_registrarme.setOnClickListener {
            startActivity(Intent(this, RegistrateActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    inner class SplashReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if( intent.action != null && intent.action == ACTION_CLOSE ) {
                this@SplashLoginActivity.finish()
            }

        }
    }

}