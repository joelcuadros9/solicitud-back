package com.csti.solicitudBack.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SolicitudResponse {
  private Long id;
  private String codigo;
  private String marca;
  private String tipoSolicitud;
  private LocalDate fechaEnvio;
  private String numeroContacto;
  private String nombreContacto;
  private List<ContactoResponse> contactos;
}
