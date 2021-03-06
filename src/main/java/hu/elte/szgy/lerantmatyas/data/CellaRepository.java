package hu.elte.szgy.lerantmatyas.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface CellaRepository extends JpaRepository<Cella, Integer> {
} 
