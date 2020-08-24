package com.example.gitstarscounter.git_api

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.io.IOException


val VoidJsonAdapter: Any = object : Any() {
    @FromJson
    @Throws(IOException::class)
    fun fromJson(reader: JsonReader): Void? {
        return reader.nextNull()
    }

    @ToJson
    @Throws(IOException::class)
    fun toJson(writer: JsonWriter, v: Void?) {
        writer.nullValue()
    }
}