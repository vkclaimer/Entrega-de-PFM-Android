package com.app.daintybox.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.daintybox.R
import com.app.daintybox.activity.SectionResultActivity
import com.app.daintybox.libs.UtilsDainty
import com.app.daintybox.models.ExploreSection

class AdapterExplora(context: Context?, data: ArrayList<ExploreSection>?, width: Int?): BaseAdapter() {

    var data: ArrayList<ExploreSection>? = null
    private var inflater: LayoutInflater? = null
    var context: Context? = null

    var COL_NUMBER = 2
    var ROW_SEPARATION = 15 //dpi
    var width: Int?


    init {
        this.context = context
        inflater = LayoutInflater.from(context)
        this.data = data
        this.width = width
    }

    override fun getCount(): Int {
        return data!!.size
    }

    override fun getItem(position: Int): Any {
        return data!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var row = convertView
        var holder: ViewHolder? = null
        val section = data!![position]

        if (row == null) {
            val inflater = (context as Activity).layoutInflater
            row = inflater.inflate(R.layout.layout_explore_section, parent, false)
            holder = ViewHolder(row)
            row!!.tag = holder
        } else {
            holder = row.tag as ViewHolder
        }


        //val height_i: Float = width!! / COL_NUMBER - UtilsDainty.convertDpToPixel(ROW_SEPARATION,context!!)
        //val param = AbsListView.LayoutParams(height_i.toInt(), height_i.toInt())
        //holder.root_view.setLayoutParams(param)

        holder.tw_section_name.text = section.name
        holder.iw_section.setImageDrawable(ContextCompat.getDrawable(context!!,section.icon))

        val layer_drawable = ContextCompat.getDrawable(context!!,R.drawable.box_section_explore_beverages) as LayerDrawable?
        val box_drawable = layer_drawable!!.findDrawableByLayerId(R.id.item_shape_explore) as GradientDrawable

        box_drawable.setColor(Color.parseColor("#40"+section.background_color))
        box_drawable.setStroke( UtilsDainty.convertDpToPixel(1,context!!).toInt() ,Color.parseColor("#"+section.background_color))
        holder.root_view.background = box_drawable

        Log.v("click",section.id_section)
        holder.root_view.setOnClickListener {

            Log.v("click","click")
            if(UtilsDainty.isFirstClick()) {
                val i_sec = Intent(context, SectionResultActivity::class.java)
                i_sec.putExtra("id_section", section.id_section)
                i_sec.putExtra("name_section", section.name)

                context?.startActivity(i_sec)
            }

        }

        return row
    }

    class ViewHolder internal constructor(itemView: View){
        var iw_section: ImageView
        var tw_section_name: TextView
        var root_view: View

        init {
            iw_section = itemView.findViewById(R.id.iw_section)
            tw_section_name = itemView.findViewById(R.id.tw_section_name)
            root_view = itemView
        }
    }

}