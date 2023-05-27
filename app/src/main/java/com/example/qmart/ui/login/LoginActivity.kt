package com.example.qmart.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.qmart.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val fragment = intent.getStringExtra("fragment")
        when (fragment) {
            "sigin" -> {
                addFragment(SignInFragment())
            }

            else -> {
                addFragment(WelcomeFragment())
            }
        }

    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment, null)
            .addToBackStack(null)
            .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment, null)
        }
    }

    fun popBack() {
        supportFragmentManager.popBackStack()
    }
}