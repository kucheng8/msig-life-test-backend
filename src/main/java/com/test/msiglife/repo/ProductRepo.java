package com.test.msiglife.repo;

import com.test.msiglife.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findAllByStatus(String status);
}
