package com.mdts.eieapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultApiQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DictionaryApiQualifier