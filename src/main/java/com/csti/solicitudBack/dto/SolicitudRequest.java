package com.csti.solicitudBack.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SolicitudRequest {
  private String marca;
  private String tipoSolicitud;
  private LocalDate fechaEnvio;
  private String numeroContacto;
  private String nombreContacto;
  private List<ContactoRequest> contactos;
}
