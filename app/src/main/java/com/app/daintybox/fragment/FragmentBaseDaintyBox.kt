package com.app.daintybox.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.app.daintybox.activity.DaintyBoxBaseActivity
import com.app.daintybox.libs.UtilsDainty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class FragmentBaseDaintyBox: Fragment() {
    var mActivity: DaintyBoxBaseActivity? = null

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity as DaintyBoxBaseActivity?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UtilsDainty.reset_click_time()

        db = Firebase.firestore

        // Initialize Firebase Auth
        auth = Firebase.auth
    }
}