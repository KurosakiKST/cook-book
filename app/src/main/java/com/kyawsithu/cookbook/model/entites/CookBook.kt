package com.kyawsithu.cookbook.model.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cook_book_table")
data class CookBook(
        @ColumnInfo val image : String,
        @ColumnInfo val imageSource : String,
        @ColumnInfo val title : String,
        @ColumnInfo val type : String,
        @ColumnInfo val category : String,
        @ColumnInfo val ingredients : String,

        @ColumnInfo(name = "cooking_time") val cookingTime: String,
        @ColumnInfo(name = "instructions") val directionToCook: String,
        @ColumnInfo(name = "favourite_dish") val favouriteDish: Boolean = false,
        @PrimaryKey(autoGenerate = true) val id: Int = 0
        )