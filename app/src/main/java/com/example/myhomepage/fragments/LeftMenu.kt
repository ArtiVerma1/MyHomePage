package com.example.myhomepage.fragments

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.myhomepage.Activities.NewBaseActivity
import com.example.myhomepage.R
import com.example.myhomepage.databinding.MLeftmenufragmentBinding
import com.example.myhomepage.model.MenuData

class LeftMenu : BaseFragment() {
    private var binding: MLeftmenufragmentBinding? = null
    lateinit var  menuData : MenuData
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    private var currentactivity: Activity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.m_leftmenufragment,
            container,
            true
        )
        //  menulist = binding!!.menulist
        var pInfo: PackageInfo? = null
        try {
            //pInfo = activity!!.packageManager.getPackageInfo(activity!!.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val version = pInfo!!.versionName
        val versioncode = pInfo.versionCode
        Log.i("MageNative", "LeftMenuResume 4")

        val app_version = "App Version $version($versioncode)"
        menuData!!.appversion = app_version
        menuData!!.copyright =
            resources.getString(R.string.copy) + " " + resources.getString(R.string.app_name)
      //  binding!!.features = FeaturesModel()
        binding!!.menudata = menuData
        //  binding!!.clickdata = ClickHandlers(currentcontext, binding)
//        (activity!!.application as MyApplication).mageNativeAppComponent!!.doLeftMeuInjection(this)
//        leftmenu = ViewModelProvider(this, viewModelFactory).get(LeftMenuViewModel::class.java)
//        leftmenu.data.observe(
//            viewLifecycleOwner,
//            Observer<HashMap<String, String>> { this.consumeResponse(it) })
        return binding!!.root
    }

