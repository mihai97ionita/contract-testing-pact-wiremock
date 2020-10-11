package ro.contract

import com.fasterxml.jackson.databind.ObjectMapper

fun Any.toJson(): String = ObjectMapper().writeValueAsString(this)