package com.mdts.eieapp.domain

interface LocalCache {
    fun putString(key: String, value: String?)
    fun getString(key: String, default: String? = null): String?

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, default: Boolean = false): Boolean

    fun <T> put(key: String, value: T?)
    fun <T> get(key: String, classOfT: Class<T>, default: T? = null): T?
}