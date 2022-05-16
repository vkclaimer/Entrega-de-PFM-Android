package com.app.daintybox.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.daintybox.R
import com.app.daintybox.adapter.AdapterProdFavoritos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoritosFragment : FragmentBaseDaintyBox() {
    companion object{
        val TAG: String = "FAVORITOS_FRAGMENT"
    }

    private lateinit var loading_favoritos: ProgressBar
    private lateinit var rv_favoritos: RecyclerView
    private lateinit var tw_empty_favs: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favoritos,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_favoritos = view.findViewById(R.id.loading_favoritos)
        rv_favoritos = view.findViewById(R.id.rv_favoritos)
        tw_empty_favs = view.findViewById(R.id.tw_empty_favs)

        getFavorites()
    }

    fun getFavorites(){
        rv_favoritos.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)

        if(auth.currentUser!=null) {
            val user_favorites = db.collection("favoritos").document(auth.currentUser!!.uid)

            user_favorites.get().addOnSuccessListener { document ->

                if (document != null && document.exists() ) {

                    val ids_productos: ArrayList<String> = document.data?.get("productos") as ArrayList<String>

                    if(ids_productos.size>0) {

                        val rs_favoritos = db.collection("productos").whereIn(FieldPath.documentId(), ids_productos)
                        val productos: ArrayList<QueryDocumentSnapshot> = ArrayList()

                        rs_favoritos.get().addOnSuccessListener { result ->

                            for (product in result) {
                                Log.d("db", product.data["name"].toString());
                                productos.add(product)
                            }

                            if (productos.size > 0) {

                                val adapterFavs = AdapterProdFavoritos(mActivity, productos)
                                rv_favoritos.adapter = adapterFavs

                                tw_empty_favs.visibility = View.GONE
                            } else {
                                tw_empty_favs.visibility = View.VISIBLE
                            }

                            loading_favoritos.visibility = View.GONE
                        }.addOnFailureListener {
                            loading_favoritos.visibility = View.GONE
                        }

                    }else{
                        tw_empty_favs.visibility = View.VISIBLE
                        loading_favoritos.visibility = View.GONE
                    }

                } else {
                    Log.d("prod", "No such document")
                    tw_empty_favs.visibility = View.VISIBLE
                    loading_favoritos.visibility = View.GONE
                }

            }.addOnFailureListener { exception ->
                Log.d("prod", "get failed with ", exception)
                loading_favoritos.visibility = View.GONE
                tw_empty_favs.visibility = View.VISIBLE
            }

        }
    }
}