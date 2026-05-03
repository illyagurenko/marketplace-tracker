package com.gur.marketplace_tracker.controller;

import com.gur.marketplace_tracker.dto.ProductRequest;

import com.gur.marketplace_tracker.entity.Product;
import com.gur.marketplace_tracker.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/tracker")
    public Long postProduct(@RequestBody ProductRequest request){
        Product product = productService.createTrack(request);
        return product.getCurrentPrice();

    }
}
