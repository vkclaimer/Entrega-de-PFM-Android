package com.app.daintybox.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.app.daintybox.R
import com.app.daintybox.adapter.AdapterResultSearch
import com.app.daintybox.customviews.GridSpacingItemDecoration
import com.app.daintybox.libs.UtilsDainty
import com.google.firebase.firestore.DocumentSnapshot

class SectionResultActivity: DaintyBoxBaseActivity() {

    var id_section: String? = null
    var name_section: String? = null
    private lateinit var rv_result_search: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_result)
        baseCreate(savedInstanceState)


        /** extra  */
        val extras = intent.extras
        if (extras != null) {
            id_section = extras.getString("id_section","")
            name_section = extras.getString("name_section","")
        }

        if (supportActionBar != null) supportActionBar!!.title = ""
        setActionbarTitle(name_section)
        activeDisplayBack()
        alignTitleStart()
        changeBackButtonColor(R.color.black)

        rv_result_search = findViewById(R.id.rv_result_search)
        rv_result_search.addItemDecoration(GridSpacingItemDecoration(2, UtilsDainty.convertDpToPixel(15,this).toInt(), true))

        getSectionData()
    }

    fun getSectionData(){
        val productDoc = db.collection("productos").whereEqualTo("type",id_section)

        val productos: ArrayList<DocumentSnapshot> = ArrayList()

        productDoc.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("prod", "DocumentSnapshot data: ${document.documents}")
                } else {
                    Log.d("prod", "No such document")
                }


                for (prod in document.documents) {
                    //Log.d("db", "${document.id} => ${document.data}")
                    //Log.d("db", document.data["name"].toString());
                    productos.add(prod)
                }

                val adapterP = AdapterResultSearch(this@SectionResultActivity,productos)

                rv_result_search.adapter = adapterP
            }
            .addOnFailureListener { exception ->
                Log.d("prod", "get failed with ", exception)

            }

    }

}