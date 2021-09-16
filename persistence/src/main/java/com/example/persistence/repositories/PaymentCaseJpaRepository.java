package com.example.persistence.repositories;

import com.example.persistence.entities.PaymentCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentCaseJpaRepository extends JpaRepository<PaymentCaseEntity, UUID> {
}
