package com.gur.marketplace_tracker.repository;

import com.gur.marketplace_tracker.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// доступ к таблице
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
