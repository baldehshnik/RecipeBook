package com.firstapplication.recipebook.data.interfaces

import com.firstapplication.recipebook.sealed.Response

interface DatabaseHelper {
    suspend fun readAll(): Response
}