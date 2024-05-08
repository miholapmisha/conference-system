package org.coursework.conferencemanagementsystem.service;

import org.coursework.conferencemanagementsystem.entity.Account;
import org.coursework.conferencemanagementsystem.entity.Review;
import org.coursework.conferencemanagementsystem.entity.Submission;
import org.coursework.conferencemanagementsystem.entity.entity_enum.Role;
import org.coursework.conferencemanagementsystem.entity.entity_enum.SubmissionStatus;
import org.coursework.conferencemanagementsystem.exception.SubmissionException;
import org.coursework.conferencemanagementsystem.model.ConferenceInformation;
import org.coursework.conferencemanagementsystem.model.request.SubmissionRequest;
import org.coursework.conferencemanagementsystem.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubmissionService {

    private SubmissionRepository submissionRepository;

    private AccountService accountService;

    private PaperService paperService;

    private ReviewService reviewService;

    private ConferenceInformation conferenceInformation;

    @Autowired
    public void setConferenceInformation(ConferenceInformation conferenceInformation) {
        this.conferenceInformation = conferenceInformation;
    }

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setPaperService(PaperService paperService) {
        this.paperService = paperService;
    }

    @Autowired
    public void setSubmissionRepository(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Optional<Submission> getSubmissionById(Long id) {
        return submissionRepository.getSubmissionById(id);
    }

    public List<Submission> getAllSubmissionsAssignedToProgramCommittee(Account account) {

        if (account.getRole() != Role.PROGRAM_COMMITTEE && account.getRole() != Role.ADMIN) {
            throw new SubmissionException("Specified account does not have a program committee rights");
        }

        return submissionRepository.getSubmissionAssignedToProgramCommittee(account.getId());
    }

    @Transactional
    public Optional<Submission> saveSubmission(SubmissionRequest submissionRequest) {

        if (LocalDateTime.now().isEqual(conferenceInformation.getLocalDateDeadline())
                || LocalDateTime.now().isAfter(conferenceInformation.getLocalDateDeadline())) {
            throw new SubmissionException("Deadline has passed for submission");
        }

        Submission submission = new Submission();

        submission.setFileDestination(submissionRequest.getPaper().getOriginalFilename());
        submission.setAbstractOfSubmission(submissionRequest.getAbstractOfSubmission());
        submission.setStatus(SubmissionStatus.SUBMITTED);
        submission.setTitle(submissionRequest.getTitle());

        Submission savedSubmission = submissionRepository.saveSubmission(submission)
                .orElseThrow(() -> new SubmissionException("Unable to save submission to data base"));
        Long submissionId = savedSubmission.getId();
        loadAuthorsToSubmission(submissionRequest.getAuthorsEmails(), submissionId);
        loadTopicsToSubmission(submissionRequest.getTopicsIds(), submissionId);
        loadConflictAuthorsToSubmission(submissionRequest.getPcConflictsIds(), submissionId);

        paperService.saveSubmissionPaper(submissionRequest.getPaper());

        return Optional.of(savedSubmission);
    }

    public void loadConflictAuthorsToSubmission(List<Long> conflictAuthorsIds, Long submissionId) {
        for (Long conflictAuthorId : conflictAuthorsIds) {
            submissionRepository.insertSubmissionToConflictAuthor(submissionId, conflictAuthorId);
        }
    }

    private void loadTopicsToSubmission(List<Long> topicsIds, Long submissionId) {
        for (Long topicId : topicsIds) {
            submissionRepository.insertSubmissionToTopic(submissionId, topicId);
        }
    }

    private void loadAuthorsToSubmission(List<String> authorsEmails, Long submissionId) {
        String emailOfCurrentAccount = accountService.getEmailOfCurrentAccount();
        Long idOfCurrentUser = accountService.getAccountByEmail(emailOfCurrentAccount)
                .orElseThrow(() -> new SubmissionException("Unable to load current account")).getId();
        submissionRepository.insertSubmissionToAuthor(submissionId, idOfCurrentUser);

        for (String authorEmail : authorsEmails) {
            if (!authorEmail.isBlank() && !authorEmail.isEmpty() && !authorEmail.equals(emailOfCurrentAccount)) {
                Account account = accountService.getAccountByEmail(authorEmail)
                        .orElseThrow(() -> new SubmissionException("Authors with such emails does not exist"));

                Long authorId = account.getId();
                submissionRepository.insertSubmissionToAuthor(submissionId, authorId);
            }
        }
    }

    public List<Submission> getAllSubmissionByAuthorEmail(String authorEmail) {
        return submissionRepository.getAllSubmissionsByAuthorEmail(authorEmail);
    }

    @Transactional
    public void assignSubmissionToProgramCommittee(Long programCommitteeId, Long submissionId) {
        submissionRepository.insertSubmissionToProgramCommittee(programCommitteeId, submissionId);
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.getAllSubmissions();
    }

    public Map<Submission, Double> getSubmissionToTotalOverallMerit(List<Submission> submissions) {
        Map<Submission, Double> submissionToAllOverallMerit = new LinkedHashMap<>();

        for (Submission submission : submissions) {
            List<Review> reviews = reviewService.getReviewsBySubmissionId(submission.getId());

            if (reviews.isEmpty()) {
                submissionToAllOverallMerit.put(submission, 0.0);
            } else {
                double totalOverallMerit = 0.0;
                for (Review review : reviews) {
                    totalOverallMerit += review.getOverallMerit().getValue();
                }

                submissionToAllOverallMerit.put(submission, totalOverallMerit / reviews.size());
            }
        }

        return submissionToAllOverallMerit;
    }

    @Transactional
    public void updateStatusOfSubmission(Long submissionId, SubmissionStatus submissionStatus) {
        Submission submission = getSubmissionById(submissionId)
                .orElseThrow(() -> new SubmissionException("Submission with id " + submissionId + " does not exist"));
        submissionRepository.updateStatusBySubmissionId(submission.getId(), submissionStatus);
    }

    @Transactional
    public void assignSubmissionsExactlyToProgramCommittees(List<Long> programCommitteesIdsToAssign, Long submissionId) {

        List<Long> assignedAccountsIds = accountService.getAssignedAccountsBySubmission(submissionId).stream()
                .map(Account::getId)
                .toList();

        for (Long pcId : programCommitteesIdsToAssign) {
            if (!assignedAccountsIds.contains(pcId)) {
                assignSubmissionToProgramCommittee(pcId, submissionId);
            }
        }
        for (Long pcId : assignedAccountsIds) {
            if (!programCommitteesIdsToAssign.contains(pcId)) {
                removeAssignSubmissionOfProgramCommittee(pcId, submissionId);
                reviewService.getReviewBySubmissionAndReviewAuthor(submissionId, pcId)
                        .ifPresent(review -> reviewService.deleteReview(review.getId()));
            }
        }
    }

    @Transactional
    public void removeAssignSubmissionOfProgramCommittee(Long pcId, Long submissionId) {
        submissionRepository.removeSubmissionToProgramCommittee(pcId, submissionId);
        reviewService.getReviewBySubmissionAndReviewAuthor(pcId, submissionId)
                .ifPresent(review -> reviewService.deleteReview(review.getId()));
    }

    public List<Submission> getAllSubmissionsBySearchRequest(String searchRequest) {
        return submissionRepository.getSubmissionsBySearchRequest(searchRequest.toLowerCase());
    }
}