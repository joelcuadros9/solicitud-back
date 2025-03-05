package com.csti.solicitudBack.repository;

import com.csti.solicitudBack.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
  List<Contacto> findBySolicitudId(Long solicitudId);
}
