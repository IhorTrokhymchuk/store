package com.example.store.repository.orderstatus;

import com.example.store.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<Status, Long> {
    Status findStatusByStatus(Status.StatusName statusName);
}
