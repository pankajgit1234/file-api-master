package com.hrblizz.fileapi

import com.hrblizz.fileapi.filestorage.FileStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

    @Autowired
    lateinit var fileStorage: FileStorage

    @Bean
    fun run() = CommandLineRunner {
        fileStorage.deleteAll()
        fileStorage.init()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
