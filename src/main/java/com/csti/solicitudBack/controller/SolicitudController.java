package com.csti.solicitudBack.controller;

import com.csti.solicitudBack.dto.SolicitudRequest;
import com.csti.solicitudBack.dto.SolicitudResponse;
import com.csti.solicitudBack.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/solicitudes")
@Tag(name = "Solicitudes", description = "API para gestionar solicitudes")
public class SolicitudController {
  private final SolicitudService solicitudService;

  public SolicitudController(SolicitudService solicitudService) {
    this.solicitudService = solicitudService;
  }

  @PostMapping
  @Operation(summary = "Crear una nueva solicitud", description = "Crea una nueva solicitud con los datos proporcionados")
  @ApiResponse(responseCode = "200", description = "Solicitud creada correctamente")
  public Mono<SolicitudResponse> create(@RequestBody SolicitudRequest solicitudRequest) {
    return solicitudService.save(solicitudRequest);
  }

  @GetMapping
  @Operation(summary = "Obtener todas las solicitudes", description = "Retorna una lista de todas las solicitudes")
  @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida correctamente")
  public Flux<SolicitudResponse> list() {
    return solicitudService.findAll();
  }

  @GetMapping("/export")
  @Operation(summary = "Exportar solicitudes a CSV", description = "Genera un archivo CSV con todas las solicitudes")
  @ApiResponse(responseCode = "200", description = "Archivo CSV generado correctamente")
  public Mono<ResponseEntity<byte[]>> exportToCsv() {
    return solicitudService.findAll()
            .collectList()
            .map(solicitudes -> {
              StringBuilder csv = new StringBuilder("Codigo,Marca,Tipo de Solicitud,Fecha de Envio,Numero de Contacto,Nombre de Contacto\n");

              for (SolicitudResponse solicitud : solicitudes) {
                csv.append(solicitud.getCodigo()).append(",")
                        .append(solicitud.getMarca()).append(",")
                        .append(solicitud.getTipoSolicitud()).append(",")
                        .append(solicitud.getFechaEnvio()).append(",")
                        .append(solicitud.getNumeroContacto()).append(",")
                        .append(solicitud.getNombreContacto()).append("\n");
              }

              byte[] csvBytes = csv.toString().getBytes();
              HttpHeaders headers = new HttpHeaders();
              headers.setContentType(MediaType.TEXT_PLAIN);
              headers.setContentDispositionFormData("attachment", "solicitudes.csv");

              return ResponseEntity.ok().headers(headers).body(csvBytes);
            });
  }

}
