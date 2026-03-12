package pl.zurawskipiotr97glitch.productcatalog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zurawskipiotr97glitch.productcatalog.repository.ProductRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnEmptyProductList() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        String requestBody = """
                {
                  "name": "Galaxy S24",
                  "description": "Smartphone",
                  "price": 3999.99,
                  "producerId": 1,
                  "attributes": [
                    { "name": "color", "value": "black" },
                    { "name": "memory", "value": "256GB" }
                  ]
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Galaxy S24"))
                .andExpect(jsonPath("$.description").value("Smartphone"))
                .andExpect(jsonPath("$.price").value(3999.99))
                .andExpect(jsonPath("$.producerId").value(1))
                .andExpect(jsonPath("$.producerName").value("Samsung"))
                .andExpect(jsonPath("$.attributes[0].name").value("color"))
                .andExpect(jsonPath("$.attributes[0].value").value("black"))
                .andExpect(jsonPath("$.attributes[1].name").value("memory"))
                .andExpect(jsonPath("$.attributes[1].value").value("256GB"));
    }

    @Test
    void shouldReturnNotFoundWhenProducerDoesNotExist() throws Exception {
        String requestBody = """
                {
                  "name": "Test",
                  "description": "Test product",
                  "price": 100.00,
                  "producerId": 999,
                  "attributes": []
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Producer not found"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "description": "Test",
                  "price": 10.00,
                  "producerId": 1,
                  "attributes": []
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("must not be blank"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        String createRequest = """
                {
                  "name": "Galaxy S24",
                  "description": "Smartphone",
                  "price": 3999.99,
                  "producerId": 1,
                  "attributes": [
                    { "name": "color", "value": "black" },
                    { "name": "memory", "value": "256GB" }
                  ]
                }
                """;

        String createdResponse = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long productId = JsonTestUtils.extractId(createdResponse);

        String updateRequest = """
                {
                  "name": "Galaxy S24 Ultra",
                  "description": "Updated smartphone",
                  "price": 4999.99,
                  "producerId": 1,
                  "attributes": [
                    { "name": "color", "value": "silver" },
                    { "name": "memory", "value": "512GB" }
                  ]
                }
                """;

        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Galaxy S24 Ultra"))
                .andExpect(jsonPath("$.description").value("Updated smartphone"))
                .andExpect(jsonPath("$.price").value(4999.99))
                .andExpect(jsonPath("$.producerId").value(1))
                .andExpect(jsonPath("$.producerName").value("Samsung"))
                .andExpect(jsonPath("$.attributes.length()").value(2))
                .andExpect(jsonPath("$.attributes[0].name").value("color"))
                .andExpect(jsonPath("$.attributes[0].value").value("silver"))
                .andExpect(jsonPath("$.attributes[1].name").value("memory"))
                .andExpect(jsonPath("$.attributes[1].value").value("512GB"));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        String createRequest = """
                {
                  "name": "Galaxy S24",
                  "description": "Smartphone",
                  "price": 3999.99,
                  "producerId": 1,
                  "attributes": [
                    { "name": "color", "value": "black" }
                  ]
                }
                """;

        String createdResponse = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long productId = JsonTestUtils.extractId(createdResponse);

        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingProduct() throws Exception {
        String updateRequest = """
                {
                  "name": "Galaxy S24 Ultra",
                  "description": "Updated smartphone",
                  "price": 4999.99,
                  "producerId": 1,
                  "attributes": [
                    { "name": "color", "value": "silver" }
                  ]
                }
                """;

        mockMvc.perform(put("/api/products/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found"));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingProduct() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Product not found"));
    }
}