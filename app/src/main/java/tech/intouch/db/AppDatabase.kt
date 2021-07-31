package tech.intouch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.intouch.models.User

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun accountDao(): AccountDao

    companion object {
        private var dbInstance: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (dbInstance == null) {
                dbInstance = Room
                    .databaseBuilder(context, AppDatabase::class.java, "in-touch-db")
                    .allowMainThreadQueries()
                    .build()
            }
            return dbInstance!!
        }
    }
}