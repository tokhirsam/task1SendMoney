package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.task1.entity.Outcome;

public interface OutcomeRepository extends JpaRepository<Outcome, Integer> {
}
