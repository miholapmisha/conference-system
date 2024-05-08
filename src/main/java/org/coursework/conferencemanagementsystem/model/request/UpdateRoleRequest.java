package org.coursework.conferencemanagementsystem.model.request;

public class UpdateRoleRequest {

    private Long accountId;

    private String role;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
