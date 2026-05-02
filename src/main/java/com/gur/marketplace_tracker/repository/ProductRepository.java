package com.gur.marketplace_tracker.repository;

import com.gur.marketplace_tracker.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
