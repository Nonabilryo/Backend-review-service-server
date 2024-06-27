package nonabili.reviewserviceserver.service

import nonabili.reviewserviceserver.client.UserClient
import nonabili.reviewserviceserver.dto.request.ReviewPostRequest
import nonabili.reviewserviceserver.dto.response.AvgRatingResponse
import nonabili.reviewserviceserver.entity.Review
import nonabili.reviewserviceserver.repository.ReviewRepository
import nonabili.reviewserviceserver.util.error.CustomError
import nonabili.reviewserviceserver.util.error.ErrorState
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ReviewService(val reviewRepository: ReviewRepository, val userClient: UserClient, val s3UploadService: S3UploadService) {
    fun postReview(articleIdx: String, images: List<MultipartFile>, request: ReviewPostRequest, userIdx: String) { // todo order와 연계
//        val userIdx = userClient.getUserIdxById(userId).idx ?: throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        reviewRepository.save(
            Review(
                writer = UUID.fromString(userIdx),
                article = UUID.fromString(articleIdx),
                rating = request.rating,
                title = request.title,
                description = request.description,
                imageUrls = images.map { s3UploadService.saveFileAndGetUrl(it, "review_images") }
            )
        )
    }
    fun getReview(articleIdx: String, page: Int): Page<Review> {
        return reviewRepository.findReviewsByArticle(UUID.fromString(articleIdx), PageRequest.of(page, 30))
    }
    fun getAvgRating(articleIdx: String): AvgRatingResponse {
        return AvgRatingResponse(
            rating = reviewRepository.findAverageRatingByArticle(UUID.fromString(articleIdx))
        )
    }
    fun deleteReview(reviewIdx: String, userIdx: String) {
        val review = reviewRepository.findReviewByIdx(UUID.fromString(reviewIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_REVIEW)
        if (review.writer != UUID.fromString(userIdx)) throw CustomError(ErrorState.DIFFERENT_USER)
        reviewRepository.delete(review)
    }
}