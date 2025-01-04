package com.example.savory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: RecipeDBHandler
    private lateinit var recipesAdapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = RecipeDBHandler(this)
        if (db.readAll().isEmpty()){
            Log.d("MainActivity", "List Empty")
            Toast.makeText(this, "Empty Recipes", Toast.LENGTH_SHORT).show()
        }
        recipesAdapter = RecipesAdapter(db.readAll(), this)

        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.recipesRecyclerView.adapter = recipesAdapter

        binding.addBtn.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        recipesAdapter.refreshData(db.readAll())
    }


}