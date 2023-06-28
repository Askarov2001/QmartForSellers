package com.example.qmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.qmart.data.Repository
import com.example.qmart.databinding.ActivityMainBinding
import com.example.qmart.ui.addproduct.ProductCreateViewModel
import com.example.qmart.ui.product.ProductViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var productViewModel: ProductViewModel

    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val sp: SharedPref by lazy {
        SharedPref().apply {
            init(this@MainActivity)
        }
    }

    private val navGraphId: Int by lazy {
        R.navigation.nav_graph
    }
    private val menuId: Int by lazy {
        R.menu.bottom_nav_menu
    }

    private val defaultMenuItem: Int by lazy {
        R.id.navigation_home
    }
    private val hostFragment: Fragment? by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
    }
    private val navController: NavController by lazy {
        hostFragment?.findNavController() as NavController
    }

    private val bottomNavigation: BottomNavigationView by lazy {
        findViewById(R.id.nav_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth.addAuthStateListener {
            val uid = it.currentUser?.uid
            uid?.let {
                database.child("sellers").child(uid).child("business").get().addOnSuccessListener {
                    sp.addProperty(SharedPref.MERCHANT, it.value.toString())
                }
                sp.addProperty(SharedPref.UID, uid)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.setProducts(Repository.products)

        navController.graph = navController.navInflater.inflate(navGraphId)
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(menuId)
        bottomNavigation.setupWithNavController(navController)
        bottomNavigation.setOnItemSelectedListener { item ->
                navController.navigate(item.itemId, null)
                true
            }
        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }*/
    }

    fun addProperty(name: String, value: String) {
        sp.addProperty(name, value)
    }

    fun getValue(name: String): String? = sp.getProperty(name)
}