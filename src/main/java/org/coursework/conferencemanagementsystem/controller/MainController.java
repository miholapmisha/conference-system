package org.coursework.conferencemanagementsystem.controller;

import org.coursework.conferencemanagementsystem.model.ConferenceInformation;
import org.coursework.conferencemanagementsystem.service.AccountService;
import org.coursework.conferencemanagementsystem.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class MainController {

    private AccountService accountService;

    private ConferenceInformation conferenceInformation;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setConferenceInformation(ConferenceInformation conferenceInformation) {
        this.conferenceInformation = conferenceInformation;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("conferenceName", conferenceInformation.getName());
        model.addAttribute("conferenceDeadline", conferenceInformation.getFormattedDeadline());
        model.addAttribute("role", accountService.getRoleOfCurrentUser());
        return "home";
    }
}
