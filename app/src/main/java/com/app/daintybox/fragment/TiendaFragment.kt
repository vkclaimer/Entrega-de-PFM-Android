package com.app.daintybox.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.daintybox.R
import com.app.daintybox.adapter.AdapterProductos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TiendaFragment: FragmentBaseDaintyBox() {
    companion object{
        val TAG: String = "TIENDA_FRAGMENT"
    }

    // Views
    private lateinit var loading_best_selling: ProgressBar
    private lateinit var rv_best_selling: RecyclerView

    private lateinit var loading_offer: ProgressBar
    private lateinit var rv_offer: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tienda,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_best_selling = view.findViewById(R.id.loading_best_selling)
        rv_best_selling = view.findViewById(R.id.rv_best_selling)
        loading_offer = view.findViewById(R.id.loading_offer)
        rv_offer = view.findViewById(R.id.rv_offer)

        populateBestSelling()
        populateExclusiveOffer()
    }


    fun populateBestSelling(){
        rv_best_selling.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false)

        val productos: ArrayList<QueryDocumentSnapshot> = ArrayList()

        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                loading_best_selling.visibility = View.GONE

                for (document in result) {
                    //Log.d("db", "${document.id} => ${document.data}")
                    //Log.d("db", document.data["name"].toString());
                    productos.add(document)
                }

                val adapterP = AdapterProductos(mActivity,productos)

                rv_best_selling.adapter = adapterP
            }
            .addOnFailureListener { exception ->
                Log.w("db", "Error getting documents.", exception)
            }


    }

    fun populateExclusiveOffer(){
        rv_offer.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false)

        var productos: ArrayList<QueryDocumentSnapshot> = ArrayList()

        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                loading_offer.visibility = View.GONE

                for (document in result) {
                    //Log.d("db", "${document.id} => ${document.data}")
                    //Log.d("db", document.data["name"].toString());
                    productos.add(document)
                }

                val adapterP = AdapterProductos(mActivity,productos)

                rv_offer.adapter = adapterP
            }
            .addOnFailureListener { exception ->
                Log.w("db", "Error getting documents.", exception)
            }


    }
}