package com.example.stepgame

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import javax.xml.datatype.DatatypeFactory.newInstance
import javax.xml.validation.SchemaFactory.newInstance

class PagerAdapter (fm :FragmentManager, var name: String) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position)
        {
            0->Home.newInstance(name)
            1->missions()
            2->Achievements()
            else->{
                return settings()
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0->""
            1->""
            2->""
            else->{
                return ""
            }
        }
    }
}