package pl.zurawskipiotr97glitch.productcatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zurawskipiotr97glitch.productcatalog.entity.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}
