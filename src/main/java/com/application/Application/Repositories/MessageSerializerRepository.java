package com.application.Application.Repositories;

import com.application.Application.Serializers.MessageSerializer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageSerializerRepository extends JpaRepository<MessageSerializer, Long> {
    MessageSerializer findByReceiverUsername(String username);
}
