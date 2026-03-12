package pl.zurawskipiotr97glitch.productcatalog.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductAttributeRequest (
        @NotBlank String name,
        String value
) {
}
