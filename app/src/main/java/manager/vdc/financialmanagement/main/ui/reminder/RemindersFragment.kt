package manager.vdc.financialmanagement.main.ui.reminder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import manager.vdc.financialmanagement.R
import manager.vdc.financialmanagement.databinding.FragmentRemindersBinding
import manager.vdc.financialmanagement.main.adapter.AdapterNotification
import manager.vdc.financialmanagement.main.base.BaseFragment
import manager.vdc.financialmanagement.main.model.m_r.NotificationInfo
import manager.vdc.financialmanagement.main.rdb.vm_data.DataViewMode

class RemindersFragment : BaseFragment() {
    lateinit var binding: FragmentRemindersBinding
    lateinit var dataViewMode: DataViewMode
    private lateinit var adapterNotification: AdapterNotification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRemindersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        binding.btnNavigation.setOnClickListener {
            onCallback()
        }

        binding.llCreateNotification.setOnClickListener {
            dataViewMode.selectNotificationInfoReminderToEdit = NotificationInfo()
            findNavController().navigate(R.id.action_nav_reminders_to_editNotificationFragment)
        }
        initView()
    }

    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    private fun initView() {

        adapterNotification = AdapterNotification(requireActivity(), listOf())
        binding.rcvNotificationInfo.adapter = adapterNotification
        binding.rcvNotificationInfo.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        dataViewMode.readAllNotificationInfoLive.observe(requireActivity()) {
            Log.e("data", "check size country: ${it.size}")
            adapterNotification.updateData(it)
        }

        adapterNotification.setClickItemSelect {
            dataViewMode.selectNotificationInfoReminderToEdit = it
            findNavController().navigate(R.id.action_nav_reminders_to_editNotificationFragment)
        }
        adapterNotification.setClickItemSelectSw {
            dataViewMode.updateNotificationInfo(it)
            val notificationHandler = NotificationHandler(requireActivity())
            if (it.isNotificationSelected!!) {
                notificationHandler.scheduleNotification(it)
            } else {
                notificationHandler.cancelScheduledNotification(it.idNotification)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        onCallbackLockedDrawers()
    }
}