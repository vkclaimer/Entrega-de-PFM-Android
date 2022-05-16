package com.app.daintybox.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.daintybox.R
import com.app.daintybox.activity.ProductActivity
import com.app.daintybox.libs.UtilsDainty
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterProductos(context: Context?, data: ArrayList<QueryDocumentSnapshot>?) : RecyclerView.Adapter<AdapterProductos.ViewHolder>(){

    var data: ArrayList<QueryDocumentSnapshot>? = null
    private var inflater: LayoutInflater? = null
    var context: Context? = null
    var auth: FirebaseAuth
    var db: FirebaseFirestore

    // data del carrito
    var user_cart: DocumentReference? = null
    var cart_items: ArrayList<MutableMap<String, Any>> = ArrayList()
    var cart_exist: Boolean = false

    init {
        this.context = context
        inflater = LayoutInflater.from(context)
        this.data = data

        db = Firebase.firestore
        // Initialize Firebase Auth
        auth = Firebase.auth
        getCarritoData()
    }

    fun getCarritoData(){
        if(auth.currentUser!=null) {
            user_cart = db.collection("carrito").document(auth.currentUser!!.uid)

            user_cart!!.get().addOnSuccessListener { document ->

                if (document != null && document.exists()) {
                    cart_items = document.get("items") as ArrayList<MutableMap<String, Any>>
                    cart_exist = true
                }else{
                    cart_exist = false
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater!!.inflate(R.layout.layout_item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        val producto: QueryDocumentSnapshot = data!![position]

        holder.tw_product_name.text = producto.data["name"].toString()
        holder.tw_product_description.text = producto["description"].toString()
        holder.tw_price.text = "$"+producto["price"].toString()

        val images: ArrayList<String> = producto.data["images"] as ArrayList<String>

        if(images.size>0) {

            Glide
                .with(this.context!!)
                .load(images[0])
                .into(holder.iw_product)

        }

        holder.root_view.setOnClickListener {
            if(UtilsDainty.isFirstClick()) {
                val i_prod = Intent(context, ProductActivity::class.java)
                i_prod.putExtra("id_product", producto.id)

                context?.startActivity(i_prod)
            }
        }

        holder.ib_add_cart.setOnClickListener {

            if(cart_exist){

                val new_cart_item = mutableMapOf(
                    "cantidad" to 1,
                    "id_product" to producto.id
                )

                //Verifico si ya existe el producto
                var is_added = false
                for(item in cart_items){
                    val id_product: String = item.get("id_product").toString()

                    if(id_product==producto.id){
                        is_added = true
                        break
                    }
                }

                if(!is_added) {
                    cart_items.add(new_cart_item as MutableMap<String, Any>)
                }

                val new_cart = hashMapOf(
                    "items" to cart_items
                )

                user_cart?.set(new_cart)
            }else{

                val new_cart_item = mutableMapOf(
                    "cantidad" to 1,
                    "id_product" to producto.id
                )

                cart_items.add(new_cart_item as MutableMap<String, Any>)

                val new_cart = hashMapOf(
                    "items" to cart_items
                )

                // No existe el carrito asique lo creo
                db.collection("carrito").document(auth.currentUser!!.uid).set(new_cart)
            }


            Toast.makeText(context, R.string.item_added_cart, Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iw_product: ImageView
        var tw_product_name: TextView
        var tw_product_description: TextView
        var tw_price: TextView
        var ib_add_cart: ImageButton
        var root_view: View

        init {
            iw_product = itemView.findViewById(R.id.iw_product)
            tw_product_name = itemView.findViewById(R.id.tw_product_name)
            tw_product_description = itemView.findViewById(R.id.tw_product_description)
            tw_price = itemView.findViewById(R.id.tw_price)
            ib_add_cart = itemView.findViewById(R.id.ib_add_cart)
            root_view = itemView
        }
    }
}