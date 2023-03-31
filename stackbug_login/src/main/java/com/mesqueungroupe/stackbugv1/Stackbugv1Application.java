package com.mesqueungroupe.stackbugv1;

import com.mesqueungroupe.stackbugv1.entity.Role;
import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class Stackbugv1Application {

	public static void main(String[] args) {
		SpringApplication.run(Stackbugv1Application.class, args);
	}



}
