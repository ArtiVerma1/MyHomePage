package com.example.myhomepage.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myhomepage.R
import com.example.myhomepage.databinding.MHomepageModifiedBinding
import com.example.myhomepage.viewModel.HomePageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomePage : NewBaseActivity() {
    private var binding: MHomepageModifiedBinding? = null

    //    @Inject
//    lateinit var factory: ViewModelFactory
    //private var homemodel: HomePageViewModel? = null
    // protected lateinit var leftmenu: LeftMenuViewModel
    lateinit var homepage: LinearLayoutCompat
    // private var personamodel: PersonalisedViewModel? = null
    private var hasBanner: Boolean? = null
    private var hasFullSearch: Boolean = false
    lateinit var searchItem: MenuItem
    lateinit var wishitemHome: MenuItem
    lateinit var cartitemHome: MenuItem
    private var scrollYPosition: Int = -1
    private val MY_REQUEST_CODE = 105
    private lateinit var homeViewModel: HomePageViewModel
    val list = ArrayList<String>()
    // private var appUpdateManager: AppUpdateManager? = null

//    @Inject
//    lateinit var personalisedadapter: PersonalisedAdapter

    private val TAG = "HomePage"

    //    @Inject
//    lateinit var padapter: PersonalisedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val group = findViewById<ViewGroup>(R.id.container)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.m_homepage_modified, group, true)
        homepage = binding!!.homecontainer
        getList1()
        homeViewModel =
            ViewModelProvider(this).get(HomePageViewModel::class.java)
//        (application as MyApplication).mageNativeAppComponent!!.doHomePageInjection(this)
//        MyApplication.dataBaseReference = MyApplication.getmFirebaseSecondanyInstance()
//            .getReference(Urls(MyApplication.context).shopdomain.replace(".myshopify.com", ""))
//        leftmenu = ViewModelProvider(this, viewModelFactory).get(LeftMenuViewModel::class.java)
//        appUpdateManager = AppUpdateManagerFactory.create(this)
//        homemodel = ViewModelProvider(this, factory).get(HomePageViewModel::class.java)
//        homemodel!!.context = this
//        showHumburger()
//        personamodel = ViewModelProvider(this, factory).get(PersonalisedViewModel::class.java)
//        personamodel?.activity = this
//        homemodel!!.connectFirebaseForHomePageData(this, homepage)
//        homemodel!!.getToastMessage()
//            .observe(this@HomePage, Observer<String> { consumeResponse(it) })
      //  consumeResponse(lis)
       // homeViewModel!!.getHomePageData()
           // ?.observe(this@HomePage, Observer<HashMap<String, View>> { consumeResponse(it) })
//        homemodel!!.hasBannerOnTop.observe(this, Observer { this.ConsumeBanner(it) })
//        homemodel!!.hasFullSearchOnTop.observe(this, Observer { this.consumeFullSearch(it) })
//        homemodel?.notifyPersonalised?.observe(this, Observer { this.loadPersonalised(it) })
//        scrollview.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
//            if (scrollY > oldScrollY) {
//                Log.i(TAG, "Scroll DOWN")
//                if (!hasBanner!! && hasFullSearch) {
//                    toolbar.visibility = View.GONE
//                    fullsearch.visibility = View.VISIBLE
//                    fullsearch_container.visibility = View.VISIBLE
//                } else if (hasBanner!! && !hasFullSearch) {
//                    toolbar.visibility = View.VISIBLE
//                    fullsearch.visibility = View.GONE
//                    fullsearch_container.visibility = View.GONE
//                } else if (hasBanner!! && hasFullSearch) {
//                    toolbar.visibility = View.GONE
//                    fullsearch.visibility = View.VISIBLE
//                    fullsearch_container.visibility = View.VISIBLE
//                } else if (!hasBanner!! && !hasFullSearch) {
//                    toolbar.visibility = View.VISIBLE
//                    fullsearch.visibility = View.GONE
//                    fullsearch_container.visibility = View.GONE
//                }
//            }
//            if (scrollY < oldScrollY) {
//                Log.i(TAG, "Scroll UP")
//            }
//            if (scrollY == 0) {
//                scrollYPosition = 0
//                Log.i(TAG, "TOP SCROLL")
//                if (hasBanner!! && hasFullSearch) {
//                    toolbar.visibility = View.GONE
//                    fullsearch.visibility = View.GONE
//                    fullsearch_container.visibility = View.GONE
//                } else if (hasBanner!! && !hasFullSearch) {
//                    toolbar.visibility = View.GONE
//                    fullsearch.visibility = View.GONE
//                    fullsearch_container.visibility = View.GONE
//                } else {
//                    toolbar.visibility = View.VISIBLE
//                    fullsearch.visibility = View.GONE
//                    fullsearch_container.visibility = View.GONE
//                }
//            }
//            if (scrollY <= 200) {
//                scrollYPosition = scrollY
//                if (hasBanner!! && hasFullSearch) {
//                    toolbar.visibility = View.GONE
//                    fullsearch.visibility = View.GONE
//                    fullsearch_container.visibility = View.GONE
//                } else if (hasBanner!! && !hasFullSearch) {
//                    toolbar.visibility = View.GONE
//                    fullsearch.visibility = View.GONE
//                    fullsearch_container.visibility = View.GONE
//                } else {
//                    toolbar.visibility = View.VISIBLE
//                    fullsearch.visibility = View.GONE
//                    fullsearch_container.visibility = View.GONE
//                }
//            }
//            if (scrollY == (v?.measuredHeight!! - v?.getChildAt(0).measuredHeight)) {
//                Log.i(TAG, "BOTTOM SCROLL")
//            }
//        }
//        if (featuresModel.forceUpdate) {
//            forceUpdate()
//        }
//        if (leftmenu.isLoggedIn) {
//            homemodel?.NResponse(
//                "VuCs0uv4gPpRuMAMYS0msr1XozTDZunonCRRh6fC",
//                "zjCB8AXxljZ0a1WnIe91QtQjzmt9xzbi2CqLp8tg",
//                "client_credentials"
//            )?.observe(this, Observer { this.showData(it) })
//        }
//
//        homemodel?.notifyZendesk?.observe(this, Observer {
//            if (it) {
//                chat_but.visibility = View.VISIBLE
//            } else {
//                chat_but.visibility = View.GONE
//            }
//
//            leftMenuViewModel!!.Response()
//                .observe(this, Observer<ApiResponse> { this.leftmenuconsumeResponse(it) })
//        })
    }

    fun AssetJSONFile(filename: String?, context: Context): String? {
        var formArray = ByteArray(0)
        try {
            val manager = context.assets
            val file = manager.open(filename!!)
            formArray = ByteArray(file.available())
            file.read(formArray)
            file.close()
            return String(formArray)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return String(formArray)
    }
    fun getList1(): ArrayList<String>? {

        try {
            val array = JSONObject(AssetJSONFile("response.json", this@HomePage)).names()
            try {
                var i = 0
                val l = array.length()
                while (i < l) {
                    list.add(array.getString(i))
                    i++
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

//    private fun showData(response: ApiResponse?) {
//        Log.i("RESPONSEGET", "" + response?.data)
//        receiveToken(response?.data)
//    }
//
//    private fun receiveToken(data: JsonElement?) {
//        val jsondata = JSONObject(data.toString())
//        try {
//            if (jsondata.has("access_token")) {
//                MagePrefs.saveaccessToken(jsondata.getString("access_token"))
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private fun loadPersonalised(it: Boolean?) {
        if (it ?: false) {
//            homemodel!!.getApiResponse()
//                .observe(this, Observer<ApiResponse> { this.consumeResponse(it) })
//            homemodel!!.getBestApiResponse()
//                .observe(this, Observer<ApiResponse> { this.consumeResponse(it) })
        }
    }

//    private fun forceUpdate() {
//        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager!!.appUpdateInfo
//        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
//            ) {
//                startUpdateFlow(appUpdateInfo)
//            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                startUpdateFlow(appUpdateInfo)
//            }
//        }
//    }

//    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
//        try {
//            appUpdateManager!!.startUpdateFlowForResult(
//                appUpdateInfo,
//                IMMEDIATE,
//                this,
//                MY_REQUEST_CODE
//            )
//        } catch (e: IntentSender.SendIntentException) {
//            e.printStackTrace()
//        }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                    getApplicationContext(),
                    "Update canceled by user! Result Code: " + resultCode,
                    Toast.LENGTH_LONG
                ).show();
                finishAffinity()
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(
                    getApplicationContext(),
                    "Update success! Result Code: " + resultCode,
                    Toast.LENGTH_LONG
                ).show();
            } else {
                Toast.makeText(
                    getApplicationContext(),
                    "Update Failed! Result Code: " + resultCode,
                    Toast.LENGTH_LONG
                ).show();
                //  forceUpdate();
            }
        }
    }

    private fun consumeFullSearch(it: Boolean?) {
        hasFullSearch = it!!
    }

    private fun ConsumeBanner(it: Boolean?) {
        hasBanner = it
    }

    override fun onPause() {
        super.onPause()
        drawer_layout.closeDrawers()
    }

    fun setToggle(toolbar: androidx.appcompat.widget.Toolbar) {
        showHumburger()
        setSupportActionBar(toolbar)
        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        mDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }
        mDrawerToggle!!.syncState()
        mDrawerToggle!!.isDrawerIndicatorEnabled = true
        mDrawerToggle!!.toolbarNavigationClickListener = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.m_search, menu)
        item = menu.findItem(R.id.search_item)
        searchItem = menu.findItem(R.id.search_item)
        wishitem = menu.findItem(R.id.wish_item)
        wishitemHome = menu.findItem(R.id.wish_item)
        cartitem = menu.findItem(R.id.cart_item)
        cartitemHome = menu.findItem(R.id.cart_item)
