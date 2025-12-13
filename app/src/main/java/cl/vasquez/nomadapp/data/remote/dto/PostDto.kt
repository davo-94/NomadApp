package cl.vasquez.nomadapp.data.remote.dto

data class PostDto(
    val id: Long? = null,
    val title: String,
    val description: String,
    val date: String,
    val imageUrls: List<String> = emptyList()
)