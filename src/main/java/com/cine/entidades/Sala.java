package com.cine.entidades;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Sala mapea la tabla "salas".
 * Contiene información de la sala (nombre, capacidad).
 * Tiene relación OneToMany con Proyeccion (muchas proyecciones pueden ocurrir en una misma sala).
 */
@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    /**
     * Relación OneToMany con Proyeccion.
     * Una sala puede tener múltiples proyecciones (en distintas fechas/horarios).
     */
    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Proyeccion> proyecciones = new HashSet<>();

    // ---------------- Constructores ----------------

    public Sala() { }

    public Sala(String nombre, Integer capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    // ---------------- Getters / Setters ----------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public Set<Proyeccion> getProyecciones() { return proyecciones; }
    public void setProyecciones(Set<Proyeccion> proyecciones) { this.proyecciones = proyecciones; }

    // Helper para relación bidireccional con Proyeccion
    public void agregarProyeccion(Proyeccion p) {
        this.proyecciones.add(p);
        p.setSala(this);
    }

    public void removerProyeccion(Proyeccion p) {
        this.proyecciones.remove(p);
        p.setSala(null);
    }

    @Override
    public String toString() {
        return "Sala{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", capacidad=" + capacidad +
                '}';
    }
}
