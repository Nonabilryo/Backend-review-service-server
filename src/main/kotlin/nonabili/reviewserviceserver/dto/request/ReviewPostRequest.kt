package nonabili.reviewserviceserver.dto.request

data class ReviewPostRequest(
    val rating: Float,
    val title: String,
    val description: String,
) {}