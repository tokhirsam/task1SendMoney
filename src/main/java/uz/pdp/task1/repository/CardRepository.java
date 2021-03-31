package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.task1.entity.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
