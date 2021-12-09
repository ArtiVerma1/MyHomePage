package com.example.myhomepage.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.example.myhomepage.R
import com.example.myhomepage.databinding.MBannerlayoutBinding
import com.example.myhomepage.model.CommanModel
import com.example.myhomepage.model.Home
import org.json.JSONArray

class HomePageBanner (fm: FragmentManager, var context: Context, private var items: JSONArray):
    PagerAdapter()
{
    private var binding: MBannerlayoutBinding? = null
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`

    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
    override fun getCount(): Int {
        return items.length()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.m_bannerlayout, null, false)
        val model = CommanModel()
        Log.i("MageNative-Banner","Banner"+items.getJSONObject(position).getString("image_url")!!)
        model.imageurl = items?.getJSONObject(position)?.getString("image_url")!!
        val home = Home()
        Log.i("MageNative-Banner","id"+items.getJSONObject(position).getString("link_value")!!)
        home.id = items.getJSONObject(position).getString("link_value")
        Log.i("MageNative-Banner","link_to"+items.getJSONObject(position).getString("link_type")!!)
        home.link_to = items.getJSONObject(position).getString("link_type")
        binding!!.common = model
        binding!!.home = home
        container.addView(binding!!.root)
        return binding!!.root
    }
}
