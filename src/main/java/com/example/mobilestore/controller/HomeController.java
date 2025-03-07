package com.example.mobilestore.controller;

import com.example.mobilestore.dto.ProductDTO;
import com.example.mobilestore.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/home")
public class HomeController {
    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    public List<ProductDTO> getHomeProduct(){
        return productService.getAllProducts();
    }
}
