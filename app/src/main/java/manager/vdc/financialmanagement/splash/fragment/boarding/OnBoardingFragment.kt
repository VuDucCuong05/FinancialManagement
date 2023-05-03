package manager.vdc.financialmanagement.splash.fragment.boarding

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import manager.vdc.financialmanagement.R
import manager.vdc.financialmanagement.splash.adapter.OnBoardingPagerAdapter
import manager.vdc.financialmanagement.databinding.FragmentOnBoardingBinding
import manager.vdc.financialmanagement.main.model.m_r.Account
import manager.vdc.financialmanagement.main.rdb.vm_data.DataViewMode
import java.util.*

class OnBoardingFragment : Fragment() {
    lateinit var binding: FragmentOnBoardingBinding
    private lateinit var onBoardingPagerAdapter: OnBoardingPagerAdapter
    private lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        initxData()
        createDefaultAccount()
        onBoardingPagerAdapter = OnBoardingPagerAdapter(requireActivity())
        binding.viewPagerLogin.adapter = onBoardingPagerAdapter
        binding.indicator.setViewPager(binding.viewPagerLogin)

        binding.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_creatsMoneyFragment)
        }
    }

    private fun initxData() {
        dataViewMode.readAllDataLiveAccount.observe(requireActivity()){
            dataViewMode.listAccount = it
            if(it.isNotEmpty()){
                Log.d("aaa","size: ${it.size}")
            }
        }
    }

    private fun createDefaultAccount() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val isFirstTime = sharedPref.getBoolean("createDefaultAccount", true)
        if (isFirstTime) {
            dataViewMode.addAccount(Account(0, "Default account","Default account", "00000", "null", false))
            sharedPref.edit().putBoolean("createDefaultAccount", false).apply()
        }
    }
}