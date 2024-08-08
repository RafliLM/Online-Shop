package com.readiness.online_shop.repository;

import com.readiness.online_shop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
