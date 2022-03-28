package com.kyawsithu.cookbook.utils

object Constants
{
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
    const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

    const val EXTRA_DISH_DETAILS: String = "DishDetails"

    const val ALL_ITEMS: String = "All"
    const val FILTER_SELECTION: String = "FilterSelection"

    const val API_ENDPOINT: String = "recipes/random"

    const val API_KEY: String = "apiKey"
    const val LIMIT_LICENSE: String = "limitLicense"
    const val TAGS: String = "tags"
    const val NUMBER: String = "number"

    const val BASE_URL: String = "https:api.spoonacular.com"
    
    const val API_KEY_VALUE: String = "9a18922c75a2403eaf296a389b751051"
    const val LIMIT_LICENSE_VALUE: Boolean = true
    const val TAGS_VALUE: String = "vegetarian, dessert"
    const val NUMBER_VALUE: Int = 1

    const val NOTIFICATION_ID: String = "CookBook_notification_id"
    const val NOTIFICATION_NAME: String = "CookBook"
    const val NOTIFICATION_CHANNEL: String = "CookBook_channel_01"


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