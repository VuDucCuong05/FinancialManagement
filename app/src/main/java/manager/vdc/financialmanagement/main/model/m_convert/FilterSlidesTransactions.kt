package manager.vdc.financialmanagement.main.model.m_convert

data class FilterSlidesTransactions(
    var time:String,
    var transaction: TransactionWithFullDetails,
    var listTransaction: List<TransactionWithFullDetails>
)
