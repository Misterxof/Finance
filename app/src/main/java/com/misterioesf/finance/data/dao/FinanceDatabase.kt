package com.misterioesf.finance.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer

@Database(entities = [Transfer::class, Account::class], version = 2)
@TypeConverters(DaoTypeConverters::class)
abstract class FinanceDatabase: RoomDatabase() {
    abstract fun getDao(): FinanceDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Transfer ADD COLUMN is_bill INTEGER NOT NULL DEFAULT(0)")
    }
}