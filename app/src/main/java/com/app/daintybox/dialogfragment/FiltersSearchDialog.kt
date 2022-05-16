package com.app.daintybox.dialogfragment

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.app.daintybox.R
import com.app.daintybox.activity.DaintyBoxBaseActivity
import com.app.daintybox.customviews.CheckBoxCustom

class FiltersSearchDialog: DialogFragment() {

    var mActivity: DaintyBoxBaseActivity? = null
    private lateinit var iw_close: ImageView
    private lateinit var btn_apply: Button
    private lateinit var checkbox_beverages: CheckBoxCustom
    private lateinit var checkbox_fruits: CheckBoxCustom
    private lateinit var checkbox_meat: CheckBoxCustom
    private lateinit var checkbox_eggs: CheckBoxCustom
    private lateinit var checkbox_herbs: CheckBoxCustom
    private lateinit var checkbox_combos: CheckBoxCustom
    private lateinit var checkbox_pretelt: CheckBoxCustom
    private lateinit var checkbox_cocacola: CheckBoxCustom

    var onApplyFilters: OnApplyFilters? = null

    interface OnApplyFilters {
        fun onApplyFilters(categories: ArrayList<String>,brands: ArrayList<String>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MY_DIALOGFULL)
        mActivity = activity as DaintyBoxBaseActivity?

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.dialog_fragment_filters_search, container)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iw_close = view.findViewById(R.id.iw_close)
        btn_apply = view.findViewById(R.id.btn_apply)
        checkbox_beverages = view.findViewById(R.id.checkbox_beverages)
        checkbox_fruits = view.findViewById(R.id.checkbox_fruits)
        checkbox_meat = view.findViewById(R.id.checkbox_meat)
        checkbox_eggs = view.findViewById(R.id.checkbox_eggs)
        checkbox_herbs = view.findViewById(R.id.checkbox_herbs)
        checkbox_combos = view.findViewById(R.id.checkbox_combos)
        checkbox_pretelt = view.findViewById(R.id.checkbox_pretelt)
        checkbox_cocacola = view.findViewById(R.id.checkbox_cocacola)

        bindEvents()
    }

    fun bindEvents(){

        iw_close.setOnClickListener {
            dismissAllowingStateLoss()
        }

        btn_apply.setOnClickListener {
            val categories: ArrayList<String> = ArrayList()
            val brands: ArrayList<String> = ArrayList()


            if(checkbox_beverages.isChecked){
                categories.add("beverages")
            }

            if(checkbox_fruits.isChecked){
                categories.add("fruit")
            }

            if(checkbox_meat.isChecked){
                categories.add("meat")
            }

            if(checkbox_eggs.isChecked){
                categories.add("chicken_eggs")
            }

            if(checkbox_herbs.isChecked){
                categories.add("herbs")
            }

            if(checkbox_combos.isChecked){
                categories.add("combos")
            }

            if(checkbox_pretelt.isChecked){
                brands.add("pretelt")
            }

            if(checkbox_cocacola.isChecked){
                brands.add("cocacola")
            }


            onApplyFilters?.onApplyFilters(categories,brands)
            dismissAllowingStateLoss()
        }

    }

}