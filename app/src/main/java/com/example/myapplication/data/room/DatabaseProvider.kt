

package com.example.myapplication.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection

object DatabaseProvider {
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.prepare("ALTER TABLE locations ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0").use { it.step() }
        }
    }


    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {

            val instance = INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "rick_morty_database"
            ).addMigrations(MIGRATION_1_2).build()

            INSTANCE = instance
            instance
        }
    }
}
