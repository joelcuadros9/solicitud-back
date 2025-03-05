package com.csti.solicitudBack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "solicitud")
public class Solicitud {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String codigo;
  private String marca;
  private String tipoSolicitud;
  private LocalDate fechaEnvio;
  private String numeroContacto;
  private String nombreContacto;

  @JsonIgnore
  @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Contacto> contactos;

  @PrePersist
  public void generarCodigo() {
    if (this.codigo == null) {
      this.codigo = UUID.randomUUID().toString();
    }
  }

}