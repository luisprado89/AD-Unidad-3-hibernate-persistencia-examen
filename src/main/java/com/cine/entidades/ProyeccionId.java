package com.cine.entidades;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Clase auxiliar que recoge los campos de la PK compuesta de Proyeccion:
 * (pelicula_id, sala_id, fecha, horario).
 */
@Embeddable
public class ProyeccionId implements Serializable {

    private Integer peliculaId;
    private Integer salaId;
    private LocalDate fecha;
    private LocalTime horario;

    public ProyeccionId() { }

    public ProyeccionId(Integer peliculaId, Integer salaId, LocalDate fecha, LocalTime horario) {
        this.peliculaId = peliculaId;
        this.salaId = salaId;
        this.fecha = fecha;
        this.horario = horario;
    }

    // Getters / Setters
    public Integer getPeliculaId() { return peliculaId; }
    public void setPeliculaId(Integer peliculaId) { this.peliculaId = peliculaId; }

    public Integer getSalaId() { return salaId; }
    public void setSalaId(Integer salaId) { this.salaId = salaId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    // equals() y hashCode() son imprescindibles para clave compuesta
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProyeccionId)) return false;
        ProyeccionId that = (ProyeccionId) o;
        return Objects.equals(peliculaId, that.peliculaId) &&
                Objects.equals(salaId, that.salaId) &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(horario, that.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peliculaId, salaId, fecha, horario);
    }
}
