package manager.vdc.financialmanagement.main.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import manager.vdc.financialmanagement.databinding.FragmentIconCatalogBinding
import manager.vdc.financialmanagement.main.adapter.IconCategoryAdapter
import manager.vdc.financialmanagement.main.base.BaseFragment
import manager.vdc.financialmanagement.main.inf.IconClickListener
import manager.vdc.financialmanagement.main.model.m.DefaultData
import manager.vdc.financialmanagement.main.model.m.IconR
import manager.vdc.financialmanagement.main.rdb.vm_data.DataViewMode


class IconCatalogFragment : BaseFragment(), IconClickListener {

    lateinit var binding: FragmentIconCatalogBinding
    private lateinit var iconCategoryAdapter: IconCategoryAdapter
    lateinit var dataViewMode: DataViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentIconCatalogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        val layoutManager = LinearLayoutManager(activity)
        binding.rcvIcon.layoutManager = layoutManager
        iconCategoryAdapter =
            IconCategoryAdapter(requireContext(), DefaultData.listIconRCategory, this)
        binding.rcvIcon.adapter = iconCategoryAdapter

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onIconClick(iconR: IconR) {
        Toast.makeText(context, "Selected icon: ${iconR.iconName}", Toast.LENGTH_SHORT).show()
        dataViewMode.selectIconR.id = iconR.id
        findNavController().popBackStack()
    }

}