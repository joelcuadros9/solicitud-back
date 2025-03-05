package com.csti.solicitudBack.service;

import com.csti.solicitudBack.dto.ContactoResponse;
import com.csti.solicitudBack.dto.SolicitudRequest;
import com.csti.solicitudBack.dto.SolicitudResponse;
import com.csti.solicitudBack.model.Contacto;
import com.csti.solicitudBack.model.Solicitud;
import com.csti.solicitudBack.repository.ContactoRepository;
import com.csti.solicitudBack.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudService {
  private final SolicitudRepository solicitudRepository;
  private final ContactoRepository contactoRepository;

  public SolicitudService(SolicitudRepository solicitudRepository,
                          ContactoRepository contactoRepository) {
    this.solicitudRepository = solicitudRepository;
    this.contactoRepository = contactoRepository;
  }

  public Mono<SolicitudResponse> save(SolicitudRequest solicitudRequest) {
    return Mono.fromCallable(() -> {

              Solicitud solicitud = new Solicitud();
              solicitud.setMarca(solicitudRequest.getMarca());
              solicitud.setTipoSolicitud(solicitudRequest.getTipoSolicitud());
              solicitud.setFechaEnvio(solicitudRequest.getFechaEnvio());
              solicitud.setNumeroContacto(solicitudRequest.getNumeroContacto());
              solicitud.setNombreContacto(solicitudRequest.getNombreContacto());

              Solicitud savedSolicitud = solicitudRepository.save(solicitud);

              List<Contacto> contactos = solicitudRequest.getContactos().stream()
                      .map(contactoRequest -> {
                        Contacto contacto = new Contacto();
                        contacto.setNombre(contactoRequest.getNombre());
                        contacto.setNumeroContacto(contactoRequest.getNumeroContacto());
                        contacto.setSolicitud(savedSolicitud);
                        return contacto;
                      })
                      .collect(Collectors.toList());
              contactoRepository.saveAll(contactos);

              return toSolicitudResponse(savedSolicitud, contactos);
            })
            .subscribeOn(Schedulers.elastic());
  }

  public Flux<SolicitudResponse> findAll() {
    return Mono.fromCallable(solicitudRepository::findAll)
            .flatMapMany(Flux::fromIterable)
            .flatMap(solicitud -> {
              return Mono.fromCallable(() -> contactoRepository.findBySolicitudId(solicitud.getId()))
                      .map(contactos -> toSolicitudResponse(solicitud, contactos));
            })
            .subscribeOn(Schedulers.elastic());
  }

  private SolicitudResponse toSolicitudResponse(Solicitud solicitud, List<Contacto> contactos) {
    SolicitudResponse response = new SolicitudResponse();
    response.setId(solicitud.getId());
    response.setCodigo(solicitud.getCodigo());
    response.setMarca(solicitud.getMarca());
    response.setTipoSolicitud(solicitud.getTipoSolicitud());
    response.setFechaEnvio(solicitud.getFechaEnvio());
    response.setNumeroContacto(solicitud.getNumeroContacto());
    response.setNombreContacto(solicitud.getNombreContacto());

    List<ContactoResponse> contactoResponses = contactos.stream()
            .map(contacto -> {
              ContactoResponse contactoResponse = new ContactoResponse();
              contactoResponse.setNombre(contacto.getNombre());
              contactoResponse.setNumeroContacto(contacto.getNumeroContacto());
              return contactoResponse;
            })
            .collect(Collectors.toList());
    response.setContactos(contactoResponses);

    return response;
  }
}
