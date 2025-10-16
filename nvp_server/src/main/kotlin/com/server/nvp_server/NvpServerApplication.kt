package com.server.nvp_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NvpServerApplication

fun main(args: Array<String>) {
    runApplication<NvpServerApplication>(*args)
}