//    private fun consumeResponse(hash: HashMap<String, String>) {
//        menuData!!.tag = hash.get("tag")
//        Log.i("MageNative", "LeftMenuResume 3" + hash["firstname"]!!)
//        menuData!!.username = hash["firstname"] + " " + hash["secondname"]
//        if (hash["tag"] == "login") {
//            menuData!!.visible = View.VISIBLE
//        } else {
//            menuData!!.visible = View.GONE
//        }
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // currentcontext = context
        currentactivity = context as NewBaseActivity
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    class ClickHandlers(
        internal var context: Context?,
        internal var binding: MLeftmenufragmentBinding? = null
    ) {
        private var open = false

//        fun getMenu(view: View, menudata: MenuData) {
//            when (menudata.type) {
//                "collection" -> {
//                    val intent = Intent(context, ProductList::class.java)
//                    if (menudata.id == null) {
//                        intent.putExtra("handle", menudata.handle)
//                    } else {
//                        try {
//                            val data = Base64.encode(
//                                ("gid://shopify/Collection/" + menudata.id!!).toByteArray(),
//                                Base64.DEFAULT
//                            )
//                            val id = String(data, Charset.defaultCharset()).trim { it <= ' ' }
//                            intent.putExtra("ID", id)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//
//                    }
//                    intent.putExtra("tittle", menudata.title)
//                    context!!.startActivity(intent)
//                    Constant.activityTransition(context!!)
//                }
//                "product" -> {
//                    val productintent = Intent(context, ProductView::class.java)
//                    if (menudata.id == null) {
//                        productintent.putExtra("handle", menudata.handle)
//                    } else {
//                        try {
//                            val data = Base64.encode(
//                                ("gid://shopify/Product/" + menudata.id!!).toByteArray(),
//                                Base64.DEFAULT
//                            )
//                            val id = String(data, Charset.defaultCharset()).trim { it <= ' ' }
//                            productintent.putExtra("ID", id)
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//                    productintent.putExtra("tittle", menudata.title)
//                    context!!.startActivity(productintent)
//                    Constant.activityTransition(context!!)
//                }
//                "product-all" -> {
//                    val product_all = Intent(context, ProductList::class.java)
//                    product_all.putExtra("tittle", menudata.title)
//                    context!!.startActivity(product_all)
//                    Constant.activityTransition(context!!)
//                }
//                "collection-all" -> {
//                    val collection_all = Intent(context, CollectionList::class.java)
//                    context!!.startActivity(collection_all)
//                    Constant.activityTransition(context!!)
//                }
//                "page" -> {
//                    val page = Intent(context, Weblink::class.java)
//                    page.putExtra("name", menudata.title)
//                    page.putExtra("link", menudata.url)
//                    context!!.startActivity(page)
//                    Constant.activityTransition(context!!)
//                }
//                "blog" -> {
//                    val blog = Intent(context, Weblink::class.java)
//                    blog.putExtra("name", menudata.title)
//                    blog.putExtra("link", menudata.url)
//                    context!!.startActivity(blog)
//                    Constant.activityTransition(context!!)
//                }
//            }
//        }

//        fun expandMenu(view: View) {
//            val constraintLayout = view.parent as ConstraintLayout
//            val linearLayoutCompat = constraintLayout.getChildAt(2) as LinearLayoutCompat
//            if (open) {
//                linearLayoutCompat.visibility = View.GONE
//                Glide.with(view)
//                    .load(R.drawable.add)
//                    .into(view as ImageView)
//                open = false
//            } else {
//                linearLayoutCompat.visibility = View.VISIBLE
//                linearLayoutCompat.requestFocus()
//                Glide.with(view)
//                    .load(R.drawable.minus_icon)
//                    .into(view as ImageView)
//                open = true
//            }
//        }
//
//        fun navigationClicks(view: View) {
//            when (view.tag as String) {
//                "livepreview" -> {
//                    val integrator = IntentIntegrator((context as NewBaseActivity)!!)
//                    integrator.setPrompt("Scan Your Barcode")
//                    integrator.setCameraId(0) // Use a specific camera of the device
//                    integrator.setOrientationLocked(true)
//                    integrator.setBeepEnabled(true)
//                    integrator.captureActivity = LivePreview::class.java
//                    integrator.initiateScan()
//                }
//                "currencyswitcher" -> {
//                    Log.i("MageNative", "currencyswitcher" + " : IN")
//                    (context as NewBaseActivity)!!.getCurrency()
//                }
//                "languageswither" -> {
//                    (context as NewBaseActivity)!!.showLanguageDialog()
//                }
//                "collections" -> {
//                    val collection_all = Intent(context, CollectionList::class.java)
//                    context!!.startActivity(collection_all)
//                    Constant.activityTransition(context!!)
//                }
//                "Sign In" -> {
//                    val login = Intent(context, LoginActivity::class.java)
//                    context!!.startActivity(login)
//                    Constant.activityTransition(context!!)
//                }
//                "mywishlist" -> {
//                    val mywishlist = Intent(context, WishList::class.java)
//                    context!!.startActivity(mywishlist)
//                    Constant.activityTransition(context!!)
//                }
//                "mycartlist" -> {
//                    val mycartlist = Intent(context, CartList::class.java)
//                    context!!.startActivity(mycartlist)
//                    Constant.activityTransition(context!!)
//                }
//                "invitefriends" -> {
//                    val appPackageName =
//                        view.context.packageName // getPackageName() from Context or Activity object
//                    val shareIntent = Intent(Intent.ACTION_SEND)
//                    shareIntent.type = "text/plain"
//                    shareIntent.putExtra(
//                        Intent.EXTRA_SUBJECT,
//                        view.context.resources.getString(R.string.app_name)
//                    )
//                    shareIntent.putExtra(
//                        Intent.EXTRA_TEXT,
//                        "https://play.google.com/store/apps/details?id=$appPackageName"
//                    )
//                    view.context.startActivity(
//                        Intent.createChooser(
//                            shareIntent,
//                            view.context.resources.getString(R.string.shareproduct)
//                        )
//                    )
//                    Constant.activityTransition(view.context)
//                }
//                "autosearch" -> {
//                    val autosearch = Intent(context, AutoSearch::class.java)
//                    context!!.startActivity(autosearch)
//                    Constant.activityTransition(context!!)
//                }
//                "earnrewards" -> {
//                    if (leftmenu.isLoggedIn) {
//                        val rewards = Intent(context, RewardDashboard::class.java)
//                        context!!.startActivity(rewards)
//                        Constant.activityTransition(context!!)
//                    } else {
//                        val rewards = Intent(context, RewardsPointActivity::class.java)
//                        context!!.startActivity(rewards)
//                        Constant.activityTransition(context!!)
//                    }
//                }
//                "chats" -> {
//                    val chats = Intent(context, Weblink::class.java)
//                    chats.putExtra("name", "Chat With Us")
//                    chats.putExtra(
//                        "link",
//                        "https://shopifymobileapp.cedcommerce.com/shopifymobile/tidiolivechatapi/chatpanel?shop=magenative.myshopify.com")
//                    context!!.startActivity(chats)
//                    Constant.activityTransition(context!!)
//                }
//                "smilereward" -> {
//                    if (leftmenu.isLoggedIn) {
//                        val intent = Intent(context, Weblink::class.java)
//                        intent.putExtra("name", "My Rewards")
//                        intent.putExtra(
//                            "link",
//                            "https://shopifymobileapp.cedcommerce.com/shopifymobile/smilerewardapi/generateview?mid=18&cid=" +MagePrefs.getCustomerID())
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                        context!!.startActivity(intent)
//                        Constant.activityTransition(context!!)
//                    } else {
//                        val rewards = Intent(context, LoginActivity::class.java)
//                        context!!.startActivity(rewards)
//                        Constant.activityTransition(context!!)
//                    }
//                }
//                "logout" -> {
//                    MagePrefs.clearUserData()
//                    binding?.signin?.text = context?.resources?.getString(R.string.SignIn)
//                    binding?.signin?.tag = "Sign In"
//                    binding?.logout?.visibility = View.GONE
//                    Toast.makeText(
//                        context,
//                        context!!.resources.getString(R.string.successlogout),
//                        Toast.LENGTH_LONG
//                    ).show()
//                    leftmenu.logOut()
//                }
//                "myprofile" -> if (leftmenu.isLoggedIn) {
//                    val myprofile = Intent(context, UserProfile::class.java)
//                    context!!.startActivity(myprofile)
//                    Constant.activityTransition(context!!)
//                } else {
//                    Toast.makeText(
//                        context,
//                        context!!.resources.getString(R.string.logginfirst),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                "myorders" -> if (leftmenu.isLoggedIn) {
//                    val myprofile = Intent(context, OrderList::class.java)
//                    context!!.startActivity(myprofile)
//                    Constant.activityTransition(context!!)
//                } else {
//                    Toast.makeText(
//                        context,
//                        context!!.resources.getString(R.string.logginfirst),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                "myaddress" -> if (leftmenu.isLoggedIn) {
//                    val myaddress = Intent(context, AddressList::class.java)
//                    context!!.startActivity(myaddress)
//                    Constant.activityTransition(context!!)
//                } else {
//                    Toast.makeText(
//                        context,
//                        context!!.resources.getString(R.string.logginfirst),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.i("MageNative", "LeftMenuResume")
//        when (activity!!.getPackageName()) {
//            "com.shopify.shopifyapp" -> {
//                menuData!!.previewvislible = View.VISIBLE
//            }
//            else -> {
//                menuData!!.previewvislible = View.GONE
//            }
//        }
//        leftmenu.context = currentcontext
//        leftmenu.fetchUserData()
//    }

//    companion object {
//        lateinit var menulist: LinearLayoutCompat
//        private var currentcontext: Context? = null
//        protected lateinit var leftmenu: LeftMenuViewModel
//        private var menuData: MenuData? = null
//        fun renderSuccessResponse(data: JsonElement) {
//            Log.i("MageNative:", "MenuData$data")
//            if (menulist.childCount == 0) {
//                val handler = Handler()
//                val runnable = Runnable {
//                    try {
//                        val `object` = JSONObject(data.toString())
//                        if (`object`.getBoolean("success")) {
//                            if (`object`.has("data")) {
//                                val array = `object`.getJSONArray("data")
//                                if (array.length() > 0) {
//                                    for (i in 0 until array.length()) {
//                                        handler.post {
//                                            try {
//                                                // Log.i("MageNative","CurrentContext :"+currentcontext)
//                                                val binding: MDynamicmenuBinding =
//                                                    DataBindingUtil.inflate(
//                                                        currentcontext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
//                                                        R.layout.m_dynamicmenu,
//                                                        null,
//                                                        false
//                                                    )
//                                                val menuData = MenuData()
//                                                if (array.getJSONObject(i).has("id")) {
//                                                    menuData.id =
//                                                        array.getJSONObject(i).getString("id")
//                                                }
//                                                if (array.getJSONObject(i).has("handle")) {
//                                                    menuData.handle =
//                                                        array.getJSONObject(i).getString("handle")
//                                                }
//                                                if (array.getJSONObject(i).has("type")) {
//                                                    menuData.type =
//                                                        array.getJSONObject(i).getString("type")
//                                                }
//                                                menuData.title =
//                                                    array.getJSONObject(i).getString("title")
//                                                if (array.getJSONObject(i).has("url")) {
//                                                    menuData.url =
//                                                        array.getJSONObject(i).getString("url")
//                                                }
//                                                binding.menudata = menuData
//                                                binding.clickdata = ClickHandlers(currentcontext)
//                                                if (array.getJSONObject(i).has("menus")) {
//                                                    binding.root.findViewById<View>(R.id.expand_collapse).visibility =
//                                                        View.VISIBLE
//                                                    updateMenu(
//                                                        array.getJSONObject(i)
//                                                            .getJSONArray("menus"),
//                                                        binding.root.findViewById(R.id.submenus)
//                                                    )
//                                                }
//                                                menulist.addView(binding.root)
//                                            } catch (e: Exception) {
//                                                Log.i("MageNative", "Error" + e.message)
//                                                Log.i("MageNative", "Error" + e.cause)
//                                                e.printStackTrace()
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//                Thread(runnable).start()
//            }
//        }
//
////        private fun updateMenu(array: JSONArray, menulist: LinearLayoutCompat) {
////            val handler = Handler()
////            val runnable = Runnable {
////                if (array.length() > 0) {
////                    for (i in 0 until array.length()) {
////                        handler.post {
////                            try {
////                                try {
////                                    val binding = DataBindingUtil.inflate<MDynamicmenuBinding>(
////                                        currentcontext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
////                                        R.layout.m_dynamicmenu,
////                                        null,
////                                        false
////                                    )
////                                    val menuData = MenuData()
////                                    if (array.getJSONObject(i).has("id")) {
////                                        menuData.id = array.getJSONObject(i).getString("id")
////                                    }
////                                    if (array.getJSONObject(i).has("handle")) {
////                                        menuData.handle = array.getJSONObject(i).getString("handle")
////                                    }
////                                    menuData.title = array.getJSONObject(i).getString("title")
////                                    if (array.getJSONObject(i).has("type")) {
////                                        menuData.type = array.getJSONObject(i).getString("type")
////                                    }
////                                    if (array.getJSONObject(i).has("url")) {
////                                        menuData.url = array.getJSONObject(i).getString("url")
////                                    }
////                                    binding.menudata = menuData
////                                    binding.clickdata = ClickHandlers(currentcontext)
////                                    if (array.getJSONObject(i).has("menus")) {
////                                        binding.root.findViewById<View>(R.id.expand_collapse).visibility =
////                                            View.VISIBLE
////                                        updateMenu(
////                                            array.getJSONObject(i).getJSONArray("menus"),
////                                            binding.root.findViewById(R.id.submenus)
////                                        )
////                                    }
////                                    menulist.addView(binding.root)
////                                } catch (e: Exception) {
////                                    e.printStackTrace()
////                                }
////                            } catch (e: Exception) {
////                                e.printStackTrace()
////                            }
////                        }
////                    }
////                }
////            }
////            Thread(runnable).start()
////        }
    }
}