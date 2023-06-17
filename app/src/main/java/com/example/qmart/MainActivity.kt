package com.example.qmart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_orders,
                R.id.navigation_plus,
                R.id.navigation_products,
                R.id.navigation_more
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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