package com.kyawsithu.cookbook.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kyawsithu.cookbook.model.entities.CookBook
import kotlinx.coroutines.flow.Flow

@Dao
interface CookBookDao {

    @Insert
    suspend fun insertCookBookDetails(cookBook: CookBook)

    @Query("SELECT * FROM COOK_BOOK_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<CookBook>>

    @Update
    suspend fun updateCookBookDetails(cookBook: CookBook)

    @Query("SELECT * FROM COOK_BOOK_TABLE WHERE favourite_dish = 1")
    fun getAllFavouriteDishesList(): Flow<List<CookBook>>

}