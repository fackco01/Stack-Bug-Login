package com.mesqueungroupe.stackbugv1;

import com.mesqueungroupe.stackbugv1.detail.CustomUserDetail;
import com.mesqueungroupe.stackbugv1.entity.Role;
import com.mesqueungroupe.stackbugv1.entity.User;
import com.mesqueungroupe.stackbugv1.repository.RoleRepository;
import com.mesqueungroupe.stackbugv1.repository.UserRepository;
import com.mesqueungroupe.stackbugv1.service.user.CustomUserServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
@Slf4j
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RoleRepository roleRepository;




    @Test
    public void testFindUserByEmail(){
        String email = "nguyenvana@gmail.com";
        User user = userRepository.findByEmail(email).get();
        log.info(user.getEmail());
        assertThat(user).isNotNull();
    }

    @Test
    public void testFindUserByUsername(){
        String username = "NVA123";
        User user = userRepository.findByUsername(username).get();
        log.info(user.getEmail());
        assertThat(user).isNotNull();
    }

    @Test
    public void testLoadUserByEmail(){
        String email = "nguyenvanc@gmail.com";
        CustomUserDetail customUserDetail = (CustomUserDetail) new CustomUserServiceImp(userRepository, roleRepository).loadUserByUsername(email);
        log.info(customUserDetail.getAuthorities().toString());
        assertThat(customUserDetail).isNotNull();
    }

//    @Test
//    public void testCreateAdmin() {
//
//        Admin admin = new Admin();
//        admin.setEmail("duanhotrong@gmail.com");
//        admin.setUsername("admin001");
//        admin.setPassword("admin1234");
//        admin.setDisplayName("Ho Trong Duan");
//        admin.setReputation(01);
//        admin.setEnable(true);
//        admin.setCreatedAt(LocalDateTime.now());
//
//        // Lấy role có id trong database
//        Role role = entityManager.find(Role.class, 1002);
//
//        // Kiểm tra xem Role có tồn tại trong cơ sở dữ liệu không
//        assertThat(role).isNotNull();
//
//        // Gán Role cho User
////        admin.setRole(role);
//
////        // Tạo mới một Role
////        Role role = new Role();
////        role.setId(1001);
////        role.setName("ROLE_USER");
//
////// Lưu Role vào cơ sở dữ liệu
////        entityManager.persist(role);
////
////// Gán Role cho User
////        user.setRole(role);
////
////        User saveUser = userRepository.save(user);
////
////        User existUser = entityManager.find(User.class, saveUser.getId());
////
////        assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
//    }
}
