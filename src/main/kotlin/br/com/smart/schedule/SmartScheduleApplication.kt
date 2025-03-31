package br.com.smart.schedule

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmartScheduleApplication

fun main(args: Array<String>) {
	runApplication<SmartScheduleApplication>(*args)
}