//        item?.setActionView(R.layout.m_search)
//        searchItem?.setActionView(R.layout.m_search)
//        wishitem?.setActionView(R.layout.m_wishcount)
//        wishitemHome?.setActionView(R.layout.m_wishcount)
//        cartitem?.setActionView(R.layout.m_count)
//        cartitemHome?.setActionView(R.layout.m_count)
        val search = item?.actionView
        val searchHome = searchItem?.actionView
//        search?.setOnClickListener {
//            val searchpage = Intent(this, AutoSearch::class.java)
//            startActivity(searchpage)
//            Constant.activityTransition(this)
//        }
//        searchHome?.setOnClickListener {
//            val searchpage = Intent(this, AutoSearch::class.java)
//            startActivity(searchpage)
//            Constant.activityTransition(this)
//        }
        val notifCount = cartitem?.actionView
        val notifCountHomePage = cartitemHome?.actionView
//        textView = notifCount?.findViewById<TextView>(R.id.count)
//        textView!!.text = "" + cartCount
//        notifCount?.setOnClickListener {
//            val mycartlist = Intent(this, CartList::class.java)
//            startActivity(mycartlist)
//            Constant.activityTransition(this)
//        }
//        notifCountHomePage?.setOnClickListener {
//            val mycartlist = Intent(this, CartList::class.java)
//            startActivity(mycartlist)
//            Constant.activityTransition(this)
//        }
        val wishcount = wishitem?.actionView
        val wishcountHomePage = wishitemHome?.actionView
