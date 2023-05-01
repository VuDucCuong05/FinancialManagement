package com.example.qltaichinhcanhan.main

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.ActivityNdmainBinding
import com.example.qltaichinhcanhan.main.inf.MyCallback
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo
import com.example.qltaichinhcanhan.main.ui.reminder.NotificationHandler

class NDMainActivity : AppCompatActivity(), MyCallback {

    private lateinit var binding: ActivityNdmainBinding
    private lateinit var textNameAccount: TextView
    private lateinit var imageAccount: ImageView
    private  var account = Account()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNdmainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColorStatusbar()

        setSupportActionBar(binding.appBarNdmain.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_ndmain)
        binding.navView.setupWithNavController(navController)

    }

    fun setColorStatusbar() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blu_mani)
        window.navigationBarColor = resources.getColor(R.color.blu_mani)
    }

    override fun onCallback() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onCallbackLockedDrawers() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onCallbackUnLockedDrawers() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onCallbackAccount(accountNew: Account) {

    }

    override fun onDestroy() {
        super.onDestroy()
        val editor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        editor.putBoolean("updated_language", false)
        editor.apply()
    }
}