package com.firstapplication.recipebook.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.firstapplication.recipebook.R
import com.firstapplication.recipebook.data.interfaces.DatabaseHelper
import com.firstapplication.recipebook.data.models.DishCategoryEntity
import com.firstapplication.recipebook.di.IODispatcher
import com.firstapplication.recipebook.sealed.CorrectResponse
import com.firstapplication.recipebook.sealed.ErrorResponse
import com.firstapplication.recipebook.sealed.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

class DatabaseCreationError : Exception()
class DatabaseOpenError : Exception()

@Singleton
class DatabaseHelperImpl @Inject constructor(
    private val context: Context,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), DatabaseHelper {

    private val databasePath = context.getDatabasePath(DB_NAME).toString()
    private var database: SQLiteDatabase? = null

    private fun createDatabase() {
        if (checkDatabase()) return
        try {
            copyDatabase()
        } catch (e: Exception) {
            throw DatabaseCreationError()
        }
    }

    private fun checkDatabase(): Boolean {
        val db: SQLiteDatabase?
        try {
            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: Exception) {
            return false
        }
        db?.close()
        return db != null
    }

    private fun copyDatabase() {
        val inputStream: InputStream = context.assets.open(DB_NAME)
        val outputStream: OutputStream = FileOutputStream(databasePath)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        inputStream.close()
        outputStream.flush()
        outputStream.close()
    }

    private fun openDatabase() {
        try {
            database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: Exception) {
            throw DatabaseOpenError()
        }
    }

    @SuppressLint("Recycle")
    override suspend fun readAll(): Response = withContext(ioDispatcher) {
        try {
            createDatabase()
            openDatabase()

            val list = mutableListOf<DishCategoryEntity>()
            val query = "SELECT * FROM category"
            val cursor = database?.rawQuery(query, null)

            if (cursor?.moveToFirst() == true) {
                do {
                    list.add(
                        DishCategoryEntity(
                            cursor.getString(0).toInt(),
                            cursor.getString(1),
                            cursor.getString(2)
                        )
                    )
                } while (cursor.moveToNext())
            }

            return@withContext CorrectResponse(list)
        } catch (e: DatabaseOpenError) {
            return@withContext ErrorResponse(R.string.failed_open_db)
        } catch (e: DatabaseCreationError) {
            return@withContext ErrorResponse(R.string.failed_create_db)
        } catch (e: Exception) {
            return@withContext ErrorResponse(R.string.error)
        } finally {
            close()
        }
    }

    @Synchronized
    override fun close() {
        database?.close()
        super.close()
    }

    override fun onCreate(p0: SQLiteDatabase?) {}
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    companion object {
        private const val DB_NAME = "dish_category_db.db"
        private const val DB_VERSION = 1
    }
}