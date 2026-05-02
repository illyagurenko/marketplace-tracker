package com.gur.marketplace_tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// сущность товара
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "article", nullable = false, unique = true)
    private String article;

    @Column(name = "current_price", nullable = false)
    private Long currentPrice;

    @Column(name = "target_price", nullable = false)
    private Long targetPrice;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;



}
