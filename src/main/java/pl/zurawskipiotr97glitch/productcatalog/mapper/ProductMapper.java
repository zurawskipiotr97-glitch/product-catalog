package pl.zurawskipiotr97glitch.productcatalog.mapper;

import org.springframework.stereotype.Component;
import pl.zurawskipiotr97glitch.productcatalog.dto.ProductAttributeResponse;
import pl.zurawskipiotr97glitch.productcatalog.dto.ProductResponse;
import pl.zurawskipiotr97glitch.productcatalog.entity.Product;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getProducer().getId(),
                product.getProducer().getName(),
                product.getAttributes().stream()
                        .map(attribute -> new ProductAttributeResponse(
                                attribute.getId(),
                                attribute.getAttributeName(),
                                attribute.getAttributeValue()
                        ))
                        .toList()
        );
    }
}