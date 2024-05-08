package org.coursework.conferencemanagementsystem.config;

import org.coursework.conferencemanagementsystem.model.security.PaperAccessManager;
import org.coursework.conferencemanagementsystem.model.security.ReviewSubmissionAccessManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.coursework.conferencemanagementsystem.repository.AccountRepository.ACCOUNT_CREDENTIALS_BY_EMAIL_QUERY;
import static org.coursework.conferencemanagementsystem.repository.AccountRepository.ROLES_BY_ACCOUNT_EMAIL_QUERY;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    private static final SessionRegistry SESSION_REGISTRY = new SessionRegistryImpl();

    private final ReviewSubmissionAccessManager reviewSubmissionAccessManager;

    private final PaperAccessManager paperAccessManager;

    public SecurityConfig(DataSource dataSource, ReviewSubmissionAccessManager reviewSubmissionAccessManager, PaperAccessManager paperAccessManager) {
        this.dataSource = dataSource;
        this.reviewSubmissionAccessManager = reviewSubmissionAccessManager;
        this.paperAccessManager = paperAccessManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(ACCOUNT_CREDENTIALS_BY_EMAIL_QUERY)
                .authoritiesByUsernameQuery(ROLES_BY_ACCOUNT_EMAIL_QUERY);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/home").authenticated()
                        .requestMatchers("/submission/submit").authenticated()
                        .requestMatchers("/submission/all").hasAnyAuthority("ADMIN", "PROGRAM_COMMITTEE")
                        .requestMatchers("/submission/assign").hasAnyAuthority("ADMIN")
                        .requestMatchers("/submission/assigned").hasAnyAuthority("ADMIN", "PROGRAM_COMMITTEE")
                        .requestMatchers("/submission/update").hasAnyAuthority("ADMIN")
                        .requestMatchers("/submission/**").authenticated()
                        .requestMatchers("/account/list/all").hasAnyAuthority("ADMIN", "PROGRAM_COMMITTEE")
                        .requestMatchers("/account/list/program-committee").authenticated()
                        .requestMatchers("/account/update").hasAnyAuthority("ADMIN")
                        .requestMatchers("/account/**").permitAll()
                        .requestMatchers("/paper/**").access(paperAccessManager)
                        .requestMatchers(HttpMethod.GET, "/review/form").access(reviewSubmissionAccessManager)
                        .requestMatchers(HttpMethod.POST, "/review/submit").access(reviewSubmissionAccessManager)
                        .requestMatchers(HttpMethod.POST, "/review/**").access(reviewSubmissionAccessManager)
                        .requestMatchers("/review/**").hasAnyAuthority("ADMIN", "PROGRAM_COMMITTEE")
                        .anyRequest().permitAll())
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer
                                .loginPage("/account/login")
                                .defaultSuccessUrl("/home"))
                .sessionManagement(sess -> {
                    sess.maximumSessions(-1).sessionRegistry(sessionRegistry());
                    sess.sessionConcurrency(concurrencyControlConfigurer ->
                            concurrencyControlConfigurer.expiredUrl("/account/login?msgCode=1"));
                })
                .logout(logout -> logout.deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true));


        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return SESSION_REGISTRY;
    }
}
