package com.leonduri.d7back.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
  
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM User limit :count offset :page", nativeQuery = true)
    List<User> findAdminUserList(@Param("count")Long count, @Param("page") Long page);

    @Query(value = "select count(id) from User", nativeQuery = true)
    Long countAllUser();
  
    Optional<User> findByNickname(String nickname);

    @Override void deleteById(Long id);
}