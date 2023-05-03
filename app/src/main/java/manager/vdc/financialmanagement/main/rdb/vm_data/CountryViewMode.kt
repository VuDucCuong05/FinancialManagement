package manager.vdc.financialmanagement.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import manager.vdc.financialmanagement.main.model.m_r.Country
import manager.vdc.financialmanagement.main.rdb.datab.AppDatabase
import manager.vdc.financialmanagement.main.rdb.reposi.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var countryDao = db.countryDao()

    private val repository: CountryRepository = CountryRepository(countryDao)

    fun getAccountById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAccountById(it)
        }
    }

    var country = Country(0)
}


