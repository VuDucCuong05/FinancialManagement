package manager.vdc.financialmanagement.main.inf

import manager.vdc.financialmanagement.main.model.m_r.Account


interface MyCallback {
    fun onCallback()
    fun onCallbackLockedDrawers()
    fun onCallbackUnLockedDrawers()
    fun onCallbackAccount(account: Account)
}