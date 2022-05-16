package com.app.daintybox.activity

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.app.daintybox.R
import com.app.daintybox.adapter.AdapterPicsProduct
import com.app.daintybox.libs.UtilsDainty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.viewpagerindicator.LinePageIndicator

class ProductActivity: DaintyBoxBaseActivity() {

    var id_product: String? = null

    private lateinit var loading: ProgressBar
    private lateinit var sc_main: ScrollView
    private lateinit var tw_product_name: TextView
    private lateinit var tw_product_description: TextView
    private lateinit var tw_product_details: TextView
    private lateinit var tw_price: TextView
    private lateinit var vp_product_images: ViewPager
    private lateinit var lp_indicator_p_images: LinePageIndicator
    private lateinit var holder_btn_add_cart: RelativeLayout
    private lateinit var btn_add_cart: Button
    private lateinit var ib_like: ImageButton

    var height_header_toolbar = 0
    var params_toolbar: FrameLayout.LayoutParams? = null
    private lateinit var v_status_bar_background: View
    private var mToolbarBackgroundColor = 0
    private val last_scrollY = 0
    private val flag_scrollY = false

    // color actual de los iconos del toolbar
    var current_color_icons = "white"

    // data del carrito
    var user_cart: DocumentReference? = null
    var cart_items: ArrayList<MutableMap<String, Any>> = ArrayList()
    var cart_exist: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        baseCreate(savedInstanceState)

        get_toolbar_header_height()
        set_status_bar_transparent()

        /** Id restaurant **/
        /** Id restaurant  */
        val extras = intent.extras
        if (extras != null) {
            id_product = extras.getString("id_product","")
        }

        Log.v("prod",id_product!!)

        supportActionBar?.title = ""
        setActionbarTitle("")
        alignTitleStart()
        activeDisplayBack()

        loading = findViewById(R.id.loading)
        sc_main = findViewById(R.id.sc_main)
        tw_product_name = findViewById(R.id.tw_product_name)
        tw_product_description = findViewById(R.id.tw_product_description)
        tw_product_details = findViewById(R.id.tw_product_details)
        tw_price = findViewById(R.id.tw_price)
        vp_product_images = findViewById(R.id.vp_product_images)
        lp_indicator_p_images = findViewById(R.id.lp_indicator_p_images)
        holder_btn_add_cart = findViewById(R.id.holder_btn_add_cart)
        btn_add_cart = findViewById(R.id.btn_add_cart)
        v_status_bar_background = findViewById(R.id.v_status_bar_background)
        ib_like = findViewById(R.id.ib_like)

        /***
         *
         * SI es menor a lollipop no tiene el statusbar transparente
         *
         */
        /***
         *
         * SI es menor a lollipop no tiene el statusbar transparente
         *
         */
        if (Build.VERSION.SDK_INT < VERSION_CODES.M) {
            v_status_bar_background.visibility = View.GONE
            setMarginTopToolbar(0)
        }
        set_status_bar_color(0, true)
        changeBackButtonColor(R.color.black)


