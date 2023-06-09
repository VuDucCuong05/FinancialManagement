package manager.vdc.financialmanagement.main.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import manager.vdc.financialmanagement.R
import manager.vdc.financialmanagement.databinding.FragmentSettingBinding
import manager.vdc.financialmanagement.main.base.BaseFragment
import manager.vdc.financialmanagement.main.library.CustomDialog
import manager.vdc.financialmanagement.main.rdb.datab.AppDatabase
import manager.vdc.financialmanagement.splash.OnBoardingScreenActivity


class SettingFragment : BaseFragment() {
    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initEvent()

    }
    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }
    private fun initView() {
        val preferencesL = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        var timeDefaultPeriod =
            preferencesL.getString("default_period", requireContext().getString(R.string.day))
        binding.textTime.text = timeDefaultPeriod

        binding.swDiscount.isChecked = preferencesL.getBoolean("sw_discount", false)
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            onCallback()
        }

        binding.llPin.setOnClickListener {
            showMessFutureUpdate()
        }
        binding.llLanguage.setOnClickListener {
            Toast.makeText(requireActivity(),requireContext().resources.getString(R.string.future_update),Toast.LENGTH_SHORT).show()
        }

        binding.llCalendar.setOnClickListener {
            val popupMenu = PopupMenu(requireActivity(), binding.llCalendar)
            popupMenu.menuInflater.inflate(R.menu.time_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_day -> {
                        binding.textTime.text = resources.getString(R.string.day)
                        setTime("default_period", resources.getString(R.string.day))
                        true
                    }
                    R.id.menu_weekly -> {
                        binding.textTime.text = resources.getString(R.string.menu_weekly_1)
                        setTime("default_period", resources.getString(R.string.menu_weekly_1))
                        true
                    }
                    R.id.menu_monthly -> {
                        binding.textTime.text = resources.getString(R.string.month)
                        setTime("default_period", resources.getString(R.string.month))
                        true
                    }

                    R.id.menu_yearly -> {
                        binding.textTime.text = resources.getString(R.string.year)
                        setTime("default_period", resources.getString(R.string.year))
                        true
                    }
                    else -> false
                }
            }
            // show the popup menu
            popupMenu.show()
        }

        binding.swDiscount.setOnCheckedChangeListener { _, isChecked ->
            setSwDiscount("sw_discount", isChecked)
        }

        binding.llRestorePurchase.setOnClickListener {
            showMessFutureUpdate()
        }
        binding.llSecurity.setOnClickListener {
            val uri = "https://smartappstudio.github.io/tarotdaily/privacy.html"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
        binding.llDeleteData.setOnClickListener {
            val customDialog = CustomDialog(requireActivity())
            customDialog.showDialog(
                Gravity.CENTER,
                resources.getString(R.string.dialog_message),
                resources.getString(R.string.mess_delete_all_data),
                resources.getString(R.string.text_ok),
                {
                    Toast.makeText(requireActivity(),
                        requireContext().resources.getString(R.string.error_occurred),
                        Toast.LENGTH_LONG).show()
                    customDialog.dismiss()

                },
                resources.getString(R.string.text_no),
                {
                    customDialog.dismiss()
                }
            )

        }
    }

    private fun setTime(nameSaveS: String, time: String) {
        val editor = PreferenceManager.getDefaultSharedPreferences(requireActivity()).edit()
        editor.putString(nameSaveS, time)
        editor.apply()
    }

    private fun setSwDiscount(nameSaveS: String, type: Boolean) {
        val editor = PreferenceManager.getDefaultSharedPreferences(requireActivity()).edit()
        editor.putBoolean(nameSaveS, type)
        editor.apply()
    }

    private fun deleteAllData() {
        // Xóa tất cả bảng trong Room Database
        AppDatabase.getInstance(requireActivity()).clearAllTables()

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().clear().apply()

        // Xóa dữ liệu của ứng dụng
        val packageManager = requireActivity().applicationContext.packageManager
        val packageName = requireActivity().applicationContext.packageName
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent?.apply {
            addFlags(flags)
        })

        // Xóa dữ liệu cache của ứng dụng
        val clearUserData = Runtime.getRuntime().exec("pm clear $packageName")
        clearUserData.waitFor()

        // Chuyển đến màn hình SplashActivity
        val intentSplash = Intent(requireActivity(), OnBoardingScreenActivity::class.java)
        startActivity(intentSplash)
        requireActivity().finish()
    }

    override fun onStop() {
        super.onStop()
        onCallbackLockedDrawers()
    }
}