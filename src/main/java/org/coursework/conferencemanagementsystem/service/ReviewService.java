package org.coursework.conferencemanagementsystem.service;

import org.coursework.conferencemanagementsystem.entity.Review;
import org.coursework.conferencemanagementsystem.entity.entity_enum.OverallMerit;
import org.coursework.conferencemanagementsystem.exception.ReviewException;
import org.coursework.conferencemanagementsystem.model.request.ReviewRequest;
import org.coursework.conferencemanagementsystem.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    private AccountService accountService;

    private PaperService paperService;

    @Autowired
    public void setPaperService(PaperService paperService) {
        this.paperService = paperService;
    }

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Transactional
    public Optional<Review> saveReview(ReviewRequest reviewRequest) {

        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setOverallMerit(OverallMerit.valueOf(reviewRequest.getOverallMerit()));
        review.setAuthorId(accountService.getCurrentAccount().getId());
        review.setSubmissionId(reviewRequest.getSubmissionId());
        review.setReviewDate(LocalDateTime.now());
        review.setFileDestination("");

        if (reviewRequest.getReviewPaper() != null && !reviewRequest.getReviewPaper().isEmpty()) {
            paperService.saveReviewPaper(reviewRequest.getReviewPaper());
            review.setFileDestination(reviewRequest.getReviewPaper().getOriginalFilename());
        }

        return reviewRepository.saveReview(review);
    }

    public List<Review> getAllReviewsByProgramCommittee(Long programCommitteeId) {
        return reviewRepository.getReviewsByAuthor(programCommitteeId);
    }

    public List<Review> getAllReviewsByCurrentProgramCommittee() {
        return getAllReviewsByProgramCommittee(accountService.getCurrentAccount().getId());
    }

    public Optional<Review> getReviewBySubmissionAndReviewAuthor(Long submissionId, Long authorId) {
        return reviewRepository.getReviewBySubmissionAndReviewAuthorIds(submissionId, authorId);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewRequest reviewRequest) {
        Review reviewToUpdate = getReviewById(reviewId)
                .orElseThrow(() -> new ReviewException("Couldn't find review with id " + reviewId));
        reviewToUpdate.setComment(reviewRequest.getComment());
        reviewToUpdate.setOverallMerit(OverallMerit.valueOf(reviewRequest.getOverallMerit()));
        reviewToUpdate.setSubmissionId(reviewRequest.getSubmissionId());
        reviewToUpdate.setReviewDate(LocalDateTime.now());

        if (reviewRequest.getReviewPaper() != null && !reviewRequest.getReviewPaper().isEmpty()) {
            paperService.deleteReviewPaper(reviewToUpdate.getFileDestination());
            paperService.saveReviewPaper(reviewRequest.getReviewPaper());
            reviewToUpdate.setFileDestination(reviewRequest.getReviewPaper().getOriginalFilename());
        }

        reviewRepository.updateReview(reviewToUpdate, reviewId);
    }

    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.getReviewById(reviewId);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review reviewToDelete = getReviewById(reviewId)
                .orElseThrow(() -> new ReviewException("Couldn't find review with id " + reviewId));
        reviewRepository.deleteReviewById(reviewId);
        if (reviewToDelete.getFileDestination() != null && !reviewToDelete.getFileDestination().isEmpty()) {
            paperService.deleteReviewPaper(reviewToDelete.getFileDestination());
        }
    }

    public List<Review> getReviewsBySubmissionId(Long submissionId) {
        return reviewRepository.getReviewBySubmissionId(submissionId);
    }
}
