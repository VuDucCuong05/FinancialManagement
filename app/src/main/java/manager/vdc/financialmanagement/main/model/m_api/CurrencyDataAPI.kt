package manager.vdc.financialmanagement.main.model.m_api

class CurrencyDataAPI (
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val source: String,
    val quotes: Map<String, Double>
)
