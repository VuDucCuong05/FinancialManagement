package com.example.qltaichinhcanhan.splash.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import kotlinx.coroutines.*


class SplashFragment : Fragment() {
    lateinit var dataViewMode: DataViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(this)[DataViewMode::class.java]


        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("default_account_initialization_check",
                Context.MODE_PRIVATE)
        val checkData = sharedPreferences.getBoolean("ck_account", false)

        if (!checkData) {
            lifecycleScope.launch {
                delay(500)
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }

        } else {
            lifecycleScope.launch {
                delay(500)
                val intent = Intent(requireActivity(), NDMainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

}