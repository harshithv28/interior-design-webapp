package com.interior.design.config;

import com.interior.design.domain.Project;
import com.interior.design.domain.Role;
import com.interior.design.domain.User;
import com.interior.design.repository.ProjectRepository;
import com.interior.design.repository.UserRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UserRepository users, ProjectRepository projects, PasswordEncoder encoder) {
        return args -> {
            if (users.count() > 0) return;

            User admin = new User();
            admin.setEmail("admin@demo.com");
            admin.setFullName("Demo Admin");
            admin.setRole(Role.ADMIN);
            admin.setPasswordHash(encoder.encode("admin123"));
            users.save(admin);

            User vendor = new User();
            vendor.setEmail("vendor@demo.com");
            vendor.setFullName("Demo Vendor");
            vendor.setRole(Role.VENDOR);
            vendor.setPasswordHash(encoder.encode("vendor123"));
            users.save(vendor);

            User customer = new User();
            customer.setEmail("customer@demo.com");
            customer.setFullName("Demo Customer");
            customer.setRole(Role.CUSTOMER);
            customer.setPasswordHash(encoder.encode("customer123"));
            users.save(customer);

            Project p1 = new Project();
            p1.setName("Modern Living Room");
            p1.setDescription("Cozy and modern living room redesign");
            p1.setCustomer(customer);
            p1.setVendor(vendor);
            p1.setEstimatedCost(new BigDecimal("5000"));
            projects.save(p1);
        };
    }
}

