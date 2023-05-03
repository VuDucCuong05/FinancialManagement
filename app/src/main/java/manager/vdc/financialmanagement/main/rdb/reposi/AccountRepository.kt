package manager.vdc.financialmanagement.main.rdb.reposi

import androidx.lifecycle.LiveData
import manager.vdc.financialmanagement.main.model.m_r.Account
import manager.vdc.financialmanagement.main.rdb.inter.AccountDao

class AccountRepository(private val accountDao: AccountDao) {
    val allAccountsLive: LiveData<List<Account>> = accountDao.getAllAccountsLive()
    val allMoneyAccounts: List<Account> = accountDao.getAllAccounts()

    suspend fun insert(account: Account) {
        accountDao.insert(account)
    }

    suspend fun delete(account: Account) {
        accountDao.delete(account)
    }

    suspend fun update(account: Account) {
        accountDao.update(account)
    }

    fun getAccountBySelect(): Account {
        return accountDao.getAccountBySelect()
    }
    fun getAccountByEmail(email: String): Account {
        return accountDao.getAccountByEmail(email)
    }
}
