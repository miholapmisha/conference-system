package org.coursework.conferencemanagementsystem.controller;

import jakarta.validation.Valid;
import org.coursework.conferencemanagementsystem.entity.Review;
import org.coursework.conferencemanagementsystem.entity.Submission;
import org.coursework.conferencemanagementsystem.exception.ReviewException;
import org.coursework.conferencemanagementsystem.exception.SubmissionException;
import org.coursework.conferencemanagementsystem.model.request.ReviewRequest;
import org.coursework.conferencemanagementsystem.service.AccountService;
import org.coursework.conferencemanagementsystem.service.ReviewService;
import org.coursework.conferencemanagementsystem.service.SubmissionService;
import org.coursework.conferencemanagementsystem.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private SubmissionService submissionService;

    private TopicService topicService;

    private ReviewService reviewService;

    private AccountService accountService;

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setSubmissionService(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @Autowired
    public void setTopicService(TopicService topicService) {
        this.topicService = topicService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String reviewList(Model model) {
        List<Review> reviews = reviewService.getAllReviewsByCurrentProgramCommittee();
        model.addAttribute("reviews", reviews);
        model.addAttribute("role", accountService.getRoleOfCurrentUser());
        return "review/reviews";
    }

    @GetMapping("/form")
    public String loadForm(@RequestParam Long submissionId, Model model) {
        Submission submission = submissionService.getSubmissionById(submissionId)
                .orElseThrow(() -> new SubmissionException("Submission not found"));
        model.addAttribute("submission", submission);
        model.addAttribute("topics", topicService.getTopicsBySubmissionId(submissionId));
        model.addAttribute("currentReview",
                reviewService.getReviewBySubmissionAndReviewAuthor(submissionId, accountService.getCurrentAccount().getId())
                        .orElse(null));

        return "review/review-form";
    }

    @PostMapping("/submit")
    public String handleSubmit(
            @RequestParam Long submissionId,
            @ModelAttribute @Valid ReviewRequest reviewRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        reviewRequest.setSubmissionId(submissionId);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors",
                    bindingResult.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList());
            return "redirect:/review/form?submissionId=" + submissionId;
        }
        Optional<Review> currentReview = reviewService
                .getReviewBySubmissionAndReviewAuthor(submissionId, accountService.getCurrentAccount().getId());

        if (currentReview.isPresent()) {
            reviewService.updateReview(currentReview.get().getId(), reviewRequest);
            return "redirect:/review";
        }

        return getRedirectDestination(reviewRequest, redirectAttributes);
    }

    @PostMapping("/{reviewId}")
    public String handleDelete(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/review";
    }

    private String getRedirectDestination(ReviewRequest reviewRequest, RedirectAttributes redirectAttrs) {
        try {
            Optional<Review> savedReview = reviewService.saveReview(reviewRequest);
            return savedReview.isPresent() ? "redirect:/review" : "redirect:/review/form?submissionId=" + reviewRequest.getSubmissionId();
        } catch (ReviewException ex) {
            redirectAttrs.addFlashAttribute("errors", List.of(ex.getMessage()));
            return "redirect:/review/form?submissionId=" + reviewRequest.getSubmissionId();
        }
    }
}