        getProductData()
        getCarritoData()
    }

    fun set_parallax_header() {

        // Color toolbar
        mToolbarBackgroundColor = ContextCompat.getColor(this,R.color.green_cane)
        //if (gradient_top != null) gradient_top.setVisibility(View.VISIBLE)
        if (sc_main != null) {
            sc_main.getViewTreeObserver().addOnScrollChangedListener(OnScrollChangedListener {
                val scrollY: Int = sc_main.getScrollY() // For ScrollView
                update_toolbar_alpha(scrollY)
            })
        }

        // Evento para update del texto en el espacio flexible
        updateScrollPosition()
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

    fun getFavoriteData() {
        var is_favorite = false

        if(auth.currentUser!=null) {

            val user_favorites = db.collection("favoritos").document(auth.currentUser!!.uid)

            user_favorites.get().addOnSuccessListener { document ->

                if( document!=null && document.exists() ) {
                    val ids_productos: ArrayList<String> = document.data?.get("productos") as ArrayList<String>


                    if( ids_productos.any { it == id_product } ){
                        is_favorite = true
                    }

                    populateFavorite(is_favorite)
                }else{
                    populateFavorite(is_favorite)
                }
            }

        }
    }

    fun populateFavorite(is_favorite: Boolean){

        // Favorito
        if(is_favorite){
            ib_like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_favorito_product_active))
            ib_like.isActivated = true
        }else{
            ib_like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_favorito_product))
            ib_like.isActivated = false
        }


        ib_like.setOnClickListener {

            if( it.isActivated ){
                ib_like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_favorito_product))
                ib_like.isActivated = false
            }else{
                ib_like.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_favorito_product_active))
                ib_like.isActivated = true
            }

            val user_favorites = db.collection("favoritos").document(auth.currentUser!!.uid)

            user_favorites.get().addOnSuccessListener { document ->

                if (document != null && document.exists() ) {
                    val ids_productos: ArrayList<String> = document.data?.get("productos") as ArrayList<String>


                    if(it.isActivated){
                        ids_productos.add(id_product!!)
                    }else{
                        ids_productos.remove(id_product)
                    }

                    val new_data = hashMapOf(
                        "productos" to ids_productos
                    )

                    user_favorites.set(new_data)
                }else{

                    val ids_productos: ArrayList<String> = ArrayList()


                    if(it.isActivated){
                        ids_productos.add(id_product!!)
                    }else{
                        ids_productos.remove(id_product)
                    }

                    val new_data = hashMapOf(
                        "productos" to ids_productos
                    )

                    // No existe la lista de favoritos para el usuario asi que la creo
                    db.collection("favoritos").document(auth.currentUser!!.uid).set(new_data)

                }
            }

        }
    }

    fun getProductData(){
        loading.visibility = View.VISIBLE

        val productDoc = db.collection("productos").document(id_product!!)
        productDoc.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("prod", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("prod", "No such document")
                }

                populateProduct(document)
                set_parallax_header()

                loading.visibility = View.GONE
                sc_main.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
                Log.d("prod", "get failed with ", exception)
                loading.visibility = View.GONE
            }
    }

    fun populateProduct(producto: DocumentSnapshot){
        setActionbarTitle(producto["name"].toString())

        tw_product_name.text = producto["name"].toString()

        getFavoriteData()
        populatePictures(producto)

        tw_product_description.text = producto["description"].toString()
        tw_product_details.text = producto["details"].toString()
        tw_price.text = "$"+producto["price"].toString()

        btn_add_cart.setOnClickListener {

            if(cart_exist){

                val new_cart_item = mutableMapOf(
                    "cantidad" to 1,
                    "id_product" to id_product
                )

                //Verifico si ya existe el producto
                var is_added = false
                for(item in cart_items){
                    val id_product: String = item.get("id_product").toString()

                    if(id_product==this.id_product){
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
                    "id_product" to id_product
                )

                cart_items.add(new_cart_item as MutableMap<String, Any>)

                val new_cart = hashMapOf(
                    "items" to cart_items
                )

                // No existe el carrito asique lo creo
                db.collection("carrito").document(auth.currentUser!!.uid).set(new_cart)
            }


            Toast.makeText(this, R.string.item_added_cart, Toast.LENGTH_SHORT).show()
        }

        holder_btn_add_cart.visibility = View.VISIBLE
    }

    fun populatePictures(producto: DocumentSnapshot){
        val images: ArrayList<String> = producto["images"] as ArrayList<String>
        var adapterPics: AdapterPicsProduct = AdapterPicsProduct(this,images)
        vp_product_images.adapter = adapterPics

        lp_indicator_p_images.setViewPager(vp_product_images)
    }

    fun getFlagScroll(): Boolean {
        return flag_scrollY
    }

    fun updateScrollPosition() {
        update_toolbar_alpha(last_scrollY)
    }

    fun update_toolbar_alpha(scrollY: Int) {
        if (tw_product_name != null) {
            val top_riv = UtilsDainty.convertDpToPixel(170, this).toInt()
            if (scrollY >= top_riv) {
                setBackgroundAlpha(mToolbar!!, 1f, mToolbarBackgroundColor)
                setBackgroundAlpha(v_status_bar_background, 1f, mToolbarBackgroundColor)
                setAlphaToolbarTitle(1f)
                //gradient_top.setAlpha(0f)
                if (!current_color_icons.equals("black", ignoreCase = true)) {
                    set_status_bar_color(0, true)
                    changeBackButtonColor(R.color.black)
                    //change_icons_options_color(R.color.black)
                    current_color_icons = "black"
                }
            } else {
                val alpha_percent: Float =
                    scrollY.toFloat() * 100 / (top_riv.toFloat() - UtilsDainty.convertDpToPixel(
                        50,
                        this
                    )) / 100 - UtilsDainty.convertDpToPixel(
                        50,
                        this
                    ) * 100 / top_riv.toFloat() / 100

                /*Log.v("alpha_percent",alpha_percent+"");
                Log.v("scrollY",scrollY+"");
                Log.v("top_title_rest",top_title_rest+"");*/if (alpha_percent > 0) {
                    setBackgroundAlpha(mToolbar!!, alpha_percent, mToolbarBackgroundColor)
                    setBackgroundAlpha(
                        v_status_bar_background,
                        alpha_percent,
                        mToolbarBackgroundColor
                    )
                    setAlphaToolbarTitle(alpha_percent)
                    //gradient_top.setAlpha(1.0f - alpha_percent)
                    if (alpha_percent > 0.5) {
                        if (!current_color_icons.equals("black", ignoreCase = true)) {
                            set_status_bar_color(0, true)
                            changeBackButtonColor(R.color.black)
                            //change_icons_options_color(R.color.black)
                            current_color_icons = "black"
                        }
                    } else {
                        if (!current_color_icons.equals("white", ignoreCase = true)) {
                            //set_status_bar_transparent()
                            set_status_bar_color(0, true)
                            changeBackButtonColor(R.color.black)
                            //change_icons_options_color(R.color.white)
                            current_color_icons = "white"
                        }
                    }
                } else {
                    mToolbar?.let { setBackgroundAlpha(it, 0f, mToolbarBackgroundColor) }
                    setBackgroundAlpha(v_status_bar_background, 0f, mToolbarBackgroundColor)
                    setAlphaToolbarTitle(0f)
                    //gradient_top.setAlpha(1f)
                    if (!current_color_icons.equals("white", ignoreCase = true)) {
                        //set_status_bar_transparent()
                        set_status_bar_color(0, true)
                        changeBackButtonColor(R.color.black)
                        //change_icons_options_color(R.color.white)
                        current_color_icons = "white"
                    }
                }
            }
        }
    }

    private fun setBackgroundAlpha(view: View, alpha: Float, baseColor: Int) {
        val a = Math.min(255, Math.max(0, (alpha * 255).toInt())) shl 24
        val rgb = 0x00ffffff and baseColor
        view.setBackgroundColor(a + rgb)
    }

    fun setMarginTopToolbar(margin: Int) {
        if (mToolbar != null) {
            if (params_toolbar == null) {
                params_toolbar = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    height_header_toolbar
                )
            }
            params_toolbar!!.topMargin = UtilsDainty.convertDpToPixel(margin, this).toInt()
            mToolbar!!.layoutParams = params_toolbar
            mToolbar!!.requestLayout()
        }
    }

    fun get_toolbar_header_height() {
        val value = TypedValue()
        if (baseContext.theme.resolveAttribute(android.R.attr.actionBarSize, value, true)) {

            height_header_toolbar = TypedValue.complexToDimensionPixelSize(
                value.data,
                baseContext.resources.displayMetrics
            )

        }
    }

}