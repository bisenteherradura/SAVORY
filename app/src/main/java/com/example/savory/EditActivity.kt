package com.example.savory

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.savory.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var db: RecipeDBHandler
    private var recipeId: Int = -1

    private lateinit var doneBtn: ImageButton
    private lateinit var ingredients: EditText
    private lateinit var instructions: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = RecipeDBHandler(this)

        recipeId = intent.getIntExtra("recipe_id", -1)
        if(recipeId == -1){
            finish()
            return
        }

        val note = db.getRecipeById(recipeId)
        binding.nameEditText.setText(note.name)
        binding.ingredientsEditText.setText(note.ingredients)
        binding.instructionsEditText.setText(note.instructions)

        binding.doneBtn.setOnClickListener {
            val newName = binding.nameEditText.text.toString()
            val newIngredients = binding.ingredientsEditText.text.toString()
            val newInstructions = binding.instructionsEditText.text.toString()
            val updatedRecipe = Recipe(newName, newIngredients, newInstructions)
            updatedRecipe.id = recipeId

            if(db.updateRecipe(updatedRecipe)) {
                finish()
                Toast.makeText(this, "Recipe Saved", Toast.LENGTH_SHORT).show()
            } else {
                finish()
                Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show()
            }
        }

        ingredients = findViewById(R.id.ingredientsEditText)
        ingredients.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable) {
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            }

            override fun onTextChanged(
                text: CharSequence,
                start: Int,
                lengthBefore: Int,
                lengthAfter: Int
            ) {
                var text = text
                if (lengthAfter > lengthBefore) {
                    if (text.toString().length == 1) {
                        text = "• $text"
                        ingredients.setText(text)
                        ingredients.setSelection(ingredients.getText().length)
                    }
                    if (text.toString().endsWith("\n")) {
                        text = text.toString().replace("\n", "\n• ")
                        text = text.toString().replace("• •", "•")
                        ingredients.setText(text)
                        ingredients.setSelection(ingredients.getText().length)
                    }
                }
            }
        })

        instructions = findViewById(R.id.instructionsEditText)
        instructions.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable) {
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
            }

            override fun onTextChanged(
                text: CharSequence,
                start: Int,
                lengthBefore: Int,
                lengthAfter: Int
            ) {
                var text = text
                if (lengthAfter > lengthBefore) {
                    if (text.toString().length == 1) {
                        text = "➢ $text"
                        instructions.setText(text)
                        instructions.setSelection(instructions.getText().length)
                    }
                    if (text.toString().endsWith("\n")) {
                        text = text.toString().replace("\n", "\n➢ ")
                        text = text.toString().replace("➢ ➢", "➢")
                        instructions.setText(text)
                        instructions.setSelection(instructions.getText().length)
                    }
                }
            }
        })
    }
}