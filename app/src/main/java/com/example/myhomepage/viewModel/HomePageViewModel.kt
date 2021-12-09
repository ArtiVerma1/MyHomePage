package com.example.myhomepage.viewModel

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.myhomepage.Activities.HomePage
import com.example.myhomepage.R
import com.example.myhomepage.adapter.HomePageBanner
import com.example.myhomepage.databinding.MBannerSliderBinding
import com.example.myhomepage.databinding.MCategoryCircleBinding
import com.example.myhomepage.databinding.MProductSliderBinding
import com.example.myhomepage.databinding.MTopbarBinding
import com.example.myhomepage.model.CategoryCircle
import com.example.myhomepage.model.CommanModel
import com.example.myhomepage.model.ProductSlider
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
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
                        //createCategorySquare(obj.getJSONObject(names[data].toString()))
                    }
                    "collection-grid-layout_" -> {
                       // createCollectionGrid(obj.getJSONObject(names[data].toString()))
                    }
                    "standalone-banner_" -> {
                      //  createStandAloneBanner(obj.getJSONObject(names[data].toString()))
                    }
                    "three-product-hv-layout_" -> {
                       // createHvLayout(obj.getJSONObject(names[data].toString()))
                    }
                    "fixed-customisable-layout_" -> {
                        //createFixedCustomisableLayout(obj.getJSONObject(names[data].toString()))
                    }
                    "collection-list-slider_" -> {
                        //createCollectionListSlider(obj.getJSONObject(names[data].toString()))
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
        //  MagePrefs.setHomepageData(apiResponse)
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
             context.toolbar.visibility = View.VISIBLE
             binding.homeToolbar.visibility = View.GONE
             context.setToggle(context.toolbar)
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

