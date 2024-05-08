package org.coursework.conferencemanagementsystem.controller;

import org.coursework.conferencemanagementsystem.entity.Account;
import org.coursework.conferencemanagementsystem.entity.Submission;
import org.coursework.conferencemanagementsystem.entity.entity_enum.Role;
import org.coursework.conferencemanagementsystem.entity.entity_enum.SubmissionStatus;
import org.coursework.conferencemanagementsystem.exception.SubmissionException;
import org.coursework.conferencemanagementsystem.model.request.AssignSubmissionRequest;
import org.coursework.conferencemanagementsystem.model.ConferenceInformation;
import org.coursework.conferencemanagementsystem.model.request.SubmissionRequest;
import org.coursework.conferencemanagementsystem.model.request.UpdateSubmissionStatusRequest;
import org.coursework.conferencemanagementsystem.service.AccountService;
import org.coursework.conferencemanagementsystem.service.SubmissionService;
import org.coursework.conferencemanagementsystem.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/submission")
public class SubmissionController {

    private TopicService topicService;

    private AccountService accountService;

    private SubmissionService submissionService;

    private ConferenceInformation conferenceInformation;

    @Autowired
    public void setTopicService(TopicService topicService) {
        this.topicService = topicService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setSubmissionService(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @Autowired
    public void setConferenceInformation(ConferenceInformation conferenceInformation) {
        this.conferenceInformation = conferenceInformation;
    }

    @GetMapping
    public String ownSubmissionList(Model model) {
        String currentEmailAccount = accountService.getEmailOfCurrentAccount();

        model.addAttribute("deadline", conferenceInformation.getFormattedDeadline());
        model.addAttribute("role", accountService.getRoleOfCurrentUser());
        model.addAttribute("submissions", submissionService.getAllSubmissionByAuthorEmail(currentEmailAccount));
        model.addAttribute("conferenceName", conferenceInformation.getName());

        return "submission/submissions";
    }

    @GetMapping("/all")
    public String allSubmissionList(Model model, @RequestParam(required = false, defaultValue = "") String search) {
        model.addAttribute("role", accountService.getRoleOfCurrentUser());
        model.addAttribute("submissions",
                submissionService.getSubmissionToTotalOverallMerit(
                        submissionService.getAllSubmissionsBySearchRequest(search)));

        return "submission/all-submission-list";
    }

    @GetMapping("/info/{submissionId}")
    public String viewSubmission(Model model, @PathVariable Long submissionId) {
        model.addAttribute("role", accountService.getRoleOfCurrentUser());
        Submission submission = submissionService.getSubmissionById(submissionId)
                .orElseThrow(() -> new SubmissionException("Couldn't load submission"));

        model.addAttribute("submission", submission);
        model.addAttribute("topics", topicService.getTopicsBySubmissionId(submissionId));

        Account currentAccount = accountService.getCurrentAccount();
        Role role = currentAccount.getRole();
        if (role == Role.ADMIN) {

            model.addAttribute("authors", accountService.getAuthorsOfSubmission(submissionId));
            List<Account> pcConflicts = accountService.getPcConflictOfSubmission(submissionId);
            model.addAttribute("pcConflicts", pcConflicts);

            List<Account> assignedAccounts = accountService.getAssignedAccountsBySubmission(submission.getId());
            List<Account> allAccounts = accountService.getAllAccounts();
            List<Long> assignedAccountsIds = assignedAccounts.stream()
                    .map(Account::getId)
                    .toList();

            model.addAttribute("accounts",

                    allAccounts.stream()
                            .filter(account -> !account.getId().equals(currentAccount.getId()) &&
                                    account.getRole() == Role.PROGRAM_COMMITTEE || account.getRole() == Role.ADMIN)
                            .collect(
                                    Collectors.toMap(Function.identity(),
                                            account ->
                                                    assignedAccountsIds.contains(account.getId()),
                                            (e1, e2) -> e1,
                                            LinkedHashMap::new)
                            ));
        }

        return "submission/submission-view";
    }

    @PostMapping("/assign")
    public String assignSubmissionToProgramCommittees(@ModelAttribute AssignSubmissionRequest request) {
        submissionService
                .assignSubmissionsExactlyToProgramCommittees(request.getProgramCommitteesIds(), request.getSubmissionId());
        return "redirect:/submission/info/" + request.getSubmissionId();
    }

    @GetMapping("/form")
    public String submissionForm(Model model) {
        model.addAttribute("topics", topicService.getAllTopics());
        model.addAttribute("program_committees",
                accountService.getAccountsByRole(Role.PROGRAM_COMMITTEE));
        model.addAttribute("deadline", conferenceInformation.getFormattedDeadline());

        return "submission/submission-form";
    }

    @GetMapping("/assigned")
    public String assignedSubmissionsToProgramCommittee(Model model) {
        Account account = accountService.getCurrentAccount();
        model.addAttribute("submissions",
                submissionService.getAllSubmissionsAssignedToProgramCommittee(account));
        model.addAttribute("role", accountService.getRoleOfCurrentUser());
        return "submission/assigned-submissions";
    }

    @PostMapping("/update")
    public String updateSubmissionStatus(@ModelAttribute UpdateSubmissionStatusRequest request) {
        submissionService.updateStatusOfSubmission(request.getSubmissionId(),
                SubmissionStatus.valueOf(request.getStatus()));
        return "redirect:/submission/all";
    }

    @PostMapping("/submit")
    public String handleSubmission(@ModelAttribute @Validated SubmissionRequest submissionRequest,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors",
                    bindingResult.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList());
            return "redirect:/submission/form";
        }

        return getRedirectDestination(submissionRequest, redirectAttributes);
    }

    private String getRedirectDestination(SubmissionRequest submissionRequest, RedirectAttributes redirectAttrs) {
        try {
            Optional<Submission> savedSubmission = submissionService.saveSubmission(submissionRequest);
            return savedSubmission.isPresent() ? "redirect:/submission" : "redirect:/submission/form";
        } catch (SubmissionException ex) {
            redirectAttrs.addFlashAttribute("errors", List.of(ex.getMessage()));
            return "redirect:/submission/form";
        }
    }

}
