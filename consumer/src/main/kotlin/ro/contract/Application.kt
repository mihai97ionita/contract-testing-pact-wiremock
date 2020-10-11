package ro.contract

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("ro.mihai97ionita")
		.start()
}

