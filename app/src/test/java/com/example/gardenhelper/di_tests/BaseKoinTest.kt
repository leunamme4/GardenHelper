package com.example.gardenhelper.di_tests

import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.MockProvider
import org.mockito.Mockito
import org.mockito.Mockito.mock

abstract class BaseKoinTest : KoinTest {
    @Before
    open fun setUp() {
        stopKoin()
        MockProvider.register { clazz -> mock(clazz.java) }
    }

    @After
    open fun tearDown() {
        stopKoin()
    }
}