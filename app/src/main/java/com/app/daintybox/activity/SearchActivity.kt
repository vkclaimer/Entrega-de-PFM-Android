package com.app.daintybox.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.app.daintybox.R
import com.app.daintybox.adapter.AdapterResultSearch
import com.app.daintybox.customviews.GridSpacingItemDecoration
import com.app.daintybox.dialogfragment.FiltersSearchDialog
import com.app.daintybox.libs.UtilsDainty
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class SearchActivity: DaintyBoxBaseActivity() {

    private lateinit var et_search: EditText
    private lateinit var rv_result_search: RecyclerView
    private lateinit var btn_filters: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        baseCreate(savedInstanceState)

        if (supportActionBar != null) supportActionBar!!.title = ""
        setActionbarTitle("")
        activeDisplayBack()
        changeBackButtonColor(R.color.black)

        et_search = findViewById(R.id.et_search)
        rv_result_search = findViewById(R.id.rv_result_search)
        btn_filters = findViewById(R.id.btn_filters)

        Handler(Looper.getMainLooper()).postDelayed({
            et_search.requestFocus()

            // open keyboard
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT)
        }, 500)


        rv_result_search.addItemDecoration(GridSpacingItemDecoration(2, UtilsDainty.convertDpToPixel(15,this).toInt(), true))


        et_search.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var query: String = et_search.text.toString()

                search_text(query)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        btn_filters.setOnClickListener {
            val filterDialog: FiltersSearchDialog = FiltersSearchDialog()

            filterDialog.onApplyFilters = object : FiltersSearchDialog.OnApplyFilters{

                override fun onApplyFilters(categories: ArrayList<String>,brands: ArrayList<String>) {
                    Log.v("categories", categories.toString())
                    Log.v("brands", brands.toString())

                    search_category_brand(categories,brands)
                }

            }


            supportFragmentManager.beginTransaction().add(filterDialog,"dialog_filters_search").commitAllowingStateLoss()
        }

    }

    fun search_category_brand(categories: ArrayList<String>,brands: ArrayList<String>){

        val productRef = db.collection("productos")


        if(categories.isEmpty()){
            categories.add("")
        }

        if(brands.isEmpty()){
            brands.add("")
        }

        val query_cat = productRef.whereIn("type", categories)
        val quert_brands = productRef.whereIn("brand", brands)

        val firstTask = query_cat.get()
        val secondTask = quert_brands.get()

        val productos: ArrayList<DocumentSnapshot> = ArrayList()

        val combinedTask: Task<*> = Tasks.whenAllSuccess<Any>(firstTask, secondTask).addOnSuccessListener {
            //Log.v("combine",it.get(0).)

            for (result in it) {

                val querySnapshot = result as QuerySnapshot


                for (prod in querySnapshot){
                    Log.d("db", "${prod.data}")
                    productos.add(prod)
                }

                val adapterP = AdapterResultSearch(this@SearchActivity,productos)

                rv_result_search.adapter = adapterP
            }
        }


    }

    fun search_text(query: String){
        val productDoc = db.collection("productos").whereGreaterThanOrEqualTo("name",query)

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

                val adapterP = AdapterResultSearch(this@SearchActivity,productos)

                rv_result_search.adapter = adapterP
            }
            .addOnFailureListener { exception ->
                Log.d("prod", "get failed with ", exception)

            }
    }
}