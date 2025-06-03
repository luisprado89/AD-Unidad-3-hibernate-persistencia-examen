package com.cine.entidades;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad Proyeccion mapea la tabla "proyecciones".
 * Clave primaria: pelicula_id, sala_id, fecha, horario (clase embebida ProyeccionId).
 * Cada proyección “une” una película con una sala en un día y un horario.
 */
@Entity
@Table(name = "proyecciones")
public class Proyeccion {

    @EmbeddedId
    private ProyeccionId id;

    /**
     * Mapeamos la relación ManyToOne con Pelicula,
     * usando la columna "pelicula_id" de la PK.
     */
    @MapsId("peliculaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pelicula_id", nullable = false)
    private Pelicula pelicula;

    /**
     * Mapeamos la relación ManyToOne con Sala,
     * usando la columna "sala_id" de la PK.
     */
    @MapsId("salaId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    // El campo fecha y horario ya están en la clave primaria (ProyeccionId)

    public Proyeccion() { }

    public Proyeccion(Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime horario) {
        this.pelicula = pelicula;
        this.sala = sala;
        this.id = new ProyeccionId(pelicula.getId(), sala.getId(), fecha, horario);
    }

    // ---------------- Getters / Setters ----------------

    public ProyeccionId getId() { return id; }
    public void setId(ProyeccionId id) { this.id = id; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        if (id == null) {
            id = new ProyeccionId();
        }
        id.setPeliculaId(pelicula.getId());
    }

    public Sala getSala() { return sala; }
    public void setSala(Sala sala) {
        this.sala = sala;
        if (id == null) {
            id = new ProyeccionId();
        }
        id.setSalaId(sala.getId());
    }

    public LocalDate getFecha() { return id.getFecha(); }
    public void setFecha(LocalDate fecha) {
        if (id == null) {
            id = new ProyeccionId();
        }
        id.setFecha(fecha);
    }

    public LocalTime getHorario() { return id.getHorario(); }
    public void setHorario(LocalTime horario) {
        if (id == null) {
            id = new ProyeccionId();
        }
        id.setHorario(horario);
    }

    @Override
    public String toString() {
        return "Proyeccion{" +
                "pelicula=" + (pelicula != null ? pelicula.getTitulo() : "null") +
                ", sala=" + (sala != null ? sala.getNombre() : "null") +
                ", fecha=" + getFecha() +
                ", horario=" + getHorario() +
                '}';
    }
}
