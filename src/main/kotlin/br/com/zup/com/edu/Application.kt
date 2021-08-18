package br.com.zup.com.edu

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zup.com.edu")
		.start()
}

