package com.example.savory

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

const val DATABASE_NAME = "MyDB"
const val TABLE_NAME = "Recipes"
const val COL_NAME = "name"
const val COL_INGREDIENTS = "ingredients"
const val COL_INSTRUCTIONS = "instructions"
const val COL_ID = "id"

class RecipeDBHandler (private var context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE Recipes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            ingredients TEXT NOT NULL,
            instructions TEXT NOT NULL
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertRecipe(recipe: Recipe): Boolean {
        return try {
            val db = this.writableDatabase
            db.beginTransaction()
            val contentValues = ContentValues().apply {
                put(COL_NAME, recipe.name)
                put(COL_INGREDIENTS, recipe.ingredients)
                put(COL_INSTRUCTIONS, recipe.instructions)
            }
            val result = db.insert(TABLE_NAME, null, contentValues)
            if (result != -1L) {
                db.setTransactionSuccessful()
            }
            db.endTransaction()
            db.close()
            result != -1L
        } catch (e:Exception) {
            Log.e("DataBaseHandler", "Error during transaction", e)
            false
        }
    }

    fun readAll(): List<Recipe> {
        return try {
            val list = mutableListOf<Recipe>()

            val db = this.readableDatabase
            val query = "SELECT * FROM $TABLE_NAME"
            val result = db.rawQuery(query, null)

            if (result.moveToFirst()) {
                do {
                    val id = result.getInt(result.getColumnIndexOrThrow(COL_ID))
                    val name = result.getString(result.getColumnIndexOrThrow(COL_NAME))
                    val ingredients = result.getString(result.getColumnIndexOrThrow(COL_INGREDIENTS))
                    val instructions = result.getString(result.getColumnIndexOrThrow(
                        COL_INSTRUCTIONS))

                    val recipe = Recipe(name, ingredients, instructions)
                    recipe.id = id  // result.getString(0).toInt()
                    list.add(recipe)
                } while (result.moveToNext())
            }
            Log.i("DataBaseHandler", "Read but empty")
            result.close()
            db.close()
            list
        } catch (e:Exception) {
            Log.e("DataBaseHandler", "Can't read", e)
            val list = mutableListOf<Recipe>()
            list
        }
    }

    fun deleteRecipe(recipeId: Int): Boolean {
        return try {
            val db = writableDatabase
            val whereClause = "$COL_ID = ?"
            val whereArgs = arrayOf(recipeId.toString())
            db.delete(TABLE_NAME, whereClause, whereArgs)
            db.close()
            true
        } catch (e:Exception) {
            Log.e("DataBaseHandler", "Delete failed", e)
            false
        }
    }

    fun updateRecipe(recipe: Recipe): Boolean{
        return try {
            val db = writableDatabase
            val value = ContentValues().apply {
                put(COL_NAME, recipe.name)
                put(COL_INGREDIENTS, recipe.ingredients)
                put(COL_INSTRUCTIONS, recipe.instructions)
            }
            val whereClause = "$COL_ID = ?"
            val whereArgs = arrayOf(recipe.id.toString())
            db.update(TABLE_NAME, value, whereClause, whereArgs)
            db.close()
            true
        } catch (e:Exception) {
            Log.e("DataBaseHandler", "Update failed", e)
            false
        }
    }

    fun getRecipeById(recipeId: Int): Recipe {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = $recipeId"
        val result = db.rawQuery(query, null)
        result.moveToFirst()

        val id = result.getInt(result.getColumnIndexOrThrow(COL_ID))
        val name = result.getString(result.getColumnIndexOrThrow(COL_NAME))
        val ingredients = result.getString(result.getColumnIndexOrThrow(COL_INGREDIENTS))
        val instructions = result.getString(result.getColumnIndexOrThrow(COL_INSTRUCTIONS))

        result.close()
        db.close()
        val recipe = Recipe(name, ingredients, instructions)
        recipe.id = id
        return recipe
    }

}



