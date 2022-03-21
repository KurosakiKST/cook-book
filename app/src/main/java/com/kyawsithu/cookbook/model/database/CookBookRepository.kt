package com.kyawsithu.cookbook.model.database

import androidx.annotation.WorkerThread
import com.kyawsithu.cookbook.model.entites.CookBook

class CookBookRepository(private val cookBookDao : CookBookDao)
{
    @WorkerThread
    suspend fun insertCookBookData(cookBook : CookBook){
        cookBookDao.insertCookBookDetails(cookBook)
    }
}