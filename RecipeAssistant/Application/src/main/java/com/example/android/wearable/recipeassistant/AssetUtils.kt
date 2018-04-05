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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.io.InputStream

internal object AssetUtils {
    private val TAG = "RecipeAssistant"

    fun loadAsset(context: Context, asset: String): ByteArray? {
        var buffer: ByteArray? = null
        try {
            val `is` = context.assets.open(asset)
            val size = `is`.available()
            buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
        } catch (e: IOException) {
            Log.e(TAG, "Failed to load asset $asset: $e")
        }

        return buffer
    }

    fun loadJSONAsset(context: Context, asset: String): JSONObject? {
        val jsonString = String(loadAsset(context, asset)!!)
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(jsonString)
        } catch (e: JSONException) {
            Log.e(TAG, "Failed to parse JSON asset $asset: $e")
        }

        return jsonObject
    }

    fun loadBitmapAsset(context: Context, asset: String): Bitmap? {
        var `is`: InputStream? = null
        var bitmap: Bitmap? = null
        try {
            `is` = context.assets.open(asset)
            if (`is` != null) {
                bitmap = BitmapFactory.decodeStream(`is`)
            }
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        } finally {
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Cannot close InputStream: ", e)
                }

            }
        }
        return bitmap
    }
}
