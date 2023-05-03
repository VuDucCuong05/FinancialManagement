package manager.vdc.financialmanagement.main.inf

import manager.vdc.financialmanagement.main.model.m_convert.TransactionWithFullDetails

interface TransactionClickListener {
    fun onTransactionClick(transactionWithFullDetails: TransactionWithFullDetails)
}
