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
import com.app.daintybox.adapter.AdapterCart
import com.app.daintybox.models.CartItem
import com.google.firebase.firestore.FieldPath
import java.text.DecimalFormat
import kotlin.collections.ArrayList

class CarritoFragment: FragmentBaseDaintyBox() {
    companion object{
        val TAG: String = "CARRITO_FRAGMENT"
    }

    private lateinit var loading_cart: ProgressBar
    private lateinit var rv_carrito: RecyclerView
    private lateinit var tw_empty_cart: TextView
    private lateinit var tw_total_checkout: TextView
    private var total_checkout: Double = 0.0
    val parsedItems: ArrayList<CartItem> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_carrito,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_cart = view.findViewById(R.id.loading_cart)
        rv_carrito = view.findViewById(R.id.rv_carrito)
        tw_empty_cart = view.findViewById(R.id.tw_empty_cart)
        tw_total_checkout = view.findViewById(R.id.tw_total_checkout)

        getCart()
    }

    fun getCart(){
        rv_carrito.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)


        if(auth.currentUser!=null) {
            val user_cart = db.collection("carrito").document(auth.currentUser!!.uid)

            user_cart.get().addOnSuccessListener { document ->

                if (document != null && document.exists() ) {

                    val items: ArrayList<MutableMap<String,Any>> = document.get("items") as ArrayList<MutableMap<String, Any>>
                    //val parsedItems: ArrayList<CartItem> = ArrayList()
                    val ids_products: ArrayList<String> = ArrayList()

                    for(item in items){
                        val cantidad: Int = item.get("cantidad").toString().toInt()
                        val id_product: String = item.get("id_product").toString()

                        ids_products.add(id_product)
                        parsedItems.add(CartItem(cantidad,id_product))
                    }

                    // Traigo todos los detalles de cada producto en el carrito
                    val rs_detalle_products = db.collection("productos").whereIn(FieldPath.documentId(), ids_products)

                    rs_detalle_products.get().addOnSuccessListener { result ->

                        for(addedItem in parsedItems){

                            for (product in result) {
                                //Log.v("id",product.id)
                                //Log.v("id",addedItem.id_product)

                                if( addedItem.id_product == product.id ){
                                    addedItem.product = product // agrego el detalle del producto
                                    break
                                }
                            }

                        }

                        if(parsedItems.size>0){
                            val adapterItems = AdapterCart(mActivity, parsedItems)
                            rv_carrito.adapter = adapterItems

                            /** callback para actualizar un articulo del carrito **/
                            adapterItems.onItemChange = object : AdapterCart.OnItemChange{
                                override fun onItemChange(cartItem: CartItem) {
                                    for(item in items){
                                        val id_product: String = item.get("id_product").toString()

                                        if(id_product==cartItem.id_product){
                                            item.put("cantidad", cartItem.cantidad)
                                            break
                                        }
                                    }

                                    val new_cart = hashMapOf(
                                        "items" to items
                                    )

                                    // Actualizo en firestore
                                    user_cart.set(new_cart)
                                    calculate_total_checkout()
                                }
                            }

                            /** Callback para borrar el articulo del carrito **/
                            adapterItems.onItemDeleted = object : AdapterCart.OnItemDeleted{
                                override fun onItemDeleted(cartItem: CartItem) {
                                    for(item in items){
                                        val id_product: String = item.get("id_product").toString()

                                        if(id_product==cartItem.id_product){
                                            items.remove(item)
                                            break
                                        }
                                    }

                                    val new_cart = hashMapOf(
                                        "items" to items
                                    )

                                    // Actualizo en firestore
                                    user_cart.set(new_cart)
                                    calculate_total_checkout()
                                }
                            }

                            calculate_total_checkout()
                            tw_empty_cart.visibility = View.GONE
                        }else{
                            tw_empty_cart.visibility = View.VISIBLE
                        }

                        loading_cart.visibility = View.GONE
                    }.addOnFailureListener {
                        loading_cart.visibility = View.GONE
                    }


                }else {
                    Log.d("prod", "No such document")
                    loading_cart.visibility = View.GONE
                    tw_empty_cart.visibility = View.VISIBLE
                }


            }.addOnFailureListener {
                loading_cart.visibility = View.GONE
                tw_empty_cart.visibility = View.VISIBLE
            }

        }
    }

    fun calculate_total_checkout(){
        total_checkout = 0.0

        for(addedItem in parsedItems){
            total_checkout += addedItem.cantidad * addedItem.product?.get("price").toString().toDouble()
        }
        val df = DecimalFormat("#.##")
        tw_total_checkout.text = "$"+df.format(total_checkout)
    }
}