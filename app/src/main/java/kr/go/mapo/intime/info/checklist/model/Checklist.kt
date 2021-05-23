package kr.go.mapo.intime.info.checklist.model

data class Checklist(

    val id: Int,
    val contents: String,
    val description: String,
    val section: Int,
    val chk: Boolean

)