//        wishtextView = wishcount?.findViewById<TextView>(R.id.count)
//        wishtextView!!.text = "" + leftMenuViewModel!!.wishListcount
//        wishcount?.setOnClickListener {
//            val wishlist = Intent(this, WishList::class.java)
//            startActivity(wishlist)
//            Constant.activityTransition(this)
//        }
//        wishcountHomePage?.setOnClickListener {
//            val wishlist = Intent(this, WishList::class.java)
//            startActivity(wishlist)
//            Constant.activityTransition(this)
//        }
        if (scrollYPosition > 0) {
            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                setToggle(toolbar)
            }
//            setHomeIconColors(
//                count_color ?: "#000000",
//                count_textcolor ?: "#000000",
//                icon_color ?: "#000000"
//            )
//            var binding: MTopbarBinding? = DataBindingUtil.inflate(
//                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
//                R.layout.m_topbar,
//                null,
//                false
//            )
//            setHomeSearchOption(
//                search_position ?: "", search_placeholder
//                    ?: "", binding!!
//            )
        } else if (scrollYPosition == 0) {
            if (homepage.childCount > 0) {
                if ((homepage.getChildAt(0) as ViewGroup).getChildAt(2) is androidx.appcompat.widget.Toolbar) {
                    var home_toolbar =
                        (homepage.getChildAt(0) as ViewGroup).getChildAt(2) as androidx.appcompat.widget.Toolbar
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(1000)
                        setToggle(home_toolbar)
                    }
//                    setHomeIconColors(
//                        count_color ?: "#000000",
//                        count_textcolor ?: "#000000",
//                        icon_color ?: "#000000"
//                    )
//                    var binding: MTopbarBinding? =
//                        DataBindingUtil.getBinding<MTopbarBinding>(homepage.getChildAt(0) as View)
//                    setHomeSearchOption(
//                        search_position ?: "", search_placeholder
//                            ?: "", binding!!
//                    )
                }
            }
        }
        return true
    }

//    fun setHomeLogoImage(url: String, binding: MTopbarBinding) {
//        if (!this.isDestroyed) {
//            Log.i("MageNative", "Image URL" + url)
//            Glide.with(this)
//                .load(url)
//                .thumbnail(0.5f)
//                .apply(
//                    RequestOptions().placeholder(R.drawable.image_placeholder)
//                        .error(R.drawable.image_placeholder).dontTransform()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                )
//                .into(binding.toolimage)
//        }
//    }

    fun setHomeWishList(visiblity: String) {
        when (visiblity) {
            "1" -> {
                wishitem?.setVisible(true)
            }
            else -> {
                wishitem?.setVisible(false)
            }
        }
    }

//    fun setHomeIconColors(countback: String, counttext: String, iconcolor: String) {
//        val wishview = wishitem?.actionView
//        val cartview = cartitem?.actionView
//        val searchview = item?.actionView
//        val wishrelative = wishview?.findViewById<RelativeLayout>(R.id.back)
//        val wishtext = wishview?.findViewById<TextView>(R.id.count)
//        val wishicon = wishview?.findViewById<FontTextView>(R.id.cart_icon)
//        val cartrelative = cartview?.findViewById<RelativeLayout>(R.id.back)
//        val carttext = cartview?.findViewById<TextView>(R.id.count)
//        val carticon = cartview?.findViewById<FontTextView>(R.id.cart_icon)
//        wishrelative?.backgroundTintList = ColorStateList.valueOf(Color.parseColor(countback))
//        cartrelative?.backgroundTintList = ColorStateList.valueOf(Color.parseColor(countback))
//        wishtext?.setTextColor(Color.parseColor(counttext))
//        carttext?.setTextColor(Color.parseColor(counttext))
//        wishicon?.setTextColor(Color.parseColor(iconcolor))
//        carticon?.setTextColor(Color.parseColor(iconcolor))
//        val searchicon = searchview?.findViewById<FontTextView>(R.id.search_icon)
//        searchicon?.setTextColor(Color.parseColor(iconcolor))
//        mDrawerToggle!!.getDrawerArrowDrawable().setColor(Color.parseColor(iconcolor))
//    }

//    fun setHomeSearchOptions(
//        searchback: String,
//        searchtext: String,
//        searhcborder: String,
//        binding: MTopbarBinding
//    ) {
//        var draw: GradientDrawable = binding.search.background as GradientDrawable
//        draw.setColor(Color.parseColor(searchback))
//        binding.search.setTextColor(Color.parseColor(searchtext))
//        binding.search.setHintTextColor(Color.parseColor(searchtext))
//        draw.setStroke(2, Color.parseColor(searhcborder));
//    }

