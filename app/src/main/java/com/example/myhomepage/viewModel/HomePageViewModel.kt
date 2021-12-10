package com.example.myhomepage.viewModel

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.myhomepage.Activities.HomePage
import com.example.myhomepage.MagePrefs
import com.example.myhomepage.R
import com.example.myhomepage.adapter.HomePageBanner
import com.example.myhomepage.databinding.*
import com.example.myhomepage.model.CategoryCircle
import com.example.myhomepage.model.CommanModel
import com.example.myhomepage.model.ProductSlider
import com.example.myhomepage.model.StandAloneBanner
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask
@HiltViewModel
class HomePageViewModel : ViewModel(){

var presentmentCurrency: String? = null
val message = MutableLiveData<String>()
private val disposables = CompositeDisposable()
val homepagedata = MutableLiveData<HashMap<String, View>>()
val hasBannerOnTop = MutableLiveData<Boolean>()
val hasFullSearchOnTop = MutableLiveData<Boolean>()
private val TAG = "HomePageViewModel"
//private var customLoader: CustomLoader? = null
val notifyPersonalised: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//var getyotpoauthenticate = MutableLiveData<ApiResponse>()
var notifyZendesk = MutableLiveData<Boolean>()

init {
    notifyZendesk.value = false
}
    lateinit var context: HomePage
companion object {
    var count_color: String? = null
    var count_textcolor: String? = null
    var icon_color: String? = null
    var panel_bg_color: String? = null
    var search_position: String? = null
    var search_placeholder: String? = null
}
    fun getHomePageData(): MutableLiveData<HashMap<String, View>> {
        return homepagedata
    }
    fun parseResponse(apiResponse: String, context: HomePage) {
       this.context  = context
        if (context.homepage.childCount > 0) {
            context.homepage.removeAllViews()
            context.homepage.invalidate()
        }
        try {
            var obj = JSONObject(apiResponse)
            var names: JSONArray = obj.getJSONObject("sort_order").names()!!
            for (data in 0..names.length() - 1) {
                var part = names[data].toString().split("_")
                var key: String = names[data].toString().replace(part.get(part.size - 1), "")
                Log.d("TAG", "parseResponse: " + key)
                when (key) {
                    "top-bar_", "top-bar-without-slider_" -> {
                        topbar(obj.getJSONObject(names[data].toString()))
                    }
                    "category-circle_" -> {
                       createCategoryCircle(obj.getJSONObject(names[data].toString()))
                    }
                    "banner-slider_" -> {
                        createBannerSlider(obj.getJSONObject(names[data].toString()))
                    }
                    "product-list-slider_" -> {
                        createProductSlider(obj.getJSONObject(names[data].toString()))
                    }
                    "category-square_" -> {
                        createCategorySquare(obj.getJSONObject(names[data].toString()))
                    }
                    "collection-grid-layout_" -> {
                        createCollectionGrid(obj.getJSONObject(names[data].toString()))
                    }
                    "standalone-banner_" -> {
                        createStandAloneBanner(obj.getJSONObject(names[data].toString()))
                    }
                    "three-product-hv-layout_" -> {
                       createHvLayout(obj.getJSONObject(names[data].toString()))
                    }
                    "fixed-customisable-layout_" -> {
                        createFixedCustomisableLayout(obj.getJSONObject(names[data].toString()))
                    }
                    "collection-list-slider_" -> {
                        createCollectionListSlider(obj.getJSONObject(names[data].toString()))
                    }
                }
            }

            if (names.length() == context.homepage.childCount) {
                //   Toast.makeText(context, "complete", Toast.LENGTH_SHORT).show()
//                if (customLoader != null && customLoader?.isShowing == true) {
//                    customLoader?.dismiss()
//                }
//                context.main_container.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
//            if (customLoader != null && customLoader?.isShowing == true) {
//                customLoader?.dismiss()
//            }
        }
          MagePrefs.setHomepageData(apiResponse)
    }
    private fun createStandAloneBanner(jsonObject: JSONObject) {
        var binding: MStandlonebannerBinding = DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.m_standlonebanner,
            null,
            false
        )
        var stand = StandAloneBanner()
        stand.image = jsonObject.getString("banner_url")
        Handler(Looper.getMainLooper()).post {
            when (jsonObject.getString("item_button_position")) {
                "no-btn" -> {
                    binding.sectionOne.visibility = View.GONE
                    binding.sectionTwo.visibility = View.GONE
                    stand.bannerlink = jsonObject.getString("banner_link_value")
                    stand.bannertype = jsonObject.getString("banner_link_type")
                }
                "bottom" -> {
                    var buttononeparams =
                        (binding.sectionOne.layoutParams as ConstraintLayout.LayoutParams)
                    var buttontwoparams =
                        (binding.sectionTwo.layoutParams as ConstraintLayout.LayoutParams)
                    buttononeparams.topToTop = ConstraintSet.GONE
                    buttontwoparams.topToTop = ConstraintSet.GONE
                }
            }
            if (binding.sectionOne.visibility == View.VISIBLE) {
                binding.image.setOnClickListener(null)
                when (jsonObject.getString("item_text_alignment")) {
                    "left" -> {
                        binding.buttonOne.gravity = Gravity.START
                        binding.buttonTwo.gravity = Gravity.START
                    }
                    "right" -> {
                        binding.buttonOne.gravity = Gravity.END
                        binding.buttonTwo.gravity = Gravity.END
                    }
                }
                if (jsonObject.has("first_button_text")) {
                    stand.text_one = jsonObject.getString("first_button_text")
                    stand.text_two = jsonObject.getString("second_button_text")
                }
                var background = JSONObject(jsonObject.getString("button_background_color"))
                var button_border_color = JSONObject(jsonObject.getString("button_border_color"))
                var button_text_color = JSONObject(jsonObject.getString("button_text_color"))
                binding.buttonOne.setBackgroundColor(Color.parseColor(background.getString("color")))
                binding.buttonTwo.setBackgroundColor(Color.parseColor(background.getString("color")))
                binding.borderOne.setBackgroundColor(
                    Color.parseColor(
                        button_border_color.getString(
                            "color"
                        )
                    )
                )
                binding.borderTwo.setBackgroundColor(
                    Color.parseColor(
                        button_border_color.getString(
                            "color"
                        )
                    )
                )
                binding.buttonOne.setTextColor(Color.parseColor(button_text_color.getString("color")))
                binding.buttonTwo.setTextColor(Color.parseColor(button_text_color.getString("color")))
                if (jsonObject.getString("item_font_weight").equals("bold")) {
                    val face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                    binding.buttonOne.typeface = face
                    binding.buttonTwo.typeface = face
                }
                if (jsonObject.getString("item_font_style").equals("italic")) {
                    binding.buttonOne.setTypeface(binding.buttonOne.typeface, Typeface.ITALIC)
                    binding.buttonTwo.setTypeface(binding.buttonTwo.typeface, Typeface.ITALIC)
                }
                if (jsonObject.has("banner_link_value")) {
                    stand.bannerlink = jsonObject.getString("banner_link_value")
                }
                stand.buttononetype = jsonObject.getString("first_button_link_type")
                stand.buttononelink = jsonObject.getString("first_button_link_value")
                stand.buttontwotype = jsonObject.getString("second_button_link_type")
                stand.buttontwolink = jsonObject.getString("second_button_link_value")
            }
        }
        binding.stand = stand
        homepagedata.value = hashMapOf("standalone-banner_" to binding.root)
    }

