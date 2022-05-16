package com.app.daintybox.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.app.daintybox.R
import com.app.daintybox.adapter.AdapterExplora
import com.app.daintybox.models.ExploreSection

class ExplorarFragment: FragmentBaseDaintyBox() {
    companion object{
        val TAG: String = "EXPLORAR_FRAGMENT"
    }

    private lateinit var grid_explore: GridView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explorar,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        grid_explore = view.findViewById(R.id.grid_explore)

        val sections: ArrayList<ExploreSection> = ArrayList()

        sections.add(ExploreSection("beverage",getString(R.string.section_beverage),"B7DFF5",R.drawable.ic_section_beverages))
        sections.add(ExploreSection("fruit",getString(R.string.section_fruits),"53B175",R.drawable.ic_section_fruits))
        sections.add(ExploreSection("meat",getString(R.string.section_meat),"F7A593",R.drawable.ic_section_meat))
        sections.add(ExploreSection("chicken_eggs",getString(R.string.section_chicken_eggs),"F7A593",R.drawable.ic_section_eggs))
        sections.add(ExploreSection("herbs",getString(R.string.section_herbs),"D3B0E0",R.drawable.ic_section_herbs))
        sections.add(ExploreSection("combos",getString(R.string.section_combos),"B7DFF5",R.drawable.ic_section_combos))

        grid_explore.post {

            val adater = AdapterExplora(mActivity,sections,grid_explore.width)
            grid_explore.adapter = adater

        }

    }
}