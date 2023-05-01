package com.example.qltaichinhcanhan.main.model.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount

data class MoneyAccountWithDetails(
    @Embedded val moneyAccount: MoneyAccount?=null,
    @Relation(parentColumn = "idCountry", entityColumn = "idCountry",entity = Country::class) var country: Country?=null,
    @Relation(parentColumn = "idAccount", entityColumn = "idAccount",entity = Account::class) val account: Account?=null
)
