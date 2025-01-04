package com.example.savory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class AddActivity : AppCompatActivity() {


    private lateinit var name: EditText
    private lateinit var ingredients: EditText
    private lateinit var instructions: EditText
    private lateinit var doneBtn: ImageButton
    private lateinit var btnAdd: Button
    private lateinit var imgView: ImageView
    private lateinit var photoPickerLauncher: ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        doneBtn = findViewById(R.id.doneBtn)
        name = findViewById(R.id.title)
        ingredients = findViewById(R.id.ingredientsEditText)
        instructions = findViewById(R.id.instructionsEditText)

        doneBtn.setOnClickListener {

            Log.d("AddActivity", "Insert Button clicked")

            val name = name.text.toString()
            val ingredients = ingredients.text.toString()
            val instructions = instructions.text.toString()

            if (name.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()){
                try {
                    val recipe = Recipe(name, ingredients, instructions)
                    val db = RecipeDBHandler(this)

                    Log.d("AddActivity", "Inserting data into database")

                    val success = db.insertRecipe(recipe)
                    if (success){
                        Log.d("AddActivity", "Data inserted successfully")
                        Toast.makeText(this, "Recipe Created", Toast.LENGTH_SHORT).show()
                    }else{
                        Log.e("AddActivity","Data insertion failed")
                        Toast.makeText(this, "Creation Failed", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception){
                    Log.e("AddActivity", "An error occurred", e)
                    Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.w("AddActivity", "either of three input is empty")
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }



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
        btnAdd = findViewById(R.id.addbtn)
        imgView = findViewById(R.id.imageView)

        // Register the launcher
        photoPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val imageUri: Uri? = data?.data
                imgView.setImageURI(imageUri)
            }
        }

        // Set click listener for the button
        btnAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            photoPickerLauncher.launch(intent)
        }
    }
}