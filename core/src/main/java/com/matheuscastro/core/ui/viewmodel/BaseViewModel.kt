package com.matheuscastro.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<S : State>(
    val initialState: S
) : ViewModel() {

    protected val _state = MutableStateFlow(initialState)
    val state : StateFlow<S> = _state.asStateFlow()

    abstract fun onEvent(event: UiEvent)
}