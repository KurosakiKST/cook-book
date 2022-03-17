package com.kyawsithu.cookbook.utils

object Constants
{
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    fun dishType():ArrayList<String>{
        var list = ArrayList<String>()
        list.add("breakfast")
        list.add("lunch")
        list.add("supper")
        list.add("dinner")
        list.add("salad")
        list.add("snack")
        list.add("side dish")
        list.add("other")
        return list
    }

    fun dishCategory():ArrayList<String>{
        var list = ArrayList<String>()
        list.add("curry")
        list.add("soup")
        list.add("burger")
        list.add("pizza")
        list.add("wrap")
        list.add("snack")
        list.add("sandwich")
        list.add("dessert")
        list.add("drinks")
        list.add("cake")
        return list
    }

    fun dishCookingTime():ArrayList<String>{
        var list = ArrayList<String>()
        list.add("10")
        list.add("20")
        list.add("30")
        list.add("40")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("180")
        return list
    }
}