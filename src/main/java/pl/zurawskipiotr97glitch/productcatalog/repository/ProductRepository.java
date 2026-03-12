package pl.zurawskipiotr97glitch.productcatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zurawskipiotr97glitch.productcatalog.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
