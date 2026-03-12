package pl.zurawskipiotr97glitch.productcatalog.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long producerId,
        String producerName,
        List<ProductAttributeResponse> attributes
) {
}