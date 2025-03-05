package com.csti.solicitudBack.repository;

import com.csti.solicitudBack.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}