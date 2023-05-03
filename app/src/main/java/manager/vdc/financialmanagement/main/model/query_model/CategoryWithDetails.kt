package manager.vdc.financialmanagement.main.model.query_model

import androidx.room.Embedded
import androidx.room.Relation
import manager.vdc.financialmanagement.main.model.m_r.Account
import manager.vdc.financialmanagement.main.model.m_r.Category

data class CategoryWithDetails(
    @Embedded val category: Category?=null,
    @Relation(parentColumn = "idAccount", entityColumn = "idAccount",entity = Account::class) val account: Account?=null
)
