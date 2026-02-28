package com.example.cryptography.repository;



import com.example.cryptography.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNodeId(String nodeId);
    boolean existsByUsername(String username);
    boolean existsByNodeId(String nodeId);
}
