package uz.pdp.task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.task1.entity.Income;

public interface IncomeRepository extends JpaRepository<Income, Integer> {
}
