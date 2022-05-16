package com.app.daintybox.activity

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.app.daintybox.R
import com.app.daintybox.libs.UtilsDainty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class DaintyBoxBaseActivity : AppCompatActivity() {

    var backButtonActive = false
    var mActionBar: ActionBar? = null
    var mToolbar: Toolbar? = null

    val KEY_SAVED_BACK = "back_flag"
    var color_status_bar = 0

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    open fun baseCreate(savedInstanceState: Bundle?) {
        UtilsDainty.reset_click_time()
        if (savedInstanceState != null) {
            backButtonActive =
                savedInstanceState.getBoolean(KEY_SAVED_BACK)
        } else {
            backButtonActive = false
        }
        init_views()
    }

    open fun init_views() {

        /** Toolbar acting like actionbar  */
        mToolbar = findViewById(R.id.toolbar_daintybox)
        if (mToolbar != null) {
            try {
                setSupportActionBar(mToolbar)
            } catch (t: Throwable) {
                /** WTF Samsung 4.2.2  */
            }

            //set_toolbar_color();
        }
    }

    open fun setActionbarTitle(title: String?) {
        val toolbar: Toolbar = findViewById(R.id.toolbar_daintybox)
        if (toolbar != null) {

            /** Le doy titulo al fragment  */
            val mTitle: TextView = toolbar.findViewById(R.id.toolbar_title)
            if (mTitle != null) {
                mTitle.text = title
                mTitle.visibility = TextView.VISIBLE
            }
            /** Quito el logo del fragment  */
            val mTitleLogo: ImageView = toolbar.findViewById(R.id.title_image)
            if (mTitleLogo != null) {
                mTitleLogo.visibility = ImageView.GONE
            }

        }
    }

    open fun offActionbarTitle() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_daintybox)
        if (toolbar != null) {

            /** Quito el titulo  */
            val mTitle: TextView = toolbar.findViewById(R.id.toolbar_title)
            mTitle.visibility = TextView.GONE

            /** Muestro el logo  */
            val mTitleLogo: ImageView = toolbar.findViewById(R.id.title_image)
            if (mTitleLogo != null) {
                mTitleLogo.visibility = ImageView.VISIBLE
            }


        }
    }

    open fun alignTitleStart() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_daintybox)
        if (toolbar != null) {
            val mTitle: TextView = toolbar.findViewById(R.id.toolbar_title)
            if (mTitle != null && mTitle.gravity != Gravity.START) {
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                params.weight = 1f
                params.leftMargin = 0
                mTitle.gravity = Gravity.START
                mTitle.gravity = Gravity.CENTER_VERTICAL
                mTitle.layoutParams = params
            }
        }
    }

    open fun activeDisplayBack() {
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setHomeButtonEnabled(true)
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar_daintybox)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     *
     * Mostrar el botón de back en el actionbar
     *
     */
    open fun shouldDisplayHomeUp() {
        backButtonActive = true
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setHomeButtonEnabled(false)
            mActionBar!!.setHomeButtonEnabled(true)
            mActionBar!!.setDisplayHomeAsUpEnabled(false)
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     *
     * Back button enabled
     *
     */
    open fun backButtonEnabled() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_daintybox)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    open fun offBackButton() {
        backButtonActive = false
        mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar!!.setHomeButtonEnabled(true)
            mActionBar!!.setHomeButtonEnabled(false)
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar!!.setDisplayHomeAsUpEnabled(false)
        }
    }

    open fun setAlphaToolbarTitle(alpha: Float) {
        val toolbar = findViewById<View>(R.id.toolbar_daintybox) as Toolbar
        if (toolbar != null) {
            /** Le doy titulo al fragment  */
            val mTitle = toolbar.findViewById<View>(R.id.toolbar_title) as TextView
            if (mTitle != null) {
                mTitle.alpha = alpha
            }
        }
    }

    open fun set_status_bar_color(color: Int, save: Boolean) {
        if (save) color_status_bar = color
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (color > 0) {
                window.decorView.systemUiVisibility = 0
                window.statusBarColor = ContextCompat.getColor(this, color)
            } else {
                if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = ContextCompat.getColor(this, R.color.green_cane)
                }
            }
        }
    }


    open fun set_status_bar_transparent() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            val w = window
            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            w.decorView.systemUiVisibility = 0
        }
    }

    open fun set_toolbar_color() {
        val ab = supportActionBar
        if (ab != null) {
            ab.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.green_cane)))
        }
    }

    open fun set_toolbar_color(color: Int) {
        val ab = supportActionBar
        ab?.setBackgroundDrawable(ColorDrawable(resources.getColor(color)))
    }

    open fun changeBackButtonColor(color: Int) {
        val ab = supportActionBar
        if (ab != null) {
            val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
            if (upArrow != null) {
                upArrow.setColorFilter(
                    ContextCompat.getColor(this, color),
                    PorterDuff.Mode.SRC_ATOP
                )
                ab.setHomeAsUpIndicator(upArrow)
            }
        }
        try {
            val toolbar = findViewById<View>(R.id.toolbar_daintybox) as Toolbar
            val mCollapseIcon = toolbar.javaClass.getDeclaredField("mCollapseIcon")
            mCollapseIcon.isAccessible = true
            val drw = mCollapseIcon[toolbar] as Drawable
            drw?.setTint(ContextCompat.getColor(this, color))
        } catch (e: Exception) {
        }
    }


}