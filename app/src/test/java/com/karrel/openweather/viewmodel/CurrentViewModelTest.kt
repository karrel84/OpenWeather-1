package com.karrel.openweather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import androidx.lifecycle.Observer
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class CurrentViewModelTest {

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)


    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CurrentViewModel

    private val observer: Observer<String> = mock()

    @Before
    fun setup(){
        viewModel = CurrentViewModel()
        viewModel.getCheckedTime().observeForever(observer)
    }

    @Test
    fun fetchTimeShouldReturnTime(){
        val time = "05:13 PM"

        viewModel.setCheckedTime(time)


        val captor = ArgumentCaptor.forClass(String::class.java)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertEquals(time, value)
        }

    }
}