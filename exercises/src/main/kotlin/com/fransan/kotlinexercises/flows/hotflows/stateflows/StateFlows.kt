package com.fransan.kotlinexercises.flows.hotflows.stateflows

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

class ViewCounter {
    private val _counter = MutableStateFlow(0)
    val counter = _counter.asStateFlow()

    fun increment() {
        //_counter.value++ // This is not thread safe!
        _counter.update { it + 1 }
    }
}

class ThermostatSelector(initialTemp: Double) {
    private val _temperature = MutableStateFlow(initialTemp)
    val temperature = _temperature.asStateFlow()

    fun setTemperature(temperature: Double) {
        _temperature.update { temperature }
    }
}

fun main() = runBlocking {
    val vc = ViewCounter()
    vc.increment()
    println(vc.counter.value)

    println("###############")

    val thermostat = ThermostatSelector(21.0)
    launch {
        thermostat.temperature.collect {
            log("Temperature changed: $it")
        }
    }
    delay(500.milliseconds)
    thermostat.setTemperature(25.5)
    delay(500.milliseconds)
    thermostat.setTemperature(21.1)
    delay(500.milliseconds)
    thermostat.setTemperature(21.1) // Does not emit anything because it is the same

    delay(500.milliseconds)
    this.coroutineContext.cancelChildren()
}