package me.alberto.rec.data.domain.model

data class Recording(
    val id: Long,
    val name: String,
    val file: String,
    val isIncoming: Boolean,
    val time: String
)
