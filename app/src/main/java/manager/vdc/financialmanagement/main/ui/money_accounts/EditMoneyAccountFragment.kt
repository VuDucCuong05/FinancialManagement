package manager.vdc.financialmanagement.main.ui.money_accounts

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import manager.vdc.financialmanagement.R
import manager.vdc.financialmanagement.splash.adapter.AdapterIConColor
import manager.vdc.financialmanagement.databinding.FragmentEditAccountBinding
import manager.vdc.financialmanagement.main.adapter.AdapterIconAccount
import manager.vdc.financialmanagement.main.base.BaseFragment
import manager.vdc.financialmanagement.main.library.CustomDialog
import manager.vdc.financialmanagement.main.library.MoneyTextWatcher
import manager.vdc.financialmanagement.main.model.m_r.MoneyAccount
import manager.vdc.financialmanagement.main.model.m.IconR
import manager.vdc.financialmanagement.main.model.m_r.Account
import manager.vdc.financialmanagement.main.model.m_r.Country
import manager.vdc.financialmanagement.main.model.query_model.MoneyAccountWithDetails
import manager.vdc.financialmanagement.main.rdb.vm_data.DataViewMode


class EditMoneyAccountFragment : BaseFragment() {
    lateinit var binding: FragmentEditAccountBinding
    lateinit var dataViewMode: DataViewMode

    private lateinit var adapterIConColor: AdapterIConColor
    private lateinit var adapterIconAccount: AdapterIconAccount

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        initView()
        initEvent()

    }

    private fun initView() {
        adapterIconAccount = AdapterIconAccount(requireContext(), IconR.listIconRAccount)
        binding.rcvIconCategory.adapter = adapterIconAccount

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

        adapterIConColor = AdapterIConColor(requireContext(), IconR.getListIconCheckCircle())
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.edtTotal.addTextChangedListener(MoneyTextWatcher(binding.edtTotal))

        if (dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idMoneyAccount == 0) {
            binding.textTitleTotal.text = resources.getString(R.string.create_money_account)
            binding.textCreate.visibility = View.VISIBLE
            binding.llUpdate.visibility = View.GONE
            binding.edtTypeAccount.isEnabled = true
            val drawable = resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
            binding.edtTypeAccount.setCompoundDrawablesWithIntrinsicBounds(null,
                null,
                drawable,
                null)

            adapterIconAccount.updateSelect(1)
            val country = dataViewMode.editOrAddMoneyAccount.country
            binding.edtTypeAccount.text = country!!.currencyCode
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idCountry = country.idCountry
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idAccount = 1
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.icon = 1
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.color = 1
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.selectMoneyAccount = false

        } else {
            binding.textTitleTotal.text = resources.getString(R.string.edit_money_account)

            val moneyAccount = dataViewMode.editOrAddMoneyAccount.moneyAccount!!
            val country = dataViewMode.editOrAddMoneyAccount.country!!
            binding.textCreate.visibility = View.GONE
            if (moneyAccount.idMoneyAccount == 1) {
                binding.llUpdate.visibility = View.GONE
                binding.textSaveId1.visibility = View.VISIBLE
            } else {
                binding.llUpdate.visibility = View.VISIBLE
                binding.textSaveId1.visibility = View.GONE
            }
            binding.edtNameAccount.setText(moneyAccount.moneyAccountName)
            binding.edtTypeAccount.text = country.currencyCode
            binding.edtTotal.setText(convertFloatToString(moneyAccount.amountMoneyAccount!!))
            adapterIconAccount.updateSelect(moneyAccount.icon!!)
            adapterIConColor.updateSelectColor(moneyAccount.color!!)
            adapterIconAccount.updateColor(moneyAccount.color!!)
            binding.edtTypeAccount.isEnabled = false
        }

        if (dataViewMode.country.idCountry != 0) {
            binding.edtTypeAccount.text = dataViewMode.country.currencyCode
            dataViewMode.editOrAddMoneyAccount.country = dataViewMode.country
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idCountry =
                dataViewMode.country.idCountry
        }
    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }
        adapterIconAccount.setClickItemSelect {
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.icon = it.id
        }
        adapterIConColor.setClickItemSelect {
            adapterIconAccount.updateColor(it.idColorR!!)
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.color = it.id
        }

        binding.textSave.setOnClickListener {
            if (checkData(1)) {
                dataViewMode.updateMoneyAccount(dataViewMode.editOrAddMoneyAccount.moneyAccount!!)
                findNavController().popBackStack()
            }
        }
        binding.textSaveId1.setOnClickListener {
            if (checkData(1)) {
                dataViewMode.updateMoneyAccount(dataViewMode.editOrAddMoneyAccount.moneyAccount!!)
                findNavController().popBackStack()
            }
        }

        binding.textDelete.setOnClickListener {
            createDialogDelete(Gravity.CENTER, dataViewMode.editOrAddMoneyAccount.moneyAccount!!)
        }
        binding.textCreate.setOnClickListener {
            if (checkData(2)) {
                dataViewMode.addMoneyAccount(dataViewMode.editOrAddMoneyAccount.moneyAccount!!)
                findNavController().popBackStack()
            }
        }

    }

    private fun checkData(typeClick: Int): Boolean {
        val textName = binding.edtNameAccount.text.toString()
        if (textName.isEmpty()) {
            Toast.makeText(requireContext(),resources.getString(R.string.category_names_cannot_be_left_blank),
                Toast.LENGTH_SHORT).show()
            return false
        }
        dataViewMode.editOrAddMoneyAccount.moneyAccount!!.moneyAccountName = textName

        val value = MoneyTextWatcher.parseCurrencyValue(binding.edtTotal.text.toString())
        val temp = value.toString()
        if (binding.edtTotal.text.isEmpty()) {
            Toast.makeText(requireContext(), resources.getString(R.string.please_enter_the_value), Toast.LENGTH_SHORT).show()
            return false
        }
        try {
            val number = temp.toFloat()
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.amountMoneyAccount = number
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), resources.getString(R.string.you_entered_the_wrong_format), Toast.LENGTH_SHORT)
                .show()
        }

        if (typeClick == 2) {
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idMoneyAccount = 0
        }

        return true
    }

    private fun createDialogDelete(gravity: Int, moneyAccount: MoneyAccount) {
        val customDialog = CustomDialog(requireActivity())
        customDialog.showDialog(
            Gravity.CENTER,
            resources.getString(R.string.dialog_message),
            resources.getString(R.string.money_account_delete_confirmation),
            resources.getString(R.string.text_ok),
            {
                dataViewMode.deleteMoneyAccount(moneyAccount)
                customDialog.dismiss()
                findNavController().popBackStack()
            },
            resources.getString(R.string.text_no),
            {
                customDialog.dismiss()
            }
        )
    }

    override fun onDestroy() {
        // select country
        dataViewMode.country = Country()
        dataViewMode.editOrAddMoneyAccount =
            MoneyAccountWithDetails(MoneyAccount(), Country(), Account())
        super.onDestroy()
    }
}