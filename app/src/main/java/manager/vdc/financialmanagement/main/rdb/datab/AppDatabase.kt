package manager.vdc.financialmanagement.main.rdb.datab

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import manager.vdc.financialmanagement.main.model.m_r.*
import manager.vdc.financialmanagement.main.rdb.inter.*

@Database(entities = [Country::class, Account::class, MoneyAccount::class, Category::class, Transaction::class, NotificationInfo::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "app_database5.db"
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun moneyAccountDao(): MoneyAccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun countryDao(): CountryDao
    abstract fun accountDao(): AccountDao
    abstract fun notificationInfoDao(): NotificationInfoDao


}
