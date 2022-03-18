package com.kyawsithu.cookbook.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.kyawsithu.cookbook.model.entites.CookBook

@Dao
interface CookBookDao {

    @Insert
    suspend fun insertCookBookDetails(cookBook: CookBook)


}