package manager.vdc.financialmanagement.main.model.m_convert

import manager.vdc.financialmanagement.main.model.query_model.MoneyAccountWithDetails
import manager.vdc.financialmanagement.main.model.query_model.TransactionWithDetails

data class TransactionWithFullDetails(
    var transactionWithDetails: TransactionWithDetails? = null,
    var moneyAccountWithDetails: MoneyAccountWithDetails? = null,
)

