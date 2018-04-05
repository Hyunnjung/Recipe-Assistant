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

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import org.json.JSONObject

class RecipeActivity : Activity() {
    private var mRecipeName: String? = null
    private var mRecipe: Recipe? = null
    private var mImageView: ImageView? = null
    private var mTitleTextView: TextView? = null
    private var mSummaryTextView: TextView? = null
    private var mIngredientsTextView: TextView? = null
    private var mStepsLayout: LinearLayout? = null

    override fun onStart() {
        super.onStart()
        val intent = intent
        mRecipeName = intent.getStringExtra(Constants.RECIPE_NAME_TO_LOAD)
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Intent: " + intent.toString() + " " + mRecipeName)
        }
        loadRecipe()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe)
        mTitleTextView = findViewById<View>(R.id.recipeTextTitle) as TextView
        mSummaryTextView = findViewById<View>(R.id.recipeTextSummary) as TextView
        mImageView = findViewById<View>(R.id.recipeImageView) as ImageView
        mIngredientsTextView = findViewById<View>(R.id.textIngredients) as TextView
        mStepsLayout = findViewById<View>(R.id.layoutSteps) as LinearLayout
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cook -> {
                startCooking()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadRecipe() {
        val jsonObject = AssetUtils.loadJSONAsset(this, mRecipeName)
        if (jsonObject != null) {
            mRecipe = Recipe.fromJson(this, jsonObject)
            if (mRecipe != null) {
                displayRecipe(mRecipe!!)
            }
        }
    }

    private fun displayRecipe(recipe: Recipe) {
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        mTitleTextView!!.animation = fadeIn
        mTitleTextView!!.text = recipe.titleText
        mSummaryTextView!!.text = recipe.summaryText
        if (recipe.recipeImage != null) {
            mImageView!!.animation = fadeIn
            val recipeImage = AssetUtils.loadBitmapAsset(this, recipe.recipeImage)
            mImageView!!.setImageBitmap(recipeImage)
        }
        mIngredientsTextView!!.text = recipe.ingredientsText

        findViewById<View>(R.id.ingredientsHeader).animation = fadeIn
        findViewById<View>(R.id.ingredientsHeader).visibility = View.VISIBLE
        findViewById<View>(R.id.stepsHeader).animation = fadeIn

        findViewById<View>(R.id.stepsHeader).visibility = View.VISIBLE

        val inf = LayoutInflater.from(this)
        mStepsLayout!!.removeAllViews()
        var stepNumber = 1
        for (step in recipe.recipeSteps!!) {
            val view = inf.inflate(R.layout.step_item, null)
            val iv = view.findViewById<View>(R.id.stepImageView) as ImageView
            if (step.stepImage == null) {
                iv.visibility = View.GONE
            } else {
                val stepImage = AssetUtils.loadBitmapAsset(this, step.stepImage)
                iv.setImageBitmap(stepImage)
            }
            (view.findViewById<View>(R.id.textStep) as TextView).text = stepNumber++.toString() + ". " + step.stepText
            mStepsLayout!!.addView(view)
        }
    }

    private fun startCooking() {
        val intent = Intent(this, RecipeService::class.java)
        intent.action = Constants.ACTION_START_COOKING
        intent.putExtra(Constants.EXTRA_RECIPE, mRecipe!!.toBundle())
        startService(intent)
    }

    companion object {
        private val TAG = "RecipeAssistant"
    }
}
