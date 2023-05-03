package manager.vdc.financialmanagement.main.model.m_convert


data class TransactionsShowDetails(
    var day: String,
    var transactions: List<TransactionWithFullDetails>,
)
