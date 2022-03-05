/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hlibrary.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection

import androidx.core.content.ContextCompat

import java.io.FileDescriptor

/**
 * @author linwenhui
 * @date 2017-1-1
 */
object BitmapDecoderUtils {


    fun decodeSampledBitmapFromDescriptor(fileDescriptor: FileDescriptor,
                                          reqWidth: Int, reqHeight: Int): Bitmap? {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight)
        options.inJustDecodeBounds = false
        return try {
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
                options)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }

    }

    fun decodeSampledBitmapFromFile(filename: String,
                                    reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filename, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight)
        options.inJustDecodeBounds = false
        try {
            return BitmapFactory.decodeFile(filename, options)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            return null
        }

    }

    fun decodeSampledBitmapFromByteArray(data: ByteArray,
                                         offset: Int, length: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, offset, length, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(data, offset, length, options)
    }


    fun forceRefreshSystemAlbum(context: Context, path: String) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val type = options.outMimeType
        MediaScannerConnection.scanFile(context, arrayOf(path), arrayOf(type), null)
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        val config = if (drawable.opacity != PixelFormat.OPAQUE)
            Bitmap.Config.ARGB_8888
        else
            Bitmap.Config.RGB_565
        val bitmap = Bitmap.createBitmap(w, h, config)
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

    fun getBitmap(context: Context, resId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, resId)
        return drawableToBitmap(drawable!!)
    }

    fun decodeSampledBitmapFromResource(res: Resources,
                                        resId: Int, reqWidth: Int, reqHeight: Int): Bitmap? {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        try {
            return BitmapFactory.decodeResource(res, resId, options)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            return null
        }

    }

    fun calculateInSampleSize(options: BitmapFactory.Options,
                              reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        return inSampleSize
    }

    fun getCompressedBitmap(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    fun getCompressedBitmap(context: Context, path: String): Bitmap {
        val displayMetrics = context.resources.displayMetrics
        return getCompressedBitmap(path, displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

}
