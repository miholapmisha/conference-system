package org.coursework.conferencemanagementsystem.model.security;

import org.coursework.conferencemanagementsystem.entity.Account;
import org.coursework.conferencemanagementsystem.entity.Review;
import org.coursework.conferencemanagementsystem.entity.entity_enum.Role;
import org.coursework.conferencemanagementsystem.repository.AccountRepository;
import org.coursework.conferencemanagementsystem.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class ReviewSubmissionAccessManager implements AuthorizationManager<RequestAuthorizationContext> {

    private AccountRepository accountRepository;

    private ReviewRepository reviewRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        if (!authentication.get().isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        String email = authentication.get().getName();
        Optional<Account> loadedAccount = accountRepository.getAccountByEmail(email);
        if (loadedAccount.isEmpty()) {
            return new AuthorizationDecision(false);
        }

        Account authenticatedAccount = loadedAccount.get();
        if (authenticatedAccount.getRole() == Role.ADMIN) {
            return new AuthorizationDecision(true);
        }

        if (authenticatedAccount.getRole() != Role.ADMIN && authenticatedAccount.getRole() != Role.PROGRAM_COMMITTEE) {
            return new AuthorizationDecision(false);
        }

        if (object.getRequest().getMethod().equalsIgnoreCase("DELETE")) {
            String uri = object.getRequest().getRequestURI();
            Long reviewId = Long.parseLong(uri.substring(uri.lastIndexOf('/') + 1));
            List<Review> reviews = reviewRepository.getReviewsByAuthor(authenticatedAccount.getId());
//            if(reviews.size() > 0) {}
        }

        Long submissionId = Long.parseLong(object.getRequest().getParameter("submissionId"));
        if (accountRepository.getNumberOfAssignsByPCAndSubmissionId(authenticatedAccount.getId(), submissionId) <= 0) {
            return new AuthorizationDecision(false);
        }

        return new AuthorizationDecision(true);
    }
}
