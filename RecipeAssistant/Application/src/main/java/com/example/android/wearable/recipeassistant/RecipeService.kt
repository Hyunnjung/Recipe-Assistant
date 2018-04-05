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

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.NotificationCompat

import java.util.ArrayList

class RecipeService : Service() {
    private var mNotificationManager: NotificationManagerCompat? = null
    private val mBinder = LocalBinder()
    private var mRecipe: Recipe? = null

    inner class LocalBinder : Binder() {
        internal val service: RecipeService
            get() = this@RecipeService
    }

    override fun onCreate() {
        mNotificationManager = NotificationManagerCompat.from(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == Constants.ACTION_START_COOKING) {
            createNotification(intent)
            return Service.START_STICKY
        }
        return Service.START_NOT_STICKY
    }

    private fun createNotification(intent: Intent) {
        mRecipe = Recipe.fromBundle(intent.getBundleExtra(Constants.EXTRA_RECIPE))
        val notificationPages = ArrayList<Notification>()

        val stepCount = mRecipe!!.recipeSteps!!.size

        for (i in 0 until stepCount) {
            val recipeStep = mRecipe!!.recipeSteps!![i]
            val style = NotificationCompat.BigTextStyle()
            style.bigText(recipeStep.stepText)
            style.setBigContentTitle(String.format(
                    resources.getString(R.string.step_count), i + 1, stepCount))
            style.setSummaryText("")
            val builder = NotificationCompat.Builder(this)
            builder.setStyle(style)
            notificationPages.add(builder.build())
        }

        val builder = NotificationCompat.Builder(this)

        if (mRecipe!!.recipeImage != null) {
            val recipeImage = Bitmap.createScaledBitmap(
                    AssetUtils.loadBitmapAsset(this, mRecipe!!.recipeImage)!!,
                    Constants.NOTIFICATION_IMAGE_WIDTH, Constants.NOTIFICATION_IMAGE_HEIGHT, false)
            builder.setLargeIcon(recipeImage)
        }
        builder.setContentTitle(mRecipe!!.titleText)
        builder.setContentText(mRecipe!!.summaryText)
        builder.setSmallIcon(R.mipmap.ic_notification_recipe)

        val notification = builder
                .extend(NotificationCompat.WearableExtender()
                        .addPages(notificationPages))
                .build()
        mNotificationManager!!.notify(Constants.NOTIFICATION_ID, notification)
    }
}
