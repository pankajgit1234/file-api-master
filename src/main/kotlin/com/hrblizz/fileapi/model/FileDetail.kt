package com.hrblizz.fileapi.model

data class FileDetail(
    var token: String = "",
    var filename: String = "",
    var size: Long = 0,
    var contentType: String = "",
    var createTime: String = "",
    var meta: MetaDetails? = null
)
