package com.kyawsithu.cookbook.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kyawsithu.cookbook.model.entities.CookBook

@Database(entities = [CookBook::class], version = 1)
abstract class CookBookRoomDatabase : RoomDatabase()
{

    abstract fun cookBookDao(): CookBookDao

    companion object
    {
        @Volatile
        private var INSTANCE : CookBookRoomDatabase? = null

        fun getDatabase(context : Context) : CookBookRoomDatabase
        {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CookBookRoomDatabase::class.java,
                    "cook_book_database")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance

                //return instance
                instance
            }
        }
    }

}