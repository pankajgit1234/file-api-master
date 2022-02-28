package com.hrblizz.fileapi.filestorage

import com.hrblizz.fileapi.model.FileCreationResponse
import com.hrblizz.fileapi.model.FileMetaDataResponse
import com.hrblizz.fileapi.model.FilesMetaDataRequest
import com.hrblizz.fileapi.model.FilesMetaDataResponse
import com.hrblizz.fileapi.model.MetaDetails
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface FileStorage {
    fun store(file: MultipartFile, meta: MetaDetails): FileCreationResponse
    fun loadFile(fileToken: String): Resource
    fun deleteAll()
    fun init()
    fun loadFiles(): Stream<Path>
    fun deleteFile(fileToken: String): Boolean
    fun metaDetail(fileToken: String): FileMetaDataResponse
    fun metaDetails(request: FilesMetaDataRequest): FilesMetaDataResponse
}
