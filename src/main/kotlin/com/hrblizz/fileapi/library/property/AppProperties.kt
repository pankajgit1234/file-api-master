package com.hrblizz.fileapi.library.property

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {
    @Value("\${spring.servlet.multipart.max-file-size}")
    lateinit var maxFileSize: String

    @Value("\${spring.servlet.multipart.max-request-size}")
    lateinit var maxRequestSize: String
}
