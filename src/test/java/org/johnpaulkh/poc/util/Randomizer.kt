package org.johnpaulkh.poc.util

import org.jeasy.random.EasyRandom
import kotlin.random.Random

val random: EasyRandom = EasyRandom()

inline fun <reified T> random(): T = random.nextObject(T::class.java)