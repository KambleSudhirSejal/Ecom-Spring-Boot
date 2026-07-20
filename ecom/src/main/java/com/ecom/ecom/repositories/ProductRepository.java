package com.ecom.ecom.repositories;

import com.ecom.ecom.dto.ProductResponse;
import com.ecom.ecom.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByActiveTrue();
}
