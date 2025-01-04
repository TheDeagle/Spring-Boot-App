package com.application.Application.Repositories;

import com.application.Application.Modules.Message;
import com.application.Application.Modules.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessagesRepository extends JpaRepository<Message, Long> {
    Message findByUser1AndUser2(User user1, User user2);
}
