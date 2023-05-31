package com.firstapplication.recipebook.utils

import com.firstapplication.recipebook.di.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class AppDispatchers @Inject constructor(
    @MainDispatcher val mainDispatcher: MainCoroutineDispatcher,
    @IODispatcher val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
    @UnconfinedDispatcher val unconfined: CoroutineDispatcher
)