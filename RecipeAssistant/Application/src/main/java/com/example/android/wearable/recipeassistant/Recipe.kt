/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.wearable.recipeassistant

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class Recipe {

    var titleText: String? = null
    var summaryText: String? = null
    var recipeImage: String? = null
    var ingredientsText: String? = null
    internal var recipeSteps: ArrayList<RecipeStep>? = null

    class RecipeStep internal constructor() {
        var stepImage: String? = null
        var stepText: String? = null

        fun toBundle(): Bundle {
            val bundle = Bundle()
            bundle.putString(Constants.RECIPE_FIELD_STEP_TEXT, stepText)
            bundle.putString(Constants.RECIPE_FIELD_STEP_IMAGE, stepImage)
            return bundle
        }

        companion object {

            fun fromBundle(bundle: Bundle): RecipeStep {
                val recipeStep = RecipeStep()
                recipeStep.stepText = bundle.getString(Constants.RECIPE_FIELD_STEP_TEXT)
                recipeStep.stepImage = bundle.getString(Constants.RECIPE_FIELD_STEP_IMAGE)
                return recipeStep
            }
        }
    }

    init {
        recipeSteps = ArrayList()
    }

    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(Constants.RECIPE_FIELD_TITLE, titleText)
        bundle.putString(Constants.RECIPE_FIELD_SUMMARY, summaryText)
        bundle.putString(Constants.RECIPE_FIELD_IMAGE, recipeImage)
        bundle.putString(Constants.RECIPE_FIELD_INGREDIENTS, ingredientsText)
        if (recipeSteps != null) {
            val stepBundles = ArrayList<Parcelable>(recipeSteps!!.size)
            for (recipeStep in recipeSteps!!) {
                stepBundles.add(recipeStep.toBundle())
            }
            bundle.putParcelableArrayList(Constants.RECIPE_FIELD_STEPS, stepBundles)
        }
        return bundle
    }

    companion object {
        private val TAG = "RecipeAssistant"

        fun fromJson(context: Context, json: JSONObject): Recipe? {
            val recipe = Recipe()
            try {
                recipe.titleText = json.getString(Constants.RECIPE_FIELD_TITLE)
                recipe.summaryText = json.getString(Constants.RECIPE_FIELD_SUMMARY)
                if (json.has(Constants.RECIPE_FIELD_IMAGE)) {
                    recipe.recipeImage = json.getString(Constants.RECIPE_FIELD_IMAGE)
                }
                val ingredients = json.getJSONArray(Constants.RECIPE_FIELD_INGREDIENTS)
                recipe.ingredientsText = ""
                for (i in 0 until ingredients.length()) {
                    recipe.ingredientsText += (" - "
                            + ingredients.getJSONObject(i).getString(Constants.RECIPE_FIELD_TEXT) + "\n")
                }

                val steps = json.getJSONArray(Constants.RECIPE_FIELD_STEPS)
                for (i in 0 until steps.length()) {
                    val step = steps.getJSONObject(i)
                    val recipeStep = RecipeStep()
                    recipeStep.stepText = step.getString(Constants.RECIPE_FIELD_TEXT)
                    if (step.has(Constants.RECIPE_FIELD_IMAGE)) {
                        recipeStep.stepImage = step.getString(Constants.RECIPE_FIELD_IMAGE)
                    }
                    recipe.recipeSteps!!.add(recipeStep)
                }
            } catch (e: JSONException) {
                Log.e(TAG, "Error loading recipe: $e")
                return null
            }

            return recipe
        }

        fun fromBundle(bundle: Bundle): Recipe {
            val recipe = Recipe()
            recipe.titleText = bundle.getString(Constants.RECIPE_FIELD_TITLE)
            recipe.summaryText = bundle.getString(Constants.RECIPE_FIELD_SUMMARY)
            recipe.recipeImage = bundle.getString(Constants.RECIPE_FIELD_IMAGE)
            recipe.ingredientsText = bundle.getString(Constants.RECIPE_FIELD_INGREDIENTS)
            val stepBundles = bundle.getParcelableArrayList<Parcelable>(Constants.RECIPE_FIELD_STEPS)
            if (stepBundles != null) {
                for (stepBundle in stepBundles) {
                    recipe.recipeSteps!!.add(RecipeStep.fromBundle(stepBundle as Bundle))
                }
            }
            return recipe
        }
    }
}
