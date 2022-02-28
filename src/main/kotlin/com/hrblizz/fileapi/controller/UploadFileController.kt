package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.filestorage.FileStorage
import com.hrblizz.fileapi.model.FileCreationResponse
import com.hrblizz.fileapi.model.MetaDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException

@RestController
class UploadFileController {

    @Autowired
    lateinit var fileStorage: FileStorage

    @PostMapping("/files")
    @Throws(FileNotFoundException::class)
    fun uploadMultipartFile(
        @RequestParam("name", required = true) name: String,
        @RequestParam("contentType", required = true) contentType: String,
        @RequestParam("meta", required = true) meta: MetaDetails,
        @RequestParam("source", required = true) source: String,
        @RequestParam("expireTime", required = false) expireTime: String?,
        @RequestParam("uploadfile") file: MultipartFile,
    ): ResponseEntity<FileCreationResponse> {
        if (file.size == 0L)
            throw FileNotFoundException("File can't be empty")
        val response: FileCreationResponse = fileStorage.store(file, meta)
        return ResponseEntity.status(201).body(response)
    }
}
