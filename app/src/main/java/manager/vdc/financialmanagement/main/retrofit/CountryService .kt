package manager.vdc.financialmanagement.main.retrofit

import manager.vdc.financialmanagement.main.model.m_api.CountryResponse
import retrofit2.http.GET

interface CountryService {
    @GET("v2/all")
    suspend fun getAllCountries(): List<CountryResponse>
}
