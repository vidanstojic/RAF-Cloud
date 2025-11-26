package com.server.nvp_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class NvpServerApplication

fun main(args: Array<String>) {
    runApplication<NvpServerApplication>(*args)
}
