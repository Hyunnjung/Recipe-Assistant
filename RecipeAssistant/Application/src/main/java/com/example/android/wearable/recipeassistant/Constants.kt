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

object Constants {
    val RECIPE_LIST_FILE = "recipelist.json"
    val RECIPE_NAME_TO_LOAD = "recipe_name"

    val RECIPE_FIELD_LIST = "recipe_list"
    val RECIPE_FIELD_IMAGE = "img"
    val RECIPE_FIELD_INGREDIENTS = "ingredients"
    val RECIPE_FIELD_NAME = "name"
    val RECIPE_FIELD_SUMMARY = "summary"
    val RECIPE_FIELD_STEPS = "steps"
    val RECIPE_FIELD_TEXT = "text"
    val RECIPE_FIELD_TITLE = "title"
    val RECIPE_FIELD_STEP_TEXT = "step_text"
    val RECIPE_FIELD_STEP_IMAGE = "step_image"

    internal val ACTION_START_COOKING = "com.example.android.wearable.recipeassistant.START_COOKING"
    val EXTRA_RECIPE = "recipe"

    val NOTIFICATION_ID = 0
    val NOTIFICATION_IMAGE_WIDTH = 280
    val NOTIFICATION_IMAGE_HEIGHT = 280
}
