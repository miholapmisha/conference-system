package org.coursework.conferencemanagementsystem.model.security;

import org.coursework.conferencemanagementsystem.entity.Account;
import org.coursework.conferencemanagementsystem.entity.Review;
import org.coursework.conferencemanagementsystem.entity.Submission;
import org.coursework.conferencemanagementsystem.entity.entity_enum.Role;
import org.coursework.conferencemanagementsystem.repository.AccountRepository;
import org.coursework.conferencemanagementsystem.repository.ReviewRepository;
import org.coursework.conferencemanagementsystem.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class PaperAccessManager implements AuthorizationManager<RequestAuthorizationContext> {

    private SubmissionRepository submissionRepository;

    private ReviewRepository reviewRepository;

    private AccountRepository accountRepository;

    @Autowired
    public void setSubmissionRepository(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Autowired
    public void setReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        Optional<Account> loadedAccount = accountRepository.getAccountByEmail(authentication.get().getName());
        if (loadedAccount.isEmpty()) {
            return new AuthorizationDecision(false);
        }

        Account authenticatedAccount = loadedAccount.get();
        Role role = authenticatedAccount.getRole();
        String uri = object.getRequest().getRequestURI();
        String fileName = URLDecoder.decode(uri.substring(uri.lastIndexOf('/') + 1), StandardCharsets.UTF_8);

        if (role == Role.ADMIN || (role == Role.PROGRAM_COMMITTEE && uri.contains("submission"))) {
            return new AuthorizationDecision(true);
        } else if (role == Role.PARTICIPANT && uri.contains("submission")) {

            List<Submission> submissions = submissionRepository.getAllSubmissionsByAuthorEmail(authenticatedAccount.getEmail());
            for (Submission submission : submissions) {
                if (submission.getFileDestination().equals(fileName)) {
                    return new AuthorizationDecision(true);
                }
            }
        } else if (role == Role.PROGRAM_COMMITTEE && uri.contains("review")) {

            List<Review> reviews = reviewRepository.getReviewsByAuthor(authenticatedAccount.getId());
            for (Review review : reviews) {
                if (review.getFileDestination().equals(fileName)) {
                    return new AuthorizationDecision(true);
                }
            }
        }

        return new AuthorizationDecision(false);
    }

}
