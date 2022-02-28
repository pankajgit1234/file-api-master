package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.filestorage.FileStorage
import com.hrblizz.fileapi.model.FileMetaDataResponse
import com.hrblizz.fileapi.model.FilesMetaDataRequest
import com.hrblizz.fileapi.model.FilesMetaDataResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DownloadFileController {
    @Autowired
    lateinit var fileStorage: FileStorage

    /*
     * Download Files
     */
    @GetMapping("/files/{filetoken}")
    fun downloadFile(@PathVariable filetoken: String): ResponseEntity<Resource> {
        val file = fileStorage.loadFile(filetoken)
        val fileDetail = fileStorage.metaDetail(filetoken)
        return ResponseEntity.ok()
            .header("X-Filename", fileDetail.file?.filename)
            .header("X-Filesize", fileDetail.file?.size.toString())
            .header("X-CreateTime", fileDetail.file?.createTime)
            .header("Content-Type", fileDetail.file?.contentType)
            .body(file)
    }

    /*
     * Delete File
     */
    @DeleteMapping("/files/{filetoken}")
    fun deleteFile(@PathVariable filetoken: String): ResponseEntity<Boolean> {
        val isDeleted = fileStorage.deleteFile(filetoken)
        return ResponseEntity.status(201).body(isDeleted)
    }

    @PostMapping("/files/metas")
    fun filesMetadata(@RequestBody request: FilesMetaDataRequest): ResponseEntity<FilesMetaDataResponse> {
        val response: FilesMetaDataResponse = fileStorage.metaDetails(request)
        return ResponseEntity.status(200).body(response)
    }

    @GetMapping("/files/{token}/metas")
    fun fileMetadata(@PathVariable token: String): ResponseEntity<FileMetaDataResponse> {
        val response: FileMetaDataResponse = fileStorage.metaDetail(token)
        return ResponseEntity.status(200).body(response)
    }
}
