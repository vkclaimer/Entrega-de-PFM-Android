package com.app.daintybox.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.app.daintybox.R
import com.bumptech.glide.Glide

class AdapterPicsProduct(context: Context?, urls: ArrayList<String>?): PagerAdapter() {

    private var inflater: LayoutInflater? = null
    var context: Context? = null
    var urls: ArrayList<String>? = null

    init {
        this.context = context
        inflater = LayoutInflater.from(context)
        this.urls = urls
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return urls!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = inflater!!.inflate(R.layout.img_product_profile, container, false)
        val iw_product = view.findViewById<View>(R.id.iw_product) as ImageView

        val url_actual = urls!![position]

        if(url_actual.isNotEmpty()){

            Log.v("pics","entro")
            Glide
                .with(this.context!!)
                .load(url_actual)
                .into(iw_product)

        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}