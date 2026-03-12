package pl.zurawskipiotr97glitch.productcatalog.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zurawskipiotr97glitch.productcatalog.dto.ProductRequest;
import pl.zurawskipiotr97glitch.productcatalog.dto.ProductResponse;
import pl.zurawskipiotr97glitch.productcatalog.entity.Producer;
import pl.zurawskipiotr97glitch.productcatalog.entity.Product;
import pl.zurawskipiotr97glitch.productcatalog.entity.ProductAttribute;
import pl.zurawskipiotr97glitch.productcatalog.mapper.ProductMapper;
import pl.zurawskipiotr97glitch.productcatalog.repository.ProducerRepository;
import pl.zurawskipiotr97glitch.productcatalog.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Producer producer = producerRepository.findById(request.producerId())
                .orElseThrow(() -> new EntityNotFoundException("Producer not found"));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setProducer(producer);

        if (request.attributes() != null) {
            request.attributes().forEach(attributeRequest -> {
                ProductAttribute attribute = new ProductAttribute();
                attribute.setAttributeName(attributeRequest.name());
                attribute.setAttributeValue(attributeRequest.value());
                attribute.setProduct(product);
                product.getAttributes().add(attribute);
            });
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }
}