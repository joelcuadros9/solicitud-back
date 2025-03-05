package com.csti.solicitudBack.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "contacto")
public class Contacto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;
  private String numeroContacto;

  @ManyToOne
  @JoinColumn(name = "solicitud_id", nullable = false)
  private Solicitud solicitud;
}
