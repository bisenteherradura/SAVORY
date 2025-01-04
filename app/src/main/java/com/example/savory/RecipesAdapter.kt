package com.example.savory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecipesAdapter(private var recipes: List<Recipe>, context: Context) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    private val db: RecipeDBHandler = RecipeDBHandler(context)

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        //val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val updateBtn:ImageView = itemView.findViewById(R.id.updateBtn)
        val deleteBtn:ImageView = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.nameTextView.text = recipe.name
        //holder.descriptionTextView.text = note.description

        //update
        holder.updateBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditActivity::class.java).apply {
                putExtra("recipe_id", recipe.id)
            }
            holder.itemView.context.startActivity(intent)
        }



        holder.deleteBtn.setOnClickListener{
            if(db.deleteRecipe(recipe.id)) {
                refreshData(db.readAll())
                Toast.makeText(holder.itemView.context, "Recipe Deleted", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(holder.itemView.context, "Delete Failed", Toast.LENGTH_SHORT).show()
            }
        }



    }

    fun refreshData(newNotes: List<Recipe>){
        recipes = newNotes
        notifyDataSetChanged()
    }

}