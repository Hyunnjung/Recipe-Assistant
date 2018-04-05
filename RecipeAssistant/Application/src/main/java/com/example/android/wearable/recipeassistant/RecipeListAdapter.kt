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
import android.database.DataSetObserver
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

class RecipeListAdapter(private val mContext: Context) : ListAdapter {
    private val TAG = "RecipeListAdapter"

    private val mItems = ArrayList<Item>()
    private var mObserver: DataSetObserver? = null

    private inner class Item {
        internal var title: String? = null
        internal var name: String? = null
        internal var summary: String? = null
        internal var image: Bitmap? = null
    }

    init {
        loadRecipeList()
    }

    private fun loadRecipeList() {
        val jsonObject = AssetUtils.loadJSONAsset(mContext, Constants.RECIPE_LIST_FILE)
        if (jsonObject != null) {
            val items = parseJson(jsonObject)
            appendItemsToList(items)
        }
    }

    private fun parseJson(json: JSONObject): List<Item> {
        val result = ArrayList<Item>()
        try {
            val items = json.getJSONArray(Constants.RECIPE_FIELD_LIST)
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val parsed = Item()
                parsed.name = item.getString(Constants.RECIPE_FIELD_NAME)
                parsed.title = item.getString(Constants.RECIPE_FIELD_TITLE)
                if (item.has(Constants.RECIPE_FIELD_IMAGE)) {
                    val imageFile = item.getString(Constants.RECIPE_FIELD_IMAGE)
                    parsed.image = AssetUtils.loadBitmapAsset(mContext, imageFile)
                }
                parsed.summary = item.getString(Constants.RECIPE_FIELD_SUMMARY)
                result.add(parsed)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Failed to parse recipe list: $e")
        }

        return result
    }

    private fun appendItemsToList(items: List<Item>) {
        mItems.addAll(items)
        if (mObserver != null) {
            mObserver!!.onChanged()
        }
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            val inf = LayoutInflater.from(mContext)
            view = inf.inflate(R.layout.list_item, null)
        }
        val item = getItem(position) as Item
        val titleView = view!!.findViewById<View>(R.id.textTitle) as TextView
        val summaryView = view.findViewById<View>(R.id.textSummary) as TextView
        val iv = view.findViewById<View>(R.id.imageView) as ImageView

        titleView.text = item.title
        summaryView.text = item.summary
        if (item.image != null) {
            iv.setImageBitmap(item.image)
        } else {
            iv.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_noimage))
        }
        return view
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isEmpty(): Boolean {
        return mItems.isEmpty()
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        mObserver = observer
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        mObserver = null
    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    fun getItemName(position: Int): String? {
        return mItems[position].name
    }
}
