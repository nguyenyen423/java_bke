package com.example.mobilestore.service;

import com.example.mobilestore.dto.ProductDTO;
import com.example.mobilestore.entity.Product;
import com.example.mobilestore.exception.NotFoundException;
import com.example.mobilestore.mapper.ProductMapper;
import com.example.mobilestore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        return productMapper.toDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        return productMapper.toDTO(productRepository.save(product));
    }

    public ProductDTO updateProduct(int id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity_in_stock(productDTO.getQuantityInStock());
        existingProduct.setImage(productDTO.getImage());
        existingProduct.setConditions(productDTO.getConditions());

        return productMapper.toDTO(productRepository.save(existingProduct));
    }
}
