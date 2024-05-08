package org.coursework.conferencemanagementsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.coursework.conferencemanagementsystem.entity.Account;
import org.coursework.conferencemanagementsystem.entity.entity_enum.Role;
import org.coursework.conferencemanagementsystem.exception.UserAlreadyExistsException;
import org.coursework.conferencemanagementsystem.model.request.RegisterAccountRequest;
import org.coursework.conferencemanagementsystem.model.request.UpdateRoleRequest;
import org.coursework.conferencemanagementsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {

    private static final Map<Integer, String> messageCodesLoginPage =
            Map.of(1, "You've been logged out due session expiration. This might be cause by changing authorities of your account",
                    2, "Credentials are invalid");

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/registration-form")
    public String registrationForm() {
        return "account/registration-form";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) Integer msgCode, Model model) {
        if (msgCode != null) {
            model.addAttribute("message", messageCodesLoginPage.get(msgCode));
        }
        return "account/login-form";
    }

    @GetMapping("/list/{role}")
    public String listOfAccounts(@PathVariable String role,
                                 @RequestParam(required = false, defaultValue = "") String search,
                                 Model model) {

        model.addAttribute("role", accountService.getRoleOfCurrentUser());

        if (role.equals("program-committee")) {
            List<Account> programCommittees = accountService.getAccountsByRole(Role.PROGRAM_COMMITTEE);
            model.addAttribute("accounts",
                    accountService.filterAccountsBySearchRequest(programCommittees, search));

        } else if (role.equals("all")) {
            List<Account> allAccounts = accountService.getAllAccounts();
            model.addAttribute("accounts",
                    accountService.filterAccountsBySearchRequest(allAccounts, search));
        }

        return "account/account-list";
    }

    @PostMapping("/update")
    public String updateRole(@ModelAttribute UpdateRoleRequest updateRoleRequest) {
        accountService.updateAccountRole(updateRoleRequest.getAccountId(), Role.valueOf(updateRoleRequest.getRole()));
        return "redirect:/account/list/all";
    }

    @PostMapping("/register")
    public String registrationForm(@ModelAttribute @Validated RegisterAccountRequest registerRequest,
                                   BindingResult bindingResult, Model model, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Check correctness of input data!");
            return "account/registration-form";
        }

        return getRedirectDestination(registerRequest, model, request);
    }

    private String getRedirectDestination(RegisterAccountRequest registerRequest, Model model,
                                          HttpServletRequest request) {
        try {
            Optional<Account> savedAccount = accountService.createAccount(request, registerRequest);
            return savedAccount.isPresent() ? "redirect:/home" : "account/registration-form";
        } catch (UserAlreadyExistsException exception) {
            model.addAttribute("error", exception.getMessage());
            return "account/registration-form";
        }
    }

}
