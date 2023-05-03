package manager.vdc.financialmanagement.main.model.m_convert

data class FilterTransactions(
    var transaction: TransactionWithFullDetails,
    var listTransaction: List<TransactionWithFullDetails>
)