//    fun setHomeSearchOption(type: String, placeholder: String, binding: MTopbarBinding) {
//        when (type) {
//            "middle-width-search" -> {
//                searchItem?.setVisible(false)
//                item?.setVisible(false)
//               // binding.toolimage.visibility = View.GONE
//               // binding.searchsection.visibility = View.VISIBLE
//                //binding.search.text = placeholder
////                binding.search.setOnClickListener {
////                    val searchpage = Intent(this, AutoSearch::class.java)
////                    startActivity(searchpage)
////                    Constant.activityTransition(this)
////                }
//            }
//            "full-width-search" -> {
//                searchItem?.setVisible(false)
//                item?.setVisible(false)
//               // binding.toolimage.visibility = View.VISIBLE
//               // binding.searchsection.visibility = View.GONE
//            }
//            else -> {
//                /*Uncomment this code when normal toolbar selected from panel*/
////                invalidateOptionsMenu()
////                setToggle(toolbar)
////                GlobalScope.launch(Dispatchers.Main) {
////                    delay(100)
////                    setHomeIconColors(
////                        count_color ?: "#000000",
////                        count_textcolor ?: "#000000",
////                        icon_color ?: "#000000"
////                    )
////                }
//                searchItem?.setVisible(true)
//                item?.setVisible(true)
//               // binding.toolimage.visibility = View.VISIBLE
//                //binding.searchsection.visibility = View.GONE
//            }
//        }
//    }


    fun consumeResponse(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show()
    }

    fun consumeResponse(data: HashMap<String, View>) {
        //  Log.d(TAG, "onCreate: " + MagePrefs.getHomepageData())
        if (data.containsKey("top-bar_")) {
            homepage.addView(data.get("top-bar_"))
        }
        if (data.containsKey("category-circle_")) {
            homepage.addView(data.get("category-circle_"))
        }
        if (data.containsKey("banner-slider_")) {
            homepage.addView(data.get("banner-slider_"))
        }
        if (data.containsKey("product-list-slider_")) {
            homepage.addView(data.get("product-list-slider_"))
        }
        if (data.containsKey("category-square_")) {
            homepage.addView(data.get("category-square_"))
        }
        if (data.containsKey("collection-grid-layout_")) {
            homepage.addView(data.get("collection-grid-layout_"))
        }
        if (data.containsKey("standalone-banner_")) {
            homepage.addView(data.get("standalone-banner_"))
        }
        if (data.containsKey("three-product-hv-layout_")) {
            homepage.addView(data.get("three-product-hv-layout_"))
        }
        if (data.containsKey("fixed-customisable-layout_")) {
            homepage.addView(data.get("fixed-customisable-layout_"))
        }
        if (data.containsKey("collection-list-slider_")) {
            homepage.addView(data.get("collection-list-slider_"))
        }

    }

    override fun onResume() {
        super.onResume()
        if (homepage.childCount > 0) {
            if ((homepage.getChildAt(0) as ViewGroup).getChildAt(2) is androidx.appcompat.widget.Toolbar) {
                var home_toolbar =
                    (homepage.getChildAt(0) as ViewGroup).getChildAt(2) as androidx.appcompat.widget.Toolbar
                invalidateOptionsMenu()
                setToggle(home_toolbar)
                GlobalScope.launch(Dispatchers.Main) {
                    delay(100)
//                    setHomeIconColors(
//                        count_color ?: "#000000",
//                        count_textcolor ?: "#000000",
//                        icon_color ?: "#000000"
//                    )
//                    var binding: MTopbarBinding? =
//                        DataBindingUtil.getBinding<MTopbarBinding>(homepage.getChildAt(0) as View)
//                    setHomeSearchOption(
//                        search_position ?: "", search_placeholder
//                            ?: "", binding!!
//                    )
                }

            }
            // nav_view.menu.findItem(R.id.home_bottom).setChecked(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        featuresModel.tidioChat = false
//        featuresModel.zenDeskChat = false
//        featuresModel.yoptoLoyalty = false
//        featuresModel.smileIO = false
//        featuresModel.multi_currency = false
//        featuresModel.multi_language = false
//        featuresModel.showBottomNavigation=false
//        featuresModel.reOrderEnabled = false
    }

   // private fun leftmenuconsumeResponse(reponse: ApiResponse) {
//        when (reponse.status) {
//            Status.SUCCESS -> LeftMenu.renderSuccessResponse(reponse.data!!)
//            Status.ERROR -> {
//                reponse.error!!.printStackTrace()
//                //  showToast(resources.getString(R.string.errorString))
//            }
//            else -> {
//            }
//        }
   // }

//    private fun consumeResponse(reponse: ApiResponse) {
//        when (reponse.status) {
//            Status.SUCCESS -> setPersonalisedData(reponse.data!!)
//            Status.ERROR -> {
//                reponse.error!!.printStackTrace()
////                Toast.makeText(
////                    this,
////                    resources.getString(R.string.errorString),
////                    Toast.LENGTH_SHORT
////                ).show()
//            }
//        }
//    }

//    private fun setPersonalisedData(data: JsonElement) {
//        try {
//            val jsondata = JSONObject(data.toString())
//            Log.i("MageNative", "TrendingProducts" + jsondata)
//            if (jsondata.has("trending")) {
//                binding!!.personalisedsection.visibility = View.VISIBLE
//                setLayout(binding!!.personalised, "horizontal")
//                personamodel!!.setPersonalisedData(
//                    jsondata.getJSONObject("trending").getJSONArray("products"),
//                    personalisedadapter,
//                    homemodel!!.presentmentCurrency!!,
//                    binding!!.personalised
//                )
//            }
//            if (jsondata.has("bestsellers")) {
//                binding!!.bestsellerpersonalisedsection.visibility = View.VISIBLE
//                setLayout(binding!!.bestpersonalised, "horizontal")
//                personamodel!!.setPersonalisedData(
//                    jsondata.getJSONObject("bestsellers").getJSONArray("products"),
//                    padapter,
//                    homemodel!!.presentmentCurrency!!,
//                    binding!!.bestpersonalised
//                )
//            }
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }
}