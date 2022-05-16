package com.app.daintybox.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.daintybox.R
import com.app.daintybox.activity.ProductActivity
import com.app.daintybox.libs.UtilsDainty
import com.app.daintybox.models.CartItem
import com.bumptech.glide.Glide
import com.google.firebase.firestore.QueryDocumentSnapshot

class AdapterCart(context: Context?, data: ArrayList<CartItem>?) : RecyclerView.Adapter<AdapterCart.ViewHolder>(){

    var data: ArrayList<CartItem>? = null
    private var inflater: LayoutInflater? = null
    var context: Context? = null
    var onItemChange: OnItemChange? = null
    var onItemDeleted: OnItemDeleted? = null

    init {
        this.context = context
        inflater = LayoutInflater.from(context)
        this.data = data
    }

    interface OnItemChange {
        fun onItemChange(cartItem: CartItem)
    }

    interface OnItemDeleted {
        fun onItemDeleted(cartItem: CartItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater!!.inflate(R.layout.layout_item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var car_item: CartItem = data!![position]
        var producto: QueryDocumentSnapshot? = car_item.product

        if(producto!=null) {

            holder.tw_cantidad.text = car_item.cantidad.toString()
            holder.tw_product_name.text = producto.data["name"].toString()
            holder.tw_product_description.text = producto["description"].toString()
            holder.tw_price.text = "$" + producto["price"].toString()

            val images: ArrayList<String> = producto.data["images"] as ArrayList<String>

            if (images.size > 0) {

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

            // Conteo -
            holder.btn_minus.setOnClickListener {
                if(car_item.cantidad>1){
                    car_item.cantidad -= 1
                    holder.tw_cantidad.text = car_item.cantidad.toString()

                    onItemChange?.onItemChange(car_item)
                }
            }

            // Conteo +
            holder.btn_plus.setOnClickListener {
                if(car_item.cantidad<=99){
                    car_item.cantidad += 1
                    holder.tw_cantidad.text = car_item.cantidad.toString()

                    onItemChange?.onItemChange(car_item)
                }
            }

            // Eliminar articulo
            holder.iw_delete.setOnClickListener {
                data!!.remove(car_item)
                notifyItemRemoved(position)

                onItemDeleted?.onItemDeleted(car_item)
            }

            holder.root_view.visibility = View.VISIBLE
        }else{
            holder.root_view.visibility = View.GONE
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
        var tw_cantidad: TextView
        var root_view: View
        var btn_minus: View
        var btn_plus: View
        var iw_delete: ImageView

        init {
            iw_product = itemView.findViewById(R.id.iw_product)
            tw_product_name = itemView.findViewById(R.id.tw_product_name)
            tw_product_description = itemView.findViewById(R.id.tw_product_description)
            tw_price = itemView.findViewById(R.id.tw_price)
            tw_cantidad = itemView.findViewById(R.id.tw_cantidad)
            btn_minus = itemView.findViewById(R.id.btn_minus)
            btn_plus = itemView.findViewById(R.id.btn_plus)
            iw_delete = itemView.findViewById(R.id.iw_delete)
            root_view = itemView
        }
    }

}