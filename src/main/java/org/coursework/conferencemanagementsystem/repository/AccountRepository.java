package org.coursework.conferencemanagementsystem.repository;

import org.coursework.conferencemanagementsystem.entity.Account;
import org.coursework.conferencemanagementsystem.entity.entity_enum.Role;
import org.coursework.conferencemanagementsystem.repository.utils.JdbcRepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private final RowMapper<Account> accountRowMapper = (rs, rowNum) -> {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setRole(Role.valueOf(rs.getString("role")));
        account.setPassword(rs.getString("password"));
        account.setAffiliation(rs.getString("affiliation"));
        account.setEmail(rs.getString("email"));
        account.setFirstName(rs.getString("first_name"));
        account.setSecondName(rs.getString("second_name"));
        account.setRegion(rs.getString("region"));

        return account;
    };

    private static final String SAVE_ACCOUNT_QUERY =
            """
                    INSERT INTO accounts (email, first_name, second_name,  password, affiliation, region, orcid_id, role) \
                    VALUES (:email, :first_name, :second_name, :password, :affiliation, :region, :orcid_id, :role) \s""";

    public static final String ACCOUNT_CREDENTIALS_BY_EMAIL_QUERY =
            "SELECT email, password, TRUE as enabled from accounts WHERE email=?";

    public static final String ROLES_BY_ACCOUNT_EMAIL_QUERY =
            "SELECT email, role FROM accounts WHERE email=?";

    public static final String SELECT_ACCOUNT_BY_EMAIL_QUERY =
            "SELECT id, email, first_name, second_name,  password, affiliation, region, orcid_id, role FROM accounts WHERE email=(:email)";

    private static final String SELECT_ACCOUNTS_BY_ROLE_QUERY =
            "SELECT id, email, first_name, second_name,  password, affiliation, region, orcid_id, role FROM accounts WHERE role=(:role)";

    private static final String SELECT_ACCOUNT_BY_ID_QUERY =
            "SELECT id, email, first_name, second_name,  password, affiliation, region, orcid_id, role FROM accounts WHERE id=(:id)";


    private static final String SELECT_ACCOUNTS_BY_SUBMISSION_ID_QUERY =
            """
                    SELECT a.id,
                           a.email,
                           a.first_name,
                           a.second_name,
                           a.password,
                           a.affiliation,
                           a.region,
                           a.orcid_id,
                           a.role
                    FROM accounts a
                             JOIN submissions_authors sa on a.id = sa.author_id
                             JOIN submissions s on s.id = sa.submission_id
                    WHERE s.id=:submission_id""";

    private static final String SELECT_PcCONFLICTS_BY_SUBMISSION_ID_QUERY =
            """
                    SELECT a.id,
                           a.email,
                           a.first_name,
                           a.second_name,
                           a.password,
                           a.affiliation,
                           a.region,
                           a.orcid_id,
                           a.role
                    FROM accounts a
                             JOIN submissions_conflicts sc on a.id = sc.account_id
                             JOIN submissions s on s.id = sc.submission_id
                    WHERE s.id=:submission_id""";

    private static final String SELECT_ALL_ACCOUNTS_QUERY
            = "SELECT id, email, first_name, second_name,  password, affiliation, region, orcid_id, role FROM accounts ORDER BY role";

    private static final String SELECT_ALL_ASSIGNMENTS_BY_SUBMISSION_ID_QUERY =
            """
                    SELECT a.id,
                           a.email,
                           a.first_name,
                           a.second_name,
                           a.password,
                           a.affiliation,
                           a.region,
                           a.orcid_id,
                           a.role
                    FROM accounts a
                             JOIN assigns aa on a.id = aa.program_committee_id
                             JOIN submissions s on s.id = aa.submission_id
                    WHERE submission_id = :submission_id""";

    private static final String UPDATE_ACCOUNT_ROLE_QUERY
            = "UPDATE accounts SET role=:role WHERE id=:id";

    private static final String SELECT_ACCOUNT_BY_SEARCH_REQUEST =
            """
                    SELECT *
                    FROM accounts
                    WHERE firstName ILIKE CONCAT('%', :search, '%')
                       OR secondName ILIKE CONCAT('%', :search, '%')
                       OR email ILIKE CONCAT('%', :search, '%')""";

    private static final String SELECT_COUNT_OF_ASSIGN_PROPERTY_QUERY =
            """
                    SELECT COUNT(*)
                    FROM assigns
                    WHERE submission_id = :submission_id
                      AND program_committee_id = :program_committee_id""";

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedJdbcTemplate = jdbcTemplate;
    }


    @Transactional
    public Optional<Account> save(Account account) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", account.getEmail())
                .addValue("first_name", account.getFirstName())
                .addValue("second_name", account.getSecondName())
                .addValue("password", account.getPassword())
                .addValue("affiliation", account.getAffiliation())
                .addValue("region", account.getRegion())
                .addValue("role", String.valueOf(account.getRole()))
                .addValue("orcid_id",
                        account.getOrcidId() == null || account.getOrcidId().isEmpty() ? "" : account.getOrcidId());

        return JdbcRepositoryUtils.saveEntity(namedJdbcTemplate, parameters, SAVE_ACCOUNT_QUERY, account);
    }

    public Optional<Account> getAccountByEmail(String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email);

        return JdbcRepositoryUtils.getOptionalEntity(namedJdbcTemplate, parameters, accountRowMapper, SELECT_ACCOUNT_BY_EMAIL_QUERY);
    }

    public Optional<Account> getAccountById(Long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return JdbcRepositoryUtils.getOptionalEntity(namedJdbcTemplate, parameters, accountRowMapper, SELECT_ACCOUNT_BY_ID_QUERY);
    }

    public List<Account> getAccountByRole(Role role) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("role", String.valueOf(role));

        return namedJdbcTemplate.query(SELECT_ACCOUNTS_BY_ROLE_QUERY, parameters, accountRowMapper);
    }

    public List<Account> getAccountBySubmissionId(Long submissionId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("submission_id", submissionId);

        return namedJdbcTemplate.query(SELECT_ACCOUNTS_BY_SUBMISSION_ID_QUERY, parameters, accountRowMapper);
    }

    public List<Account> getPcConflictsBySubmissionId(Long submissionId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("submission_id", submissionId);

        return namedJdbcTemplate.query(SELECT_PcCONFLICTS_BY_SUBMISSION_ID_QUERY, parameters, accountRowMapper);
    }

    public List<Account> getAllAccounts() {
        return namedJdbcTemplate.query(SELECT_ALL_ACCOUNTS_QUERY, accountRowMapper);
    }

    public void updateAccountRoleById(Long id, Role role) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("role", String.valueOf(role))
                .addValue("id", id);

        namedJdbcTemplate.update(UPDATE_ACCOUNT_ROLE_QUERY, parameters);
    }

    public List<Account> getAssignedAccountsBySubmissionId(Long submissionId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("submission_id", submissionId);

        return namedJdbcTemplate.query(SELECT_ALL_ASSIGNMENTS_BY_SUBMISSION_ID_QUERY, parameters, accountRowMapper);
    }

    public List<Account> getAccountsBySearchRequest(String searchRequest) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("search", searchRequest);

        return namedJdbcTemplate.query(SELECT_ACCOUNT_BY_SEARCH_REQUEST, parameters, accountRowMapper);
    }

    public Integer getNumberOfAssignsByPCAndSubmissionId(Long pcId, Long submissionId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("submission_id", submissionId)
                .addValue("program_committee_id", pcId);

        return namedJdbcTemplate.queryForObject(SELECT_COUNT_OF_ASSIGN_PROPERTY_QUERY, parameters, Integer.class);
    }
}
