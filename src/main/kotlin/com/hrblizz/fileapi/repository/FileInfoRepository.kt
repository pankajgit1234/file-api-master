package com.hrblizz.fileapi.repository

import com.hrblizz.fileapi.model.FileInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface FileInfoRepository : MongoRepository<FileInfo, ObjectId> {

    fun findAllByFileTokenIn(fileTokens: List<String>): List<FileInfo>

    fun findByFileToken(fileToken: String): Optional<FileInfo>

    fun deleteByFileToken(fileToken: String)
}
