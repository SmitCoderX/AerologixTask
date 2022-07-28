package com.smitcoderx.task.aerologixtask.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smitcoderx.task.aerologixtask.Model.Data
import com.smitcoderx.task.aerologixtask.Utils.Convertor

@Database(
    entities = [Data::class],
    version = 2
)
@TypeConverters(Convertor::class)
abstract class AerologixDatabase : RoomDatabase() {

    abstract fun getAerologixDb(): AerologixDao

    companion object {

        @Volatile
        private var instance: AerologixDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AerologixDatabase::class.java,
                "AerologixDatabase.db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}