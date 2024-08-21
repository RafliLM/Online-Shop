package com.readiness.online_shop.repository;

import com.readiness.online_shop.model.Customer;
import com.readiness.online_shop.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByItemNameContaining(String itemName, Pageable pageable);
}
