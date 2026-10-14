package com.example.back_end.model.repository;

import com.example.back_end.model.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByNumeroSerieIgnoreCase(String numeroSerie);

    boolean existsByNumeroSerieIgnoreCaseAndIdNot(String numeroSerie, Long id);

    boolean existsByTituloId(Long tituloId);
}
