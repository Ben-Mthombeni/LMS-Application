package org.pm09.ps12.lms.leaveapplication.config;

import org.pm09.ps12.lms.leaveapplication.entity.Employee;
import org.pm09.ps12.lms.leaveapplication.repository.EmployeeRepository;
import org.pm09.ps12.lms.leaveapplication.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final CustomUserDetailsService userDetailsService;
    @Autowired
    private final EmployeeRepository employeeRepository;

    public SecurityConfig(CustomUserDetailsService userDetailsService, EmployeeRepository employeeRepository) {
        this.userDetailsService = userDetailsService;
        this.employeeRepository = employeeRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/webjars/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    CommandLineRunner initUsers(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (employeeRepository.count() == 0) {

                Employee employee = new Employee();
                employee.setName("John Employee");
                employee.setUsername("employee"); // <-- default username
                employee.setEmail("employee@example.com");
                employee.setPassword(passwordEncoder.encode("password123"));
                employee.setRole(Employee.Role.EMPLOYEE);

                Employee manager = new Employee();
                manager.setName("Jane Manager");
                manager.setUsername("manager"); // <-- default username
                manager.setEmail("manager@example.com");
                manager.setPassword(passwordEncoder.encode("password123"));
                manager.setRole(Employee.Role.MANAGER);

                employeeRepository.save(employee);
                employeeRepository.save(manager);
            }
        };

    }
    }
