package org.coursework.conferencemanagementsystem.service;

import jakarta.servlet.http.HttpServletRequest;
import org.coursework.conferencemanagementsystem.entity.Account;
import org.coursework.conferencemanagementsystem.entity.entity_enum.Role;
import org.coursework.conferencemanagementsystem.exception.AccountException;
import org.coursework.conferencemanagementsystem.exception.UserAlreadyExistsException;
import org.coursework.conferencemanagementsystem.model.request.RegisterAccountRequest;
import org.coursework.conferencemanagementsystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    private AccountWebService accountWebService;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setAccountWebService(AccountWebService accountWebService) {
        this.accountWebService = accountWebService;
    }

    @Transactional
    public Optional<Account> createAccount(HttpServletRequest request, RegisterAccountRequest registerRequest) {
        Optional<Account> accountByEmail = getAccountByEmail(registerRequest.getEmail());

        if (accountByEmail.isPresent()) {
            throw new UserAlreadyExistsException(registerRequest.getEmail());
        }

        Account account = new Account();

        account.setRole(Role.PARTICIPANT);
        account.setEmail(registerRequest.getEmail());
        account.setPassword(accountWebService.encodePassword(registerRequest.getPassword()));
        account.setAffiliation(registerRequest.getAffiliation());
        account.setFirstName(registerRequest.getFirstName());
        account.setSecondName(registerRequest.getSecondName());
        account.setOrcidId(registerRequest.getOrcidId());
        account.setRegion(registerRequest.getRegion());

        Optional<Account> savedAccount = accountRepository.save(account);
        accountWebService.login(request, registerRequest.getEmail(), registerRequest.getPassword());
        return savedAccount;
    }

    @Transactional
    public void updateAccountRole(Long accountId, Role role) {
        Account account = getAccountById(accountId)
                .orElseThrow(() -> new AccountException("Couldn't find account with id: " + accountId));
        if (role != account.getRole()) {
            accountRepository.updateAccountRoleById(account.getId(), role);
            accountWebService.expireSessionOfAccount(account);
        }
    }

    public String getEmailOfCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Account getCurrentAccount() {
        return accountRepository.getAccountByEmail(getEmailOfCurrentAccount())
                .orElseThrow(() -> new AccountException("Couldn't load current account"));
    }

    public List<Account> filterAccountsBySearchRequest(List<Account> accounts, String searchRequest) {

        return accounts.stream()
                .filter(account -> {
                    String lowerCaseSearchRequest = searchRequest.toLowerCase();
                    return account.getEmail().toLowerCase().contains(lowerCaseSearchRequest)
                            || account.getFirstName().toLowerCase().contains(lowerCaseSearchRequest)
                            || account.getSecondName().toLowerCase().contains(lowerCaseSearchRequest);
                })
                .toList();
    }

    public List<Account> getAccountsByRole(Role role) {
        return accountRepository.getAccountByRole(role);
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.getAccountByEmail(email);
    }

    public Role getRoleOfCurrentUser() {
        return getCurrentAccount().getRole();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    public List<Account> getAuthorsOfSubmission(Long submissionId) {
        return accountRepository.getAccountBySubmissionId(submissionId);
    }

    public List<Account> getAssignedAccountsBySubmission(Long submissionId) {
        return accountRepository.getAssignedAccountsBySubmissionId(submissionId);
    }

    public List<Account> getPcConflictOfSubmission(Long submissionId) {
        return accountRepository.getPcConflictsBySubmissionId(submissionId);
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.getAccountById(id);
    }

    public List<Account> getAccountBySearchRequest(String searchRequest) {
        return accountRepository.getAccountsBySearchRequest(searchRequest.toLowerCase());
    }

    public boolean isSubmissionAssignedToProgramCommittee(Long pcId, Long submissionId) {
        return accountRepository.getNumberOfAssignsByPCAndSubmissionId(pcId, submissionId) > 0;
    }
}