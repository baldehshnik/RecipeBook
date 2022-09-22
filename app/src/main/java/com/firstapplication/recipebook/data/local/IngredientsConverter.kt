package com.firstapplication.recipebook.data.local

import androidx.room.TypeConverter

class IngredientsConverter {

    @TypeConverter
    fun fromIngredients(map: Map<String, String>): String {
        val keysString = map.keys.joinToString(", ")
        val valuesString = map.values.joinToString(", ")
        return "$keysString|$valuesString"
    }

    @TypeConverter
    fun toIngredients(string: String): Map<String, String> {
        val keysAndValues: List<String> = string.split("|")
        val keysArray = keysAndValues[0].split(", ")
        val valuesArray = keysAndValues[1].split(", ")

        val map = mutableMapOf<String, String>()

        val size = keysArray.size - 1
        for (i in 0..size)
            map[keysArray[i]] = valuesArray[i]

        return map
    }

}