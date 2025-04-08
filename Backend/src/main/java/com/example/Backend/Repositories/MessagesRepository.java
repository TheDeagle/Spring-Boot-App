package com.example.Backend.Repositories;

import com.example.Backend.Modules.Messages;
import com.example.Backend.Modules.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {
    Messages findByUserAAndUserB(User userA, User userB);
}
