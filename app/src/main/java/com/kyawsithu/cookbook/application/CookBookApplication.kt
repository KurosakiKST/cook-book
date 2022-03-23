package com.kyawsithu.cookbook.application

import android.app.Application
import com.kyawsithu.cookbook.model.database.CookBookRepository
import com.kyawsithu.cookbook.model.database.CookBookRoomDatabase

class CookBookApplication : Application()
{
    private val database by lazy { CookBookRoomDatabase.getDatabase(this@CookBookApplication) }

    val repository by lazy { CookBookRepository(database.cookBookDao()) }


}