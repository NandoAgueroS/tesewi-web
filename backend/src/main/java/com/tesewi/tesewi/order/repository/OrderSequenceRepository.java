package com.tesewi.tesewi.order.repository;

import com.tesewi.tesewi.order.entity.OrderSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSequenceRepository extends JpaRepository<OrderSequence, String> {
}