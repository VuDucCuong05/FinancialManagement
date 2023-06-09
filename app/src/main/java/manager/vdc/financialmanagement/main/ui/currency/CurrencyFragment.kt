package manager.vdc.financialmanagement.main.ui.currency

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import manager.vdc.financialmanagement.R
import manager.vdc.financialmanagement.databinding.FragmentCurrencyBinding
import manager.vdc.financialmanagement.main.base.BaseFragment
import manager.vdc.financialmanagement.main.model.m_r.Country
import manager.vdc.financialmanagement.main.model.m_api.CurrencyDataAPI
import manager.vdc.financialmanagement.main.rdb.vm_data.DataViewMode
import manager.vdc.financialmanagement.main.retrofit.CountryService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import manager.vdc.financialmanagement.main.adapter.AdapterCountry
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class CurrencyFragment : BaseFragment() {
    private lateinit var binding: FragmentCurrencyBinding
    lateinit var adapterCountry: AdapterCountry
    lateinit var dataViewMode: DataViewMode


    var listCountry = listOf<Country>()
    var position = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCurrencyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
    }

    private fun initView() {

        if (dataViewMode.checkInputScreenCurrency == 0) {
            binding.btnNavigation.isActivated = false
            binding.textCurrencyConversion.visibility = View.VISIBLE
            binding.textTitleAccount.setText(R.string.menu_currency)
        } else if (dataViewMode.checkInputScreenCurrency == 1) {
            binding.btnNavigation.isActivated = true
            binding.textCurrencyConversion.visibility = View.GONE
            binding.textTitleAccount.setText(R.string.text_sreach)
        }
        val country = Country()
        adapterCountry =
            AdapterCountry(
                requireActivity(),
                arrayListOf(),
              AdapterCountry.LayoutType.TYPE1,
                country,
                country,
                0F)
        binding.rcvCategory.adapter = adapterCountry
        binding.rcvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        dataViewMode.readAllDataLiveCountry.observe(requireActivity()) {
            listCountry = it
            position = listCountry.indexOfFirst { it.selectCountry == true }
            adapterCountry.updateData(it as ArrayList<Country>)
            if (position != -1) {
                binding.rcvCategory.layoutManager?.scrollToPosition(position)
            }
        }
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("listCountry", Context.MODE_PRIVATE)
        val checkData = sharedPreferences.getBoolean("ck", false)

        if (!checkData) {
            Log.e("data", "list: null")
            binding.pressedLoading.visibility = View.VISIBLE
            binding.rcvCategory.visibility = View.INVISIBLE
            initData()
        } else {
            Log.e("data", "list: true")
            binding.pressedLoading.visibility = View.GONE
            binding.rcvCategory.visibility = View.VISIBLE
        }
        if (dataViewMode.checkInputScreenCurrency == 0) {
            Log.e("dddd", "fsdfsdf: $position")
//            updateExchangeRateByDay()
        }
    }

    private fun initEvent() {

        binding.btnNavigation.setOnClickListener {
            if (dataViewMode.checkInputScreenCurrency == 0) {
                onCallback()
            } else if (dataViewMode.checkInputScreenCurrency == 1) {
                findNavController().popBackStack()
            }
        }

        binding.imgSearch.setOnClickListener {
            binding.clSearch.visibility = View.VISIBLE
            binding.edtSearch.requestFocus()
            showKeyboard(binding.edtSearch)
        }

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                imm.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.imgArrowBack.setOnClickListener {
            if (dataViewMode.checkInputScreenCurrency == 0) {
                binding.clSearch.visibility = View.GONE
                binding.clActionBarTop.visibility = View.VISIBLE
                binding.edtSearch.setText("")
                hideKeyboard(binding.edtSearch)
            } else if (dataViewMode.checkInputScreenCurrency == 1) {
                findNavController().popBackStack()
            }

        }

        binding.imgClose.setOnClickListener {
            binding.edtSearch.setText("")
            hideKeyboard(binding.edtSearch)
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterCountry.updateData(filterList(s.toString(),
                    listCountry as ArrayList<Country>))
                if (s!!.isEmpty()) {
                    binding.imgClose.visibility = View.GONE
                    binding.rcvCategory.layoutManager?.scrollToPosition(position)
                    binding.textNotData.visibility = View.GONE
                } else {
                    binding.imgClose.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        adapterCountry.setClickItemSelect {
            if (dataViewMode.checkInputScreenCurrency == 0) {
                createDialogCurrencyExchange(Gravity.CENTER, listCountry[position], it)

            } else if (dataViewMode.checkInputScreenCurrency == 1) {
                dataViewMode.country = it
                findNavController().popBackStack()
            }
        }

        binding.textCurrencyConversion.setOnClickListener {
            findNavController().navigate(R.id.action_nav_currency_to_currencyConversionFragment)
        }

    }

    private fun updateExchangeRateByDay() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val isFirstTime = sharedPref.getBoolean("ExchangeRate", false)
        if (!isFirstTime) {
            getExchangeRate(listCountry)
        }

    }

    private fun filterList(query: String, listCountry: List<Country>): ArrayList<Country> {
        val filteredList = arrayListOf<Country>()
        val searchText = query.toLowerCase()
        for (i in listCountry) {
            if (i.countryName?.toLowerCase()?.contains(searchText) == true ||
                i.currencyCode?.toLowerCase()?.contains(searchText) == true ||
                i.currencyName?.toLowerCase()?.contains(searchText) == true ||
                i.currencySymbol?.toLowerCase()?.contains(searchText) == true
            ) {
                filteredList.add(i)
            }
        }
        if (filteredList.isEmpty()) {
            binding.textNotData.visibility = View.VISIBLE
        } else {
            binding.textNotData.visibility = View.GONE
        }
        return filteredList
    }

    private fun initData() {
        val countryService = Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryService::class.java)

        val listCountryNew = arrayListOf<Country>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val country = countryService.getAllCountries()
                withContext(Dispatchers.Main) {
                    if (country.isNotEmpty()) {
                        Log.e("data", "Số lượng quốc gia: ${country.size}")
                        for (i in country) {
                            if (i.currencies != null) {
                                listCountryNew.add(Country(0,
                                    i.name,
                                    i.currencies[0].code,
                                    i.currencies[0].name,
                                    i.currencies[0].symbol,
                                    i.flag, 1F, false))
                            }
                        }
                        Log.e("data", "Số lượng quốc gia đã được conver: ${listCountryNew.size}")
                        if (listCountryNew.size != 0) {
                            if (listCountryNew[0].currencyCode == "AFN") {
                                listCountryNew.removeAt(0)
                            }
                            dataViewMode.addListCountry(listCountryNew)
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("MainActivity", "Error getting country data", e)
            } finally {
                withContext(Dispatchers.Main) {
                    binding.pressedLoading.visibility = View.GONE
                    binding.rcvCategory.visibility = View.VISIBLE
                    adapterCountry.updateData(listCountryNew)
                    val sharedPreferences: SharedPreferences =
                        requireActivity().getSharedPreferences("listCountry", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("ck", true)
                    editor.commit()
                }
            }
        }
    }

    private fun getExchangeRate(countryList: List<Country>) {
        binding.pressedLoading.visibility = View.VISIBLE
        binding.rcvCategory.visibility = View.GONE
        val currencyCodes = countryList.map { it.currencyCode }
        val position = countryList.indexOfFirst { it.selectCountry == true }
        if (countryList.isEmpty()) {
            Toast.makeText(requireContext(),
                requireContext().resources.getString(R.string.error_occurred),
                Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            val client = OkHttpClient().newBuilder().build()
            CoroutineScope(Dispatchers.IO).launch {
                val request = Request.Builder()
                    .url("https://api.apilayer.com/currency_data/live?source=${currencyCodes[position]}&currencies=${currencyCodes}")
                    .addHeader("apikey", "RBoOmM3hdqp3wjPJuhZxg6MSjcsEqg4D")
                    .method("GET", null)
                    .build()
                val response = client.newCall(request).execute()
                val json = response.body()?.string()
                val currencyData = Gson().fromJson(json, CurrencyDataAPI::class.java)

                countryList.forEach { country ->
                    val exchangeRate =
                        currencyData.quotes["${currencyData.source}${country.currencyCode}"]
                    if (exchangeRate != null) {
                        country.exchangeRate = exchangeRate.toFloat()
                        dataViewMode.updateCountry(country)
                    }
                }
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                sharedPref.edit().putBoolean("ExchangeRate", true).apply()
                binding.pressedLoading.visibility = View.GONE
                binding.rcvCategory.visibility = View.VISIBLE
            }
        }
    }

    private fun createDialogCurrencyExchange(gravity: Int, type1: Country, type2: Country) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_layout)

        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wLayoutParams = window.attributes
        wLayoutParams.gravity = gravity
        window.attributes = wLayoutParams

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(false)
        } else {
            dialog.setCancelable(false)
        }
        dialog.show()

        val textMessage = dialog.findViewById<TextView>(R.id.dialog_message)
        val textCo = dialog.findViewById<TextView>(R.id.text_co)
        val textKhong = dialog.findViewById<TextView>(R.id.text_khong)

        val t1 = resources.getText(R.string.currency_exchange).toString()
        val t2 = resources.getText(R.string.currency_exchange1).toString()
        val thanh = requireContext().resources.getString(R.string.wall)
        textMessage.text =
            "$t1 ${type1.currencyCode.toString()} (${type1.currencySymbol.toString()}) " +
                    "${thanh} ${type2.currencyCode.toString()} (${type2.currencySymbol.toString()}). $t2"

        textCo.setOnClickListener {
            // hàm đổi
            dialog.dismiss()
            findNavController().popBackStack()
        }
        textKhong.setOnClickListener {
            dialog.dismiss()
        }
    }


    // https://openexchangerates.org/api/currencies.json
    override fun onDestroy() {
        Log.e("data", "currency: onDestroy")
        dataViewMode.checkInputScreenCurrency = 0
        super.onDestroy()
    }
}