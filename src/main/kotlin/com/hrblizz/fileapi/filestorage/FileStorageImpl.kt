package com.hrblizz.fileapi.filestorage

import com.hrblizz.fileapi.exception.NotFoundException
import com.hrblizz.fileapi.model.FileCreationResponse
import com.hrblizz.fileapi.model.FileDetail
import com.hrblizz.fileapi.model.FileInfo
import com.hrblizz.fileapi.model.FileMetaDataResponse
import com.hrblizz.fileapi.model.FilesMetaDataRequest
import com.hrblizz.fileapi.model.FilesMetaDataResponse
import com.hrblizz.fileapi.model.MetaDetails
import com.hrblizz.fileapi.model.TokenDetail
import com.hrblizz.fileapi.repository.FileInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.UUID
import java.util.stream.Stream

@Service
class FileStorageImpl : FileStorage {

    @Autowired
    lateinit var fileInfoRepository: FileInfoRepository

    val rootLocation: Path = Paths.get("filestorage")

    override fun store(file: MultipartFile, meta: MetaDetails): FileCreationResponse {
        Files.copy(file.inputStream, this.rootLocation.resolve(file.getOriginalFilename()))
        val fileInfo = FileInfo(
            fileName = file.originalFilename.toString(),
            fileToken = UUID.randomUUID().toString(),
            relativePath = this.rootLocation.toString(),
            createdBy = meta.creatorEmployeeId,
            createTime = LocalDateTime.now().toString(),
            size = file.size,
            contentType = file.contentType.toString()
        )
        val response = fileInfoRepository.save(fileInfo)
        val fileCreationResponse: FileCreationResponse = FileCreationResponse().also {
            it.token = response.fileToken
        }
        return fileCreationResponse
    }

    override fun loadFile(fileToken: String): Resource {
        val fileDetail = fileInfoRepository.findByFileToken(fileToken)
            .orElseThrow { NotFoundException("File with token $fileToken not found") }

        val fileName = fileDetail.fileName
        val file = rootLocation.resolve(fileName)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("FAIL!")
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        Files.createDirectory(rootLocation)
    }

    override fun loadFiles(): Stream<Path> {
        return Files.walk(this.rootLocation, 1)
            .filter { path -> !path.equals(this.rootLocation) }
            .map(this.rootLocation::relativize)
    }

    override fun deleteFile(fileToken: String): Boolean {
        val fileDetailToDelete = fileInfoRepository.findByFileToken(fileToken)
            .orElseThrow { NotFoundException("File with token $fileToken not found") }
        fileInfoRepository.deleteByFileToken(fileToken)
        val filename = fileDetailToDelete.fileName
        return FileSystemUtils.deleteRecursively(rootLocation.resolve(filename))
    }

    override fun metaDetail(fileToken: String): FileMetaDataResponse {
        val fileInfo = fileInfoRepository.findByFileToken(fileToken)
            .orElseThrow { NotFoundException("File with token $fileToken not found") }
        val response = FileMetaDataResponse()
        response.file = createFileDetail(fileInfo)
        return response
    }

    override fun metaDetails(request: FilesMetaDataRequest): FilesMetaDataResponse {

        val tokens = createTokenList(request.tokens)
        val fileInfoList = fileInfoRepository.findAllByFileTokenIn(tokens)
        val response = FilesMetaDataResponse()
        response.files = createFileDetailList(fileInfoList)
        return response
    }

    fun createTokenList(tokenDetailList: List<TokenDetail>): MutableList<String> {
        val tokens: MutableList<String> = mutableListOf()
        for (tokenDetail in tokenDetailList) {
            tokens.add(tokenDetail.token)
        }
        return tokens
    }

    fun createFileDetailList(fileInfoList: List<FileInfo>): MutableList<FileDetail> {
        val list = mutableListOf<FileDetail>()
        for (fileInfo in fileInfoList) {
            val fileDetail = createFileDetail(fileInfo)
            list.add(fileDetail)
        }
        return list
    }

    fun createFileDetail(fileInfo: FileInfo): FileDetail {
        val fileDetail = FileDetail()
        fileDetail.token = fileInfo.fileToken
        fileDetail.filename = fileInfo.fileName
        fileDetail.size = fileInfo.size
        fileDetail.contentType = fileInfo.contentType
        fileDetail.createTime = fileInfo.createTime
        val meta = MetaDetails(fileInfo.createdBy)
        fileDetail.meta = meta
        return fileDetail
    }
}
