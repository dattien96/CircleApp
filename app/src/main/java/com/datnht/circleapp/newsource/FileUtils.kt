package com.datnht.circleapp.newsource

import android.content.Context
import java.io.IOException
import kotlin.jvm.Throws

@Throws(IOException::class)
fun getJsonFromAssets(context: Context, fileName: String): String {
    val inputStream = context.assets.open(fileName)

    val out = StringBuilder()
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            out.append(line)
        }
    }

    return out.toString()
}