    private fun createCollectionGrid(jsonObject: JSONObject) {
        val binding: MCollectionlgridBinding = DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.m_collectionlgrid,
            null,
            false
        )
      //  context.setLayout(binding.categorylist, "3grid")
//        try {
//            repository.getJSonArray(JsonParser().parse(jsonObject.getString("items")).asJsonArray)
//                .subscribeOn(Schedulers.io())
//                .filter { x -> x.asJsonObject.get("link_type").asString.isNotEmpty() }
//                .toList()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : SingleObserver<List<JsonElement>> {
//                    override fun onSubscribe(d: Disposable) {
//
//                    }
//
//                    override fun onSuccess(list: List<JsonElement>) {
//                        adapter = CollectionGridAdapter()
//                        adapter.setData(list, context, jsonObject)
//                        binding.categorylist.adapter = adapter
//                    }
//
//                    override fun onError(e: Throwable) {
//                        e.printStackTrace()
//                    }
//                })
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        var background = JSONObject(jsonObject.getString("panel_background_color"))
        binding.categorylist.setBackgroundColor(Color.parseColor(background.getString("color")))
        homepagedata.value = hashMapOf("collection-grid-layout_" to binding.root)
    }

    private fun createFixedCustomisableLayout(jsonObject: JSONObject) {
        try {
            var binding: MFixedcustomisableBinding = DataBindingUtil.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                R.layout.m_fixedcustomisable,
                null,
                false
            )
            var productSlider = ProductSlider()
            updateDataInRecylerView(
                binding.productdata,
                jsonObject.getJSONArray("items").getJSONObject(0).getJSONArray("product_value"),
                " ",
                jsonObject
            )
            // updateDataInRecylerView(binding.productdata, jsonObject.getJSONArray("items").getJSONObject(0).getJSONArray("product_value"), "152927698980", jsonObject)
            var header_background_color =
                JSONObject(jsonObject.getString("header_background_color"))
            var panel_background_color = JSONObject(jsonObject.getString("panel_background_color"))
            binding.panelbackgroundcolor.setBackgroundColor(
                Color.parseColor(
                    panel_background_color.getString(
                        "color"
                    )
                )
            )
            binding.headerback.setBackgroundColor(
                Color.parseColor(
                    header_background_color.getString(
                        "color"
                    )
                )
            )
            if (jsonObject.getString("header").equals("1")) {
                productSlider.headertextvisibility = View.VISIBLE
                productSlider.headertext = jsonObject.getString("header_title_text")
                var header_title_color = JSONObject(jsonObject.getString("header_title_color"))
                binding.headertext.setTextColor(Color.parseColor(header_title_color.getString("color")))
                binding.headertext.textSize = 20f
                val face: Typeface
                when (jsonObject.getString("header_title_font_weight")) {
                    "bold" -> {
                        face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                    }
                    else -> {
                        face = Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                    }
                }
                binding.headertext.typeface = face
                if (jsonObject.getString("item_header_font_style").equals("italic")) {
                    binding.headertext.setTypeface(
                        binding.headertext.typeface,
                        Typeface.ITALIC
                    )
                }
                if (jsonObject.getString("header_subtitle").equals("1")) {
                    productSlider.subheadertextvisibity = View.VISIBLE
                    productSlider.subheadertext = jsonObject.getString("header_subtitle_text")
                    var header_subtitle_color =
                        JSONObject(jsonObject.getString("header_subtitle_color"))
                    binding.subheadertext.setTextColor(
                        Color.parseColor(
                            header_subtitle_color.getString(
                                "color"
                            )
                        )
                    )
                    val face: Typeface
                    when (jsonObject.getString("header_subtitle_font_weight")) {
                        "bold" -> {
                            face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                        }
                        else -> {
                            face =
                                Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                        }
                    }
                    binding.subheadertext.typeface = face
                    if (jsonObject.getString("header_subtitle_title_font_style").equals("italic")) {
                        binding.subheadertext.setTypeface(
                            binding.subheadertext.typeface,
                            Typeface.ITALIC
                        )
                    }
                } else {
                    productSlider.subheadertextvisibity = View.GONE
                }
                if (jsonObject.getString("header_deal").equals("1")) {
                    productSlider.timericon = View.VISIBLE
                    var header_deal_color = JSONObject(jsonObject.getString("header_deal_color"))
                    binding.timer.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                    binding.timerMessage.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                    binding.timericon.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                    productSlider.timertextmessage = jsonObject.getString("item_deal_message")
                    var DATE_FORMAT = "MM/dd/yyyy HH:mm:ss"
                    var sdf = SimpleDateFormat(DATE_FORMAT)
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    var item_deal_start_date = sdf.format(Date())
                    Log.i("MageNative", "item_deal_start_date " + item_deal_start_date)
                    var item_deal_end_date = jsonObject.getString("item_deal_end_date")
                    Log.i("MageNative", "item_deal_end_date " + item_deal_end_date)
                    var startdate: Date?
                    var enddate: Date?
                    try {
                        startdate = sdf.parse(item_deal_start_date)
                        enddate = sdf.parse(item_deal_end_date)
                        var oldLong = startdate.time
                        var NewLong = enddate.time
                        var diff = NewLong - oldLong
                        Log.i("MageNative", "Long" + diff)
                        if (diff > 0) {
                            // var counter = MyCount(diff, 1000, productSlider, jsonObject.getString("item_deal_format"))
                            var counter = MyCount(diff, 1000, productSlider, ":")
                            counter.start()
                        } else {
                            productSlider.timericon = View.GONE
                        }
                    } catch (ex: ParseException) {
                        ex.printStackTrace()
                    }
                } else {
                    productSlider.timericon = View.GONE
                }
                if (jsonObject.getString("header_action").equals("1")) {
                    productSlider.actiontextvisibity = View.VISIBLE
                    productSlider.action_id = getcategoryID(
                        jsonObject.getJSONArray("items").getJSONObject(0).getString("link_value")
                    )
                    productSlider.actiontext = jsonObject.getString("header_action_text")
                    var header_action_color =
                        JSONObject(jsonObject.getString("header_action_color"))
                    var header_action_background_color =
                        JSONObject(jsonObject.getString("header_action_background_color"))
                    binding.actiontext.setBackgroundColor(
                        Color.parseColor(
                            header_action_background_color.getString("color")
                        )
                    )
                    binding.actiontext.setTextColor(Color.parseColor(header_action_color.getString("color")))
                    val face: Typeface
                    when (jsonObject.getString("header_action_font_weight")) {
                        "bold" -> {
                            face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                        }
                        else -> {
                            face =
                                Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                        }
                    }
                    binding.actiontext.typeface = face
                    if (jsonObject.getString("header_action_title_font_style").equals("italic")) {
                        binding.actiontext.setTypeface(
                            binding.actiontext.typeface,
                            Typeface.ITALIC
                        )
                    }

                } else {
                    productSlider.actiontextvisibity = View.GONE
                }
            } else {
                productSlider.headertextvisibility = View.GONE
            }
            binding.productslider = productSlider
            homepagedata.value = hashMapOf("fixed-customisable-layout_" to binding.root)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun createCollectionListSlider(jsonObject: JSONObject) {
        try {
            var binding: MCollectionsliderBinding = DataBindingUtil.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                R.layout.m_collectionslider,
                null,
                false
            )
            var productSlider = ProductSlider()
            var header_background_color =
                JSONObject(jsonObject.getString("header_background_color"))
            var panel_background_color = JSONObject(jsonObject.getString("panel_background_color"))
            var header_title_color = JSONObject(jsonObject.getString("header_title_color"))
            var header_subtitle_color = JSONObject(jsonObject.getString("header_subtitle_color"))
            binding.panelbackgroundcolor.setBackgroundColor(
                Color.parseColor(
                    panel_background_color.getString(
                        "color"
                    )
                )
            )
            binding.headerback.setBackgroundColor(
                Color.parseColor(
                    header_background_color.getString(
                        "color"
                    )
                )
            )
            if (jsonObject.getString("header").equals("1")) {
                productSlider.headertextvisibility = View.VISIBLE
                productSlider.headertext = jsonObject.getString("header_title_text")
                binding.headertext.setTextColor(Color.parseColor(header_title_color.getString("color")))
                binding.headertext.textSize = 20f
                val face: Typeface
                when (jsonObject.getString("header_title_font_weight")) {
                    "bold" -> {
                        face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                    }
                    else -> {
                        face = Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                    }
                }
                binding.headertext.typeface = face
                if (jsonObject.getString("header_title_font_style").equals("italic")) {
                    binding.headertext.setTypeface(
                        binding.headertext.typeface,
                        Typeface.ITALIC
                    )
                }
                if (jsonObject.getString("header_subtitle").equals("1")) {
                    productSlider.subheadertextvisibity = View.VISIBLE
                    productSlider.subheadertext = jsonObject.getString("header_subtitle_text")
                    binding.subheadertext.setTextColor(
                        Color.parseColor(
                            header_subtitle_color.getString(
                                "color"
                            )
                        )
                    )
                    val face: Typeface
                    when (jsonObject.getString("header_subtitle_font_weight")) {
                        "bold" -> {
                            face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                        }
                        else -> {
                            face =
                                Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                        }
                    }
                    binding.subheadertext.typeface = face
                    if (jsonObject.getString("header_subtitle_font_style").equals("italic")) {
                        binding.subheadertext.setTypeface(
                            binding.subheadertext.typeface,
                            Typeface.ITALIC
                        )
                    }
                } else {
                    productSlider.subheadertextvisibity = View.GONE
                }
            } else {
                productSlider.headertextvisibility = View.GONE
            }
//            context.setLayout(binding.productdata, "horizontal")
//            slideradapter = CollectionSliderAdapter()
//            slideradapter.setData(jsonObject.getJSONArray("items"), context, jsonObject)
//            binding.productdata.adapter = slideradapter
//            slideradapter.notifyDataSetChanged()
            binding.productslider = productSlider
            homepagedata.value = hashMapOf("collection-list-slider_" to binding.root)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun createHvLayout(jsonObject: JSONObject) {
        try {
            var binding: MProductHvLayoutBinding = DataBindingUtil.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                R.layout.m_product_hv_layout,
                null,
                false
            )
            var productSlider = ProductSlider()
            var header_background_color =
                JSONObject(jsonObject.getString("header_background_color"))
            var panel_background_color = JSONObject(jsonObject.getString("panel_background_color"))
            var header_title_color = JSONObject(jsonObject.getString("header_title_color"))
            var header_action_color = JSONObject(jsonObject.getString("header_action_color"))
            var header_action_background_color =
                JSONObject(jsonObject.getString("header_action_background_color"))
            var header_subtitle_color = JSONObject(jsonObject.getString("header_subtitle_color"))
            var header_deal_color = JSONObject(jsonObject.getString("header_deal_color"))
            binding.productdata.setBackgroundColor(
                Color.parseColor(
                    panel_background_color.getString(
                        "color"
                    )
                )
            )
            binding.headerback.setBackgroundColor(
                Color.parseColor(
                    header_background_color.getString(
                        "color"
                    )
                )
            )
            if (jsonObject.getString("header").equals("1")) {
                productSlider.headertextvisibility = View.VISIBLE
                binding.headertext.textSize = 20f
                productSlider.headertext = jsonObject.getString("header_title_text")
                binding.headertext.setTextColor(Color.parseColor(header_title_color.getString("color")))
                val face: Typeface
                when (jsonObject.getString("header_title_font_weight")) {
                    "bold" -> {
                        face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                    }
                    else -> {
                        face = Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                    }
                }
                binding.headertext.typeface = face
                if (jsonObject.getString("header_title_font_style").equals("italic")) {
                    binding.headertext.setTypeface(
                        binding.headertext.typeface,
                        Typeface.ITALIC
                    )
                }
                if (jsonObject.getString("header_subtitle").equals("1")) {
                    productSlider.subheadertextvisibity = View.VISIBLE
                    productSlider.subheadertext = jsonObject.getString("header_subtitle_text")
                    binding.subheadertext.setTextColor(
                        Color.parseColor(
                            header_subtitle_color.getString(
                                "color"
                            )
                        )
                    )
                    val face: Typeface
                    when (jsonObject.getString("header_subtitle_font_weight")) {
                        "bold" -> {
                            face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                        }
                        else -> {
                            face =
                                Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                        }
                    }
                    binding.subheadertext.typeface = face
                    if (jsonObject.getString("header_subtitle_font_style").equals("italic")) {
                        binding.subheadertext.setTypeface(
                            binding.subheadertext.typeface,
                            Typeface.ITALIC
                        )
                    }
                } else {
                    productSlider.subheadertextvisibity = View.GONE
                }
                if (jsonObject.getString("header_deal").equals("1")) {
                    productSlider.timericon = View.VISIBLE
                    binding.timer.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                    binding.timerMessage.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                    binding.timericon.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                    productSlider.timertextmessage = jsonObject.getString("item_deal_message")
                    var DATE_FORMAT = "MM/dd/yyyy HH:mm:ss"
                    var sdf = SimpleDateFormat(DATE_FORMAT)
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    var item_deal_start_date = sdf.format(Date())
                    Log.i("MageNative", "item_deal_start_date " + item_deal_start_date)
                    var item_deal_end_date = jsonObject.getString("item_deal_end_date")
                    Log.i("MageNative", "item_deal_end_date " + item_deal_end_date)
                    var startdate: Date?
                    var enddate: Date?
                    try {
                        startdate = sdf.parse(item_deal_start_date)
                        enddate = sdf.parse(item_deal_end_date)
                        var oldLong = startdate.time
                        var NewLong = enddate.time
                        var diff = NewLong - oldLong
                        Log.i("MageNative", "Long" + diff)
                        if (diff > 0) {
                            //var counter = MyCount(diff, 1000, productSlider, jsonObject.getString("item_deal_format"))
                            var counter = MyCount(diff, 1000, productSlider, ":")
                            counter.start()
                        } else {
                            productSlider.timericon = View.GONE
                        }
                    } catch (ex: ParseException) {
                        ex.printStackTrace()
                    }
                } else {
                    productSlider.timericon = View.GONE
                }
                if (jsonObject.getString("header_action").equals("1")) {
                    productSlider.actiontextvisibity = View.VISIBLE
                    //productSlider.action_id = getcategoryID(jsonObject.getString("item_link_action_value"))
                    productSlider.actiontext = jsonObject.getString("header_action_text")
                    binding.actiontext.setBackgroundColor(
                        Color.parseColor(
                            header_action_background_color.getString("color")
                        )
                    )
                    binding.actiontext.setTextColor(Color.parseColor(header_action_color.getString("color")))
                    val face: Typeface
                    when (jsonObject.getString("header_action_font_weight")) {
                        "bold" -> {
                            face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                        }
                        else -> {
                            face =
                                Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                        }
                    }
                    binding.actiontext.typeface = face
                    if (jsonObject.getString("header_action_font_style").equals("italic")) {
                        binding.actiontext.setTypeface(
                            binding.actiontext.typeface,
                            Typeface.ITALIC
                        )
                    }

                } else {
                    productSlider.actiontextvisibity = View.GONE
                }
            } else {
                productSlider.headertextvisibility = View.GONE
            }

            /**************first hv***************************/
            productSlider.hvimageone =
                jsonObject.getJSONArray("items").getJSONObject(0).getString("image_url")
            // productSlider.hvimageone="https://images.unsplash.com/photo-1580748141549-71748dbe0bdc?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=334&q=80"
            productSlider.hvnameone =
                jsonObject.getJSONArray("items").getJSONObject(0).getString("title")
            productSlider.hvtypeone =
                jsonObject.getJSONArray("items").getJSONObject(0).getString("link_type")
            productSlider.hvvalueone =
                jsonObject.getJSONArray("items").getJSONObject(0).getString("link_value")
            /**************second hv***************************/
            productSlider.hvimagetwo =
                jsonObject.getJSONArray("items").getJSONObject(1).getString("image_url")
            //productSlider.hvimagetwo="https://images.unsplash.com/photo-1570589107939-54ebe3183842?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=719&q=80"
            productSlider.hvnametwo =
                jsonObject.getJSONArray("items").getJSONObject(1).getString("title")
            productSlider.hvtypetwo =
                jsonObject.getJSONArray("items").getJSONObject(1).getString("link_type")
            productSlider.hvvaluetwo =
                jsonObject.getJSONArray("items").getJSONObject(1).getString("link_value")
            /**************third hv***************************/
            productSlider.hvimagethree =
                jsonObject.getJSONArray("items").getJSONObject(2).getString("image_url")
            //productSlider.hvimagethree="https://images.unsplash.com/photo-1570589107939-54ebe3183842?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=719&q=80"
            productSlider.hvnamethree =
                jsonObject.getJSONArray("items").getJSONObject(2).getString("title")
            productSlider.hvtypethree =
                jsonObject.getJSONArray("items").getJSONObject(2).getString("link_type")
            productSlider.hvvaluethree =
                jsonObject.getJSONArray("items").getJSONObject(2).getString("link_value")
            when (jsonObject.getString("item_text_alignment")) {
                "left" -> {
                    productSlider.textaligment = Gravity.START
                }
                "right" -> {
                    productSlider.textaligment = Gravity.END
                }
            }
            if (jsonObject.getString("item_shape").equals("rounded")) {

                var background = JSONObject(jsonObject.getString("item_border_color"))
                var first_hv_shape = GradientDrawable()
                first_hv_shape.shape = GradientDrawable.RECTANGLE
                first_hv_shape.cornerRadius = 15f
                first_hv_shape.setStroke(2, Color.parseColor(background.getString("color")))
                binding.firsthvsection.setPadding(2, 2, 2, 2)
                first_hv_shape.setSize(binding.firsthvsection.width, binding.firsthvsection.height)
                binding.firsthvsection.background = first_hv_shape
                binding.cardOne.radius = 15f

                var second_hv_shape = GradientDrawable()
                second_hv_shape.shape = GradientDrawable.RECTANGLE
                second_hv_shape.cornerRadius = 15f
                second_hv_shape.setStroke(2, Color.parseColor(background.getString("color")))
                binding.secondhvcard.setPadding(2, 2, 2, 2)
                binding.thirdhvcard.setPadding(2, 2, 2, 2)
                binding.secondhvcard.background = second_hv_shape
                binding.thirdhvcard.background = second_hv_shape
                binding.cardTwo.radius = 15f
                binding.cardThree.radius = 15f

            }
            if (jsonObject.getString("item_border").equals("1")) {
                var background = JSONObject(jsonObject.getString("item_border_color"))
                if (!jsonObject.getString("item_shape").equals("rounded")) {
                    var first_hv_shape = GradientDrawable()
                    first_hv_shape.shape = GradientDrawable.RECTANGLE
                    first_hv_shape.setStroke(2, Color.parseColor(background.getString("color")))
                    binding.firsthvsection.setPadding(2, 2, 2, 2)
                    first_hv_shape.setSize(
                        binding.firsthvsection.width,
                        binding.firsthvsection.height
                    )
                    binding.firsthvsection.background = first_hv_shape
                    binding.cardOne.radius = 0f

                    var second_hv_shape = GradientDrawable()
                    second_hv_shape.shape = GradientDrawable.RECTANGLE
                    second_hv_shape.setStroke(2, Color.parseColor(background.getString("color")))
                    binding.secondhvcard.setPadding(2, 2, 2, 2)
                    binding.thirdhvcard.setPadding(2, 2, 2, 2)
                    binding.secondhvcard.background = second_hv_shape
                    binding.thirdhvcard.background = second_hv_shape
                    binding.cardTwo.radius = 0f
                    binding.cardThree.radius = 0f
                }

            }
            val face: Typeface
            when (jsonObject.getString("item_title_font_weight")) {
                "bold" -> {
                    face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                }
                else -> {
                    face = Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                }
            }
            binding.hvnameone.typeface = face
            binding.hvnametwo.typeface = face
            binding.hvnamethree.typeface = face
            if (jsonObject.getString("item_title_font_style").equals("italic")) {
                binding.hvnameone.setTypeface(binding.hvnameone.typeface, Typeface.ITALIC)
                binding.hvnametwo.setTypeface(binding.hvnametwo.typeface, Typeface.ITALIC)
                binding.hvnamethree.setTypeface(binding.hvnamethree.typeface, Typeface.ITALIC)
            }
            binding.productslider = productSlider
            homepagedata.value = hashMapOf("three-product-hv-layout_" to binding.root)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun createProductSlider(jsonObject: JSONObject) {
        try {
            val binding: MProductSliderBinding = DataBindingUtil.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                R.layout.m_product_slider,
                null,
                false
            )
            var productSlider = ProductSlider()
            Log.d(TAG, "createProductSlider: " + jsonObject.getJSONArray("item_value"))
            updateDataInRecylerView(
                binding.productdata,
                jsonObject.getJSONArray("item_value"),
                " ",
                jsonObject
            )
            var header_title_color = JSONObject(jsonObject.getString("header_title_color"))
            var header_action_color = JSONObject(jsonObject.getString("header_action_color"))
            var header_action_background_color =
                JSONObject(jsonObject.getString("header_action_background_color"))
            var header_subtitle_color = JSONObject(jsonObject.getString("header_subtitle_color"))
            var header_deal_color = JSONObject(jsonObject.getString("header_deal_color"))
            var header_background_color =
                JSONObject(jsonObject.getString("header_background_color"))
            var panel_background_color = JSONObject(jsonObject.getString("panel_background_color"))
            binding.panelbackgroundcolor.setBackgroundColor(
                Color.parseColor(
                    panel_background_color.getString(
                        "color"
                    )
                )
            )
            binding.headerback.setBackgroundColor(
                Color.parseColor(
                    header_background_color.getString(
                        "color"
                    )
                )
            )
            if (jsonObject.getString("header").equals("1")) {
                productSlider.headertextvisibility = View.VISIBLE
                productSlider.headertext = jsonObject.getString("header_title_text")
                binding.headertext.setTextColor(Color.parseColor(header_title_color.getString("color")))
                val face: Typeface
                when (jsonObject.getString("item_header_font_weight")) {
                    "bold" -> {
                        face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                    }
                    else -> {
                        face = Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                    }

                }
                binding.headertext.typeface = face
                if (jsonObject.getString("item_header_font_style").equals("italic")) {
                    binding.headertext.setTypeface(
                        binding.headertext.typeface,
                        Typeface.ITALIC
                    )
                }
                if (jsonObject.getString("header_action").equals("1")) {
                    productSlider.actiontextvisibity = View.VISIBLE
                    productSlider.action_id =
                        getcategoryID(jsonObject.getString("item_link_action_value"))
                    productSlider.actiontext = jsonObject.getString("header_action_text")
                    binding.headertext.textSize = 20f

                    var action_drawable = GradientDrawable()
                    action_drawable.shape = GradientDrawable.RECTANGLE
                    action_drawable.cornerRadius = 8f
                    action_drawable.setColor(
                        Color.parseColor(
                            header_action_background_color.getString(
                                "color"
                            )
                        )
                    )
                    binding.actiontext.background = action_drawable
                    // binding.actiontext.setBackgroundColor(Color.parseColor(header_action_background_color.getString("color")))
                    binding.actiontext.setTextColor(Color.parseColor(header_action_color.getString("color")))
                    val face: Typeface
                    when (jsonObject.getString("header_action_font_weight")) {
                        "bold" -> {
                            face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                        }
                        else -> {
                            face =
                                Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                        }
                    }
                    binding.actiontext.typeface = face
                    if (jsonObject.getString("header_action_font_style").equals("italic")) {
                        binding.actiontext.setTypeface(
                            binding.actiontext.typeface,
                            Typeface.ITALIC
                        )
                    }

                } else {
                    productSlider.actiontextvisibity = View.GONE
                }
                if (jsonObject.getString("header_subtitle").equals("1")) {
                    productSlider.subheadertextvisibity = View.VISIBLE
                    productSlider.subheadertext = jsonObject.getString("header_subtitle_text")
                    binding.subheadertext.setTextColor(
                        Color.parseColor(
                            header_subtitle_color.getString(
                                "color"
                            )
                        )
                    )
                    val face: Typeface
                    when (jsonObject.getString("header_subtitle_font_weight")) {
                        "bold" -> {
                            face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                        }
                        else -> {
                            face =
                                Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                        }

                    }
                    binding.subheadertext.typeface = face
                    if (jsonObject.getString("header_subtitle_title_font_style").equals("italic")) {
                        binding.subheadertext.setTypeface(
                            binding.subheadertext.typeface,
                            Typeface.ITALIC
                        )
                    }
                } else {
                    productSlider.subheadertextvisibity = View.GONE
                }
                if (jsonObject.getString("header_deal").equals("1")) {
                    productSlider.timericon = View.VISIBLE
                  //  binding.timer.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                    binding.timerMessage.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                  //  binding.timericon.setTextColor(Color.parseColor(header_deal_color.getString("color")))
                    productSlider.timertextmessage = jsonObject.getString("item_deal_message")

//                    if (jsonObject.getString("item_deal_message").equals("{deal-time}")) {
//                        binding.timerMessage.text = ""
//                    } else if (jsonObject.getString("item_deal_message")?.contains("{")!!) {
//                        binding.timerMessage.text = jsonObject.getString("item_deal_message").split("{").get(1).split("}").get(1)
//                    } else {
//                        binding.timerMessage.text = jsonObject.getString("item_deal_message")
//                    }
                    var DATE_FORMAT = "MM/dd/yyyy HH:mm:ss"
                    var sdf = SimpleDateFormat(DATE_FORMAT)
                    sdf.timeZone = TimeZone.getTimeZone("UTC")
                    var item_deal_start_date = sdf.format(Date())
                    Log.i("MageNative", "item_deal_start_date " + item_deal_start_date)
                    var item_deal_end_date = jsonObject.getString("item_deal_end_date")
                    Log.i("MageNative", "item_deal_end_date " + item_deal_end_date)
                    var startdate: Date?
                    var enddate: Date?
                    try {
                        startdate = sdf.parse(item_deal_start_date)
                        enddate = sdf.parse(item_deal_end_date)
                        var oldLong = startdate.time
                        var NewLong = enddate.time
                        var diff = NewLong - oldLong
                        Log.i("MageNative", "Long" + diff)
                        if (diff > 0) {
                            //var counter = MyCount(diff, 1000, productSlider, jsonObject.getString("item_deal_format"))
                            var counter = MyCount(diff, 1000, productSlider, ":")
                            counter.start()
                        } else {
                            productSlider.timericon = View.GONE
                        }
                    } catch (ex: ParseException) {
                        ex.printStackTrace()
                    }
                } else {
                    productSlider.timericon = View.GONE
                }
            } else {
                productSlider.headertextvisibility = View.GONE
            }
            binding.productslider = productSlider
            homepagedata.value = hashMapOf("product-list-slider_" to binding.root)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    private fun createCategorySquare(jsonObject: JSONObject) {
        var binding: MCategorySquareBinding = DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.m_category_square,
            null,
            false
        )
        var category = CategoryCircle()
        binding.category = category
        when (jsonObject.getString("item_shape")) {
            "square" -> {
                binding.cardOne.cardElevation = 0f
                binding.cardOne.radius = 0f
                binding.cardTwo.cardElevation = 0f
                binding.cardTwo.radius = 0f
                binding.cardThree.cardElevation = 0f
                binding.cardThree.radius = 0f
                binding.cardFour.cardElevation = 0f
                binding.cardFour.radius = 0f
                binding.cardFive.cardElevation = 0f
                binding.cardFive.radius = 0f
            }
        }
        var background = JSONObject(jsonObject.getString("panel_background_color"))
        binding.root.setBackgroundColor(Color.parseColor(background.getString("color")))
        if (jsonObject.getString("item_title").equals("1")) {
            category.cat_text_one =
                jsonObject.getJSONArray("items").getJSONObject(0).getString("title")
            category.cat_text_two =
                jsonObject.getJSONArray("items").getJSONObject(1).getString("title")
            category.cat_text_three =
                jsonObject.getJSONArray("items").getJSONObject(2).getString("title")
            category.cat_text_four =
                jsonObject.getJSONArray("items").getJSONObject(3).getString("title")
            category.cat_text_five =
                jsonObject.getJSONArray("items").getJSONObject(4).getString("title")
            var item_color = JSONObject(jsonObject.getString("item_title_color"))
            binding.catTextOne.setTextColor(Color.parseColor(item_color.getString("color")))
            binding.catTextTwo.setTextColor(Color.parseColor(item_color.getString("color")))
            binding.catTextThree.setTextColor(Color.parseColor(item_color.getString("color")))
            binding.catTextFour.setTextColor(Color.parseColor(item_color.getString("color")))
            binding.catTextFive.setTextColor(Color.parseColor(item_color.getString("color")))
            if (jsonObject.getString("item_font_weight").equals("bold")) {
                val face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
                binding.catTextOne.typeface = face
                binding.catTextTwo.typeface = face
                binding.catTextThree.typeface = face
                binding.catTextFour.typeface = face
                binding.catTextFive.typeface = face
            } else {
                val face = Typeface.createFromAsset(context.assets, "fonts/cairoregular.ttf")
                binding.catTextOne.typeface = face
                binding.catTextTwo.typeface = face
                binding.catTextThree.typeface = face
                binding.catTextFour.typeface = face
                binding.catTextFive.typeface = face
            }
            if (jsonObject.getString("item_font_style").equals("italic")) {
                binding.catTextOne.setTypeface(binding.catTextOne.typeface, Typeface.ITALIC)
                binding.catTextTwo.setTypeface(binding.catTextTwo.typeface, Typeface.ITALIC)
                binding.catTextThree.setTypeface(
                    binding.catTextThree.typeface,
                    Typeface.ITALIC
                )
                binding.catTextFour.setTypeface(binding.catTextFour.typeface, Typeface.ITALIC)
                binding.catTextFive.setTypeface(binding.catTextFive.typeface, Typeface.ITALIC)
            }
        }
        if (jsonObject.getString("item_border").equals("1")) {
            // Handler(Looper.getMainLooper()).post {
            var background = JSONObject(jsonObject.getString("item_border_color"))
            binding.cardOne.setCardBackgroundColor(Color.parseColor(background.getString("color")))
            binding.cardTwo.setCardBackgroundColor(Color.parseColor(background.getString("color")))
            binding.cardThree.setCardBackgroundColor(Color.parseColor(background.getString("color")))
            binding.cardFour.setCardBackgroundColor(Color.parseColor(background.getString("color")))
            binding.cardFive.setCardBackgroundColor(Color.parseColor(background.getString("color")))
            binding.cardOne.setContentPadding(2, 2, 2, 2)
            binding.cardTwo.setContentPadding(2, 2, 2, 2)
            binding.cardThree.setContentPadding(2, 2, 2, 2)
            binding.cardFour.setContentPadding(2, 2, 2, 2)
            binding.cardFive.setContentPadding(2, 2, 2, 2)
            // }
        }
        category.cat_image_one =
            jsonObject.getJSONArray("items").getJSONObject(0).getString("image_url")
        category.cat_value_one =
            jsonObject.getJSONArray("items").getJSONObject(0).getString("link_value")
        category.cat_link_one =
            jsonObject.getJSONArray("items").getJSONObject(0).getString("link_type")
        category.cat_image_two =
            jsonObject.getJSONArray("items").getJSONObject(1).getString("image_url")
        category.cat_value_two =
            jsonObject.getJSONArray("items").getJSONObject(1).getString("link_value")
        category.cat_link_two =
            jsonObject.getJSONArray("items").getJSONObject(1).getString("link_type")
        category.cat_image_three =
            jsonObject.getJSONArray("items").getJSONObject(2).getString("image_url")
        category.cat_value_three =
            jsonObject.getJSONArray("items").getJSONObject(2).getString("link_value")
        category.cat_link_three =
            jsonObject.getJSONArray("items").getJSONObject(2).getString("link_type")
        category.cat_image_four =
            jsonObject.getJSONArray("items").getJSONObject(3).getString("image_url")
        category.cat_value_four =
            jsonObject.getJSONArray("items").getJSONObject(3).getString("link_value")
        category.cat_link_four =
            jsonObject.getJSONArray("items").getJSONObject(3).getString("link_type")
        category.cat_image_five =
            jsonObject.getJSONArray("items").getJSONObject(4).getString("image_url")
        if (jsonObject.getJSONArray("items").getJSONObject(4).has("link_value")) {
            category.cat_value_five =
                jsonObject.getJSONArray("items").getJSONObject(4).getString("link_value")
        } else {
            category.cat_value_five = "list_collection"
        }
        category.cat_link_five =
            jsonObject.getJSONArray("items").getJSONObject(4).getString("link_type")
        homepagedata.value = hashMapOf("category-square_" to binding.root)
    }

    private fun updateDataInRecylerView(
        productdata: RecyclerView?,
        jsonArray: JSONArray,
        cat_id: String,
        jsonObject: JSONObject
    ) {
        runBlocking(Dispatchers.IO) {
            try {
//                val edges = mutableListOf<Storefront.Product>()
//                val product_ids = ArrayList<ID>()
                for (i in 0..jsonArray.length() - 1) {
//                    Log.d(
//                        TAG,
//                        "updateDataInRecylerView: " + ID(getProductID(jsonArray.getString(i)))
//                    )
                   // product_ids.add(ID(getProductID(jsonArray.getString(i))))
                }

//                val currency_list = ArrayList<Storefront.CurrencyCode>()
//                if (presentmentCurrency != "nopresentmentcurrency") {
//                    currency_list.add(Storefront.CurrencyCode.valueOf(presentmentCurrency!!))
//                }
//                getProductsById(
//                    product_ids,
//                    productdata,
//                    jsonArray,
//                    jsonObject,
//                    edges,
//                    currency_list
//                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }
//    suspend fun getProductsById(
//        id: ArrayList<ID>,
//        productdata: RecyclerView?,
//        jsonArray: JSONArray,
//        jsonObject: JSONObject,
//        edges: MutableList<Storefront.Product>,
//        currency_list: ArrayList<Storefront.CurrencyCode>
//    ) {
//        try {
//            doGraphQLQueryGraph(
//                repository,
//                Query.getAllProductsByID(id, currency_list),
//                customResponse = object : CustomResponse {
//                    override fun onSuccessQuery(result: GraphCallResult<Storefront.QueryRoot>) {
//                        if (result is GraphCallResult.Success<*>) {
//                            consumeResponse(
//                                GraphQLResponse.success(result as GraphCallResult.Success<*>),
//                                productdata,
//                                jsonArray,
//                                jsonObject,
//                                edges
//                            )
//                        } else {
//                            consumeResponse(
//                                GraphQLResponse.error(result as GraphCallResult.Failure),
//                                productdata,
//                                jsonArray,
//                                jsonObject,
//                                edges
//                            )
//                        }
//                    }
//                },
//                context = context
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private fun getProductID(id: String?): String? {
        var cat_id: String? = null
        try {
            val data =
                Base64.encode(("gid://shopify/Product/" + id!!).toByteArray(), Base64.DEFAULT)
            cat_id = String(data, Charset.defaultCharset()).trim { it <= ' ' }
            Log.i("MageNatyive", "ProductSliderID :$id " + cat_id)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cat_id
    }

    private fun getcategoryID(id: String?): String? {
        var cat_id: String? = null
        try {
            val data = Base64.encode("gid://shopify/Collection/$id".toByteArray(), Base64.DEFAULT)
            cat_id = String(data, Charset.defaultCharset()).trim { it <= ' ' }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return cat_id
    }

    class MyCount : CountDownTimer {
        var productSlider: ProductSlider
        var format: String

        constructor(
            millisInFuture: Long,
            countDownInterval: Long,
            productSlider: ProductSlider,
            format: String
        ) : super(millisInFuture, countDownInterval) {
            this.productSlider = productSlider
            this.format = format
        }

        override fun onFinish() {
            productSlider.timericon = View.GONE
        }

        override fun onTick(millisUntilFinished: Long) {
            var millis = millisUntilFinished
            var hms =
                " " + (TimeUnit.MILLISECONDS.toDays(millis)) + " Day $format " + (TimeUnit.MILLISECONDS.toHours(
                    millis
                ) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis))) + " H $format " + (TimeUnit.MILLISECONDS.toMinutes(
                    millis
                ) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))) + " M $format " + (TimeUnit.MILLISECONDS.toSeconds(
                    millis
                ) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))) + " S"
            productSlider.timertext = hms
        }
    }
private fun createCategoryCircle(jsonObject: JSONObject) {
    var binding: MCategoryCircleBinding = DataBindingUtil.inflate(
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
        R.layout.m_category_circle,
        null,
        false
    )
    var category = CategoryCircle()
    binding.category = category
    var background = JSONObject(jsonObject.getString("panel_background_color"))

    binding.root.setBackgroundColor(Color.parseColor(background.getString("color")))
    if (jsonObject.getString("item_title").equals("1")) {
        category.cat_text_one =
            jsonObject.getJSONArray("items").getJSONObject(0).getString("title")
        category.cat_text_two =
            jsonObject.getJSONArray("items").getJSONObject(1).getString("title")
        category.cat_text_three =
            jsonObject.getJSONArray("items").getJSONObject(2).getString("title")
        category.cat_text_four =
            jsonObject.getJSONArray("items").getJSONObject(3).getString("title")
        category.cat_text_five =
            jsonObject.getJSONArray("items").getJSONObject(4).getString("title")
        var item_color = JSONObject(jsonObject.getString("item_title_color"))
        binding.catTextOne.setTextColor(Color.parseColor(item_color.getString("color")))
        binding.catTextTwo.setTextColor(Color.parseColor(item_color.getString("color")))
        binding.catTextThree.setTextColor(Color.parseColor(item_color.getString("color")))
        binding.catTextFour.setTextColor(Color.parseColor(item_color.getString("color")))
        binding.catTextFive.setTextColor(Color.parseColor(item_color.getString("color")))
        if (jsonObject.getString("item_font_weight").equals("bold")) {
            val face = Typeface.createFromAsset(context.assets, "fonts/cairobold.ttf")
            binding.catTextOne.typeface = face
            binding.catTextTwo.typeface = face
            binding.catTextThree.typeface = face
            binding.catTextFour.typeface = face
            binding.catTextFive.typeface = face
        }
        if (jsonObject.getString("item_font_style").equals("italic")) {
            binding.catTextOne.setTypeface(binding.catTextOne.typeface, Typeface.ITALIC)
            binding.catTextTwo.setTypeface(binding.catTextTwo.typeface, Typeface.ITALIC)
            binding.catTextThree.setTypeface(
                binding.catTextThree.typeface,
                Typeface.ITALIC
            )
            binding.catTextFour.setTypeface(binding.catTextFour.typeface, Typeface.ITALIC)
            binding.catTextFive.setTypeface(binding.catTextFive.typeface, Typeface.ITALIC)
        }
    }
    if (jsonObject.getString("item_border").equals("1")) {
        var item_border_color = JSONObject(jsonObject.getString("item_border_color"))
        binding.imageOne.tag = item_border_color.getString("color")
        binding.imageTwo.tag = item_border_color.getString("color")
        binding.imageThree.tag = item_border_color.getString("color")
        binding.imageFour.tag = item_border_color.getString("color")
        binding.imageFive.tag = item_border_color.getString("color")
    } else {
        binding.imageOne.tag = background.getString("color")
        binding.imageTwo.tag = background.getString("color")
        binding.imageThree.tag = background.getString("color")
        binding.imageFour.tag = background.getString("color")
        binding.imageFive.tag = background.getString("color")
    }
    category.cat_image_one =
        jsonObject.getJSONArray("items").getJSONObject(0).getString("image_url")
    category.cat_value_one =
        jsonObject.getJSONArray("items").getJSONObject(0).getString("link_value")
    category.cat_link_one =
        jsonObject.getJSONArray("items").getJSONObject(0).getString("link_type")
    category.cat_image_two =
        jsonObject.getJSONArray("items").getJSONObject(1).getString("image_url")
    category.cat_value_two =
        jsonObject.getJSONArray("items").getJSONObject(1).getString("link_value")
    category.cat_link_two =
        jsonObject.getJSONArray("items").getJSONObject(1).getString("link_type")
    category.cat_image_three =
        jsonObject.getJSONArray("items").getJSONObject(2).getString("image_url")
    category.cat_value_three =
        jsonObject.getJSONArray("items").getJSONObject(2).getString("link_value")
    category.cat_link_three =
        jsonObject.getJSONArray("items").getJSONObject(2).getString("link_type")
    category.cat_image_four =
        jsonObject.getJSONArray("items").getJSONObject(3).getString("image_url")
    category.cat_value_four =
        jsonObject.getJSONArray("items").getJSONObject(3).getString("link_value")
    category.cat_link_four =
        jsonObject.getJSONArray("items").getJSONObject(3).getString("link_type")
    category.cat_image_five =
        jsonObject.getJSONArray("items").getJSONObject(4).getString("image_url")
    if (jsonObject.getJSONArray("items").getJSONObject(4).has("link_value")) {
        category.cat_value_five =
            jsonObject.getJSONArray("items").getJSONObject(4).getString("link_value")
    } else {
        category.cat_value_five = "list_collection"
    }
    category.cat_link_five =
        jsonObject.getJSONArray("items").getJSONObject(4).getString("link_type")
    homepagedata.value = hashMapOf("category-circle_" to binding.root)
}

private fun createBannerSlider(jsonObject: JSONObject) {
    try {
        var binding: MBannerSliderBinding = DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.m_banner_slider,
            null,
            false
        )
        binding.banners.adapter = HomePageBanner(
            context.supportFragmentManager,
            context,
            jsonObject.getJSONArray("items")
        )
        if (jsonObject.getString("item_dots").equals("1")) {
            binding.indicator.visibility = View.VISIBLE
            var background = JSONObject(jsonObject.getString("active_dot_color"))
            var strokebackground = JSONObject(jsonObject.getString("inactive_dot_color"))
            binding.indicator.setDotIndicatorColor(Color.parseColor(background.getString("color")))
            binding.indicator.setStrokeDotsIndicatorColor(
                Color.parseColor(
                    strokebackground.getString(
                        "color"
                    )
                )
            )
            binding!!.indicator.setViewPager(binding!!.banners)

        }
        var i = 0
        val timer = Timer()
        timer.scheduleAtFixedRate(timerTask {
            GlobalScope.launch(Dispatchers.Main) {
                binding.banners.setCurrentItem(i++);
                if (i == jsonObject.getJSONArray("items").length()) {
                    i = 0
                }
            }
        }, 3000, 3000)
        homepagedata.setValue(hashMapOf("banner-slider_" to binding.root))
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}


 private fun topbar(jsonObject: JSONObject) {
     try {
         var binding: MTopbarBinding = DataBindingUtil.inflate(
             context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
             R.layout.m_topbar,
             null,
             false
         )

         binding.root.setBackgroundColor(
             Color.parseColor(
                 JSONObject(jsonObject.getString("panel_background_color")).getString(
                     "color"
                 )
             )
         )
         panel_bg_color =
             JSONObject(jsonObject.getString("panel_background_color")).getString("color")
         count_color = JSONObject(jsonObject.getString("count_color")).getString("color")
         count_textcolor = JSONObject(jsonObject.getString("count_textcolor")).getString("color")
         icon_color = JSONObject(jsonObject.getString("icon_color")).getString("color")

         if (jsonObject.has("item_banner") && jsonObject.getString("item_banner").equals("1")) {
             hasBannerOnTop.value = true
             var common = CommanModel()
             var adp = HomePageBanner(
                 context.supportFragmentManager,
                 context,
                 jsonObject.getJSONArray("items")
             )
             binding.bannerss.adapter = adp
             adp.notifyDataSetChanged()
             context.toolbar.visibility = View.GONE
             binding.homeToolbar.visibility = View.VISIBLE
             context.setToggle(binding.homeToolbar)

             GlobalScope.launch(Dispatchers.Main) {
                 delay(1000)
//                    context.setHomeIconColors(
//                        JSONObject(jsonObject.getString("count_color")).getString("color"),
//                        JSONObject(jsonObject.getString("count_textcolor")).getString("color"),
//                        JSONObject(jsonObject.getString("icon_color")).getString("color")
//                    )
//                    context.setHomeSearchOption(
//                        jsonObject.getString("search_position"),
//                        jsonObject.getString("search_placeholder"),
//                        binding
//                    )
                 context.setHomeWishList(jsonObject.getString("wishlist"))
//                    if (jsonObject.has("logo_image_url")) {
//                        context.setHomeLogoImage(jsonObject.getString("logo_image_url"), binding)
//                    }

                 context.setToggle(context.toolbar)
                 delay(1000)
                 search_position = jsonObject.getString("search_position")
                 search_placeholder = jsonObject.getString("search_placeholder")
                 context.setSearchOption(
                     jsonObject.getString("search_position"),
                     jsonObject.getString("search_placeholder")
                 )
                 context.setWishList(jsonObject.getString("wishlist"))
                 if (jsonObject.has("logo_image_url")) {
                     context.setLogoImage(jsonObject.getString("logo_image_url"))
                 }
                 context.setPanelBackgroundColor(
                     JSONObject(jsonObject.getString("panel_background_color")).getString(
                         "color"
                     )
                 )
//                    context.setIconColors(
//                        JSONObject(jsonObject.getString("count_color")).getString("color"),
//                        JSONObject(jsonObject.getString("count_textcolor")).getString("color"),
//                        JSONObject(jsonObject.getString("icon_color")).getString("color")
//                    )
             }

             when (jsonObject.getString("search_position")) {
                 "middle-width-search" -> {
                     GlobalScope.launch(Dispatchers.Main) {
                         delay(1000)
//                            context.setHomeSearchOptions(
//                                JSONObject(jsonObject.getString("search_background_color")).getString(
//                                    "color"
//                                ),
//                                JSONObject(jsonObject.getString("search_text_color")).getString("color"),
//                                JSONObject(jsonObject.getString("search_border_color")).getString("color"),
//                                binding
//                            )
                         context.setSearchOptions(
                             JSONObject(jsonObject.getString("search_background_color")).getString(
                                 "color"
                             ),
                             JSONObject(jsonObject.getString("search_text_color")).getString("color"),
                             JSONObject(jsonObject.getString("search_border_color")).getString("color")
                         )

                     }
                 }
                 "full-width-search" -> {
                     hasFullSearchOnTop.value = true
                     binding.fullsearch.visibility = View.VISIBLE
                     binding.fullsearch.text = jsonObject.getString("search_placeholder")
                     binding.fullsearch.setOnClickListener {
//                            val searchpage = Intent(context, AutoSearch::class.java)
//                            context.startActivity(searchpage)
//                            Constant.activityTransition(context)
                     }
                     var draw: GradientDrawable =
                         binding.fullsearch.background as GradientDrawable
                     draw.setColor(
                         Color.parseColor(
                             JSONObject(jsonObject.getString("search_background_color")).getString(
                                 "color"
                             )
                         )
                     )
                     binding.fullsearch.setTextColor(
                         Color.parseColor(
                             JSONObject(
                                 jsonObject.getString(
                                     "search_text_color"
                                 )
                             ).getString("color")
                         )
                     )
                     binding.fullsearch.setHintTextColor(
                         Color.parseColor(
                             JSONObject(
                                 jsonObject.getString(
                                     "search_text_color"
                                 )
                             ).getString("color")
                         )
                     )
                     draw.setStroke(
                         2,
                         Color.parseColor(
                             JSONObject(jsonObject.getString("search_border_color")).getString("color")
                         )
                     )


//                        context.fullsearch_container.setBackgroundColor(
//                            Color.parseColor(
//                                JSONObject(
//                                    jsonObject.getString("panel_background_color")
//                                ).getString("color")
//                            )
//                        )
//                        context.fullsearch.visibility = View.GONE
//                        context.fullsearch_container.visibility = View.GONE
//                        context.fullsearch.text = jsonObject.getString("search_placeholder")
//                        context.fullsearch.setOnClickListener {
////                            val searchpage = Intent(context, AutoSearch::class.java)
////                            context.startActivity(searchpage)
////                            Constant.activityTransition(context)
//                        }

                     //  var draw1: GradientDrawable =
//                            context.fullsearch.background as GradientDrawable
//                        draw1.setColor(
//                            Color.parseColor(
//                                JSONObject(jsonObject.getString("search_background_color")).getString(
//                                    "color"
//                                )
//                            )
                     //         )
//                        context.fullsearch.setTextColor(
//                            Color.parseColor(
//                                JSONObject(
//                                    jsonObject.getString(
//                                        "search_text_color"
//                                    )
//                                ).getString("color")
//                            )
//                        )
//                        context.fullsearch.setHintTextColor(
//                            Color.parseColor(
//                                JSONObject(
//                                    jsonObject.getString(
//                                        "search_text_color"
//                                    )
//                                ).getString("color")
//                            )
//                        )
//                        draw1.setStroke(
//                            2,
//                            Color.parseColor(
//                                JSONObject(jsonObject.getString("search_border_color")).getString("color")
//                            )
//                        )
                     //context.fullsearch.background = draw1
                 }
             }


             //  binding.bannersection.backgroundTintList = ColorStateList.valueOf(Color.parseColor(JSONObject(jsonObject.getString("panel_background_color")).getString("color")))
             binding.bannerss.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                 override fun onPageScrollStateChanged(state: Int) {

                 }

                 override fun onPageScrolled(
                     position: Int,
                     positionOffset: Float,
                     positionOffsetPixels: Int
                 ) {
                 }

                 override fun onPageSelected(position: Int) {
                     common.imageurl = jsonObject.getJSONArray("items").getJSONObject(position)
                         .getString("image_url")
                 }

             })
             binding.bannersection.backgroundTintList = ColorStateList.valueOf(
                 Color.parseColor(
                     JSONObject(jsonObject.getString("panel_background_color")).getString("color")
                 )
             )
             if (jsonObject.getString("shape").equals("square")) {
                 binding.card.radius = 0f
                 binding.card.cardElevation = 0f
             }
             if (jsonObject.getString("item_dots").equals("1")) {
                 var background = JSONObject(jsonObject.getString("active_dot_color"))
                 var strokebackground = JSONObject(jsonObject.getString("inactive_dot_color"))
                 binding.indicators.visibility = View.VISIBLE
                 binding.indicators.setDotIndicatorColor(
                     Color.parseColor(
                         background.getString(
                             "color"
                         )
                     )
                 )
                 binding.indicators.setStrokeDotsIndicatorColor(
                     Color.parseColor(
                         strokebackground.getString("color")
                     )
                 )
                 binding.indicators.setViewPager(binding.bannerss)

                 var i = 0
                 val timer = Timer()
                 timer.scheduleAtFixedRate(timerTask {
                     GlobalScope.launch(Dispatchers.Main) {
                         binding.bannerss.currentItem = i++
                         if (i == jsonObject.getJSONArray("items").length()) {
                             i = 0
                         }
                     }
                 }, 3000, 3000)
             }
             common.imageurl =
                 jsonObject.getJSONArray("items").getJSONObject(0).getString("image_url")
             binding.commondata = common
             binding.bannersection.visibility = View.VISIBLE
             binding.backImage.visibility = View.VISIBLE
             binding.overlay.visibility = View.VISIBLE
         } else {
             hasBannerOnTop.value = false
             binding.bannersection.visibility = View.GONE
             binding.backImage.visibility = View.GONE
             binding.overlay.visibility = View.GONE
            // context.toolbar.visibility = View.VISIBLE
            // binding.homeToolbar.visibility = View.GONE
          //   context.setToggle(context.toolbar)
             GlobalScope.launch(Dispatchers.Main) {
                 delay(1000)
                 context.setSearchOption(
                     jsonObject.getString("search_position"),
                     jsonObject.getString("search_placeholder")
                 )
                 context.setWishList(jsonObject.getString("wishlist"))
                 if (jsonObject.has("logo_image_url")) {
                     context.setLogoImage(jsonObject.getString("logo_image_url"))
                 }
                 context.setPanelBackgroundColor(
                     JSONObject(jsonObject.getString("panel_background_color")).getString(
                         "color"
                     )
                 )
//                    context.setIconColors(
//                        JSONObject(jsonObject.getString("count_color")).getString("color"),
//                        JSONObject(jsonObject.getString("count_textcolor")).getString("color"),
//                        JSONObject(jsonObject.getString("icon_color")).getString("color")
//                    )
             }
             when (jsonObject.getString("search_position")) {
                 "middle-width-search" -> {
                     if (jsonObject.has("item_banner") && !jsonObject.getString("item_banner")
                             .equals("1")
                     ) {
                         GlobalScope.launch(Dispatchers.Main) {
                             context.setSearchOptions(
                                 JSONObject(jsonObject.getString("search_background_color")).getString(
                                     "color"
                                 ),
                                 JSONObject(jsonObject.getString("search_text_color")).getString(
                                     "color"
                                 ),
                                 JSONObject(jsonObject.getString("search_border_color")).getString(
                                     "color"
                                 )
                             )
                         }
                     }
                 }
                 "full-width-search" -> {
                     hasFullSearchOnTop.value = true
                     binding.fullsearch.visibility = View.VISIBLE
                     binding.fullsearch.text = jsonObject.getString("search_placeholder")
                     binding.fullsearch.setOnClickListener {
//                            val searchpage = Intent(context, AutoSearch::class.java)
//                            context.startActivity(searchpage)
//                            Constant.activityTransition(context)
                     }
                     var draw: GradientDrawable =
                         binding.fullsearch.background as GradientDrawable
                     draw.setColor(
                         Color.parseColor(
                             JSONObject(jsonObject.getString("search_background_color")).getString(
                                 "color"
                             )
                         )
                     )
                     binding.fullsearch.setTextColor(
                         Color.parseColor(
                             JSONObject(
                                 jsonObject.getString(
                                     "search_text_color"
                                 )
                             ).getString("color")
                         )
                     )
                     binding.fullsearch.setHintTextColor(
                         Color.parseColor(
                             JSONObject(
                                 jsonObject.getString(
                                     "search_text_color"
                                 )
                             ).getString("color")
                         )
                     )
                     draw.setStroke(
                         2,
                         Color.parseColor(
                             JSONObject(jsonObject.getString("search_border_color")).getString("color")
                         )
                     )

//                        context.fullsearch_container.setBackgroundColor(
//                            Color.parseColor(
//                                JSONObject(
//                                    jsonObject.getString("panel_background_color")
//                                ).getString("color")
//                            )
//                        )
//                        context.fullsearch.visibility = View.GONE
//                        context.fullsearch_container.visibility = View.GONE
//                        context.fullsearch.text = jsonObject.getString("search_placeholder")
//                        context.fullsearch.setOnClickListener {
////                            val searchpage = Intent(context, AutoSearch::class.java)
////                            context.startActivity(searchpage)
////                            Constant.activityTransition(context)
//                        }
//                        var draw1: GradientDrawable =
//                            context.fullsearch.background as GradientDrawable
//                        draw1.setColor(
//                            Color.parseColor(
//                                JSONObject(jsonObject.getString("search_background_color")).getString(
//                                    "color"
//                                )
//                            )
//                        )
//                        context.fullsearch.setTextColor(
//                            Color.parseColor(
//                                JSONObject(
//                                    jsonObject.getString(
//                                        "search_text_color"
//                                    )
//                                ).getString("color")
//                            )
//                        )
//                        context.fullsearch.setHintTextColor(
//                            Color.parseColor(
//                                JSONObject(
//                                    jsonObject.getString(
//                                        "search_text_color"
//                                    )
//                                ).getString("color")
//                            )
//                        )
//                        draw1.setStroke(
//                            2,
//                            Color.parseColor(
//                                JSONObject(jsonObject.getString("search_border_color")).getString("color")
//                            )
//                        )
                     //  context.fullsearch.background = draw1
                 }
             }
         }
         homepagedata.value = hashMapOf("top-bar_" to binding.root)
     } catch (ex: Exception) {
         ex.printStackTrace()
     }
 }
    }

