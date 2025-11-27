package com.example.db

import com.example.model.MyObjectBox
import io.objectbox.BoxStore
import java.io.File

object Database {

    lateinit var boxStore: BoxStore
        private set

    fun init() {
        if (::boxStore.isInitialized) return

        val dbDir = File("objectbox-data/server-db").apply {
            if (!exists()) mkdirs()
        }

        boxStore = MyObjectBox.builder()
            .directory(dbDir)
            .build()
    }

    fun <T> box(clazz: Class<T>) = boxStore.boxFor(clazz)
}