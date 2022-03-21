package com.kyawsithu.cookbook.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "cook_book_table")
data class CookBook(
        @ColumnInfo val image : String,
        @ColumnInfo val imageSource : String,
        @ColumnInfo val title : String,
        @ColumnInfo val type : String,
        @ColumnInfo val category : String,
        @ColumnInfo val ingredients : String,
                   )