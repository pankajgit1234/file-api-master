package com.hrblizz.fileapi.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("FileInfo")
data class FileInfo(
    @Id
    val id: ObjectId? = null,
    var fileName: String,
    var relativePath: String,
    var createdBy: String,
    var fileToken: String,
    var size: Long,
    var createTime: String,
    var contentType: String
)
