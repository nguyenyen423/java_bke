package com.example.mobilestore.mapper;

import com.example.mobilestore.dto.ProductDTO;
import com.example.mobilestore.entity.Category;
import com.example.mobilestore.entity.Manufacturer;
import com.example.mobilestore.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
//    @Mappings({
//            @Mapping(source = "manufacturer", target = "manufacturer.id"),
//            @Mapping(source = "category", target = "category.id")
//    })
//    Product toEntity(ProductDTO productDTO);
//
//    @Mappings({
//            @Mapping(source = "manufacturer.id", target = "manufacturer"),
//            @Mapping(source = "category.id", target = "category")
//    })
//    ProductDTO toDTO(Product product);
// Chuyển từ ProductDTO sang Product
@Mapping(target = "manufacturer", expression = "java(mapManufacturer(productDTO.getManufacturer()))")
@Mapping(target = "category", expression = "java(mapCategory(productDTO.getCategory()))")
    Product toEntity(ProductDTO productDTO);
    // Chuyển từ Product sang ProductDTO
    @Mapping(target = "manufacturer", source = "manufacturer.name") // Trả về tên Manufacturer
    @Mapping(target = "category", source = "category.name") // Trả về tên Category
    ProductDTO toDTO(Product product);

    // Hàm ánh xạ Manufacturer từ String sang Entity
    default Manufacturer mapManufacturer(String name) {
        if (name == null) {
            return null;
        }
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(name);
        return manufacturer;
    }

    // Hàm ánh xạ Category từ String sang Entity
    default Category mapCategory(String name) {
        if (name == null) {
            return null;
        }
        Category category = new Category();
        category.setName(name);
        return category;
    }

}
