package com.example.savory

class Recipe {
    var id: Int = 0
    var name: String = " "
    var ingredients: String = " "
    var instructions: String = " "

    constructor(name: String, ingredients: String, instructions: String) {
        this.name = name
        this.ingredients = ingredients
        this.instructions = instructions
    }
}