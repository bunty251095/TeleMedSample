package com.vivant.telemedicine.network

import kotlinx.coroutines.CoroutineDispatcher

class AppDispatchers(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher
)