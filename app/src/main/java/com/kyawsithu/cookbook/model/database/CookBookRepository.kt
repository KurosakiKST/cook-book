package com.kyawsithu.cookbook.model.database

import androidx.annotation.WorkerThread
import com.kyawsithu.cookbook.model.entities.CookBook
import kotlinx.coroutines.flow.Flow

class CookBookRepository(private val cookBookDao : CookBookDao)
{
    @WorkerThread
    suspend fun insertCookBookData(cookBook : CookBook){
        cookBookDao.insertCookBookDetails(cookBook)
    }

    val allDishesList: Flow<List<CookBook>> = cookBookDao.getAllDishesList()
}