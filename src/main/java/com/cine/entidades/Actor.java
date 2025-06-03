package com.cine.entidades;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Actor mapea la tabla "actores".
 * Tiene relación ManyToMany con Pelicula usando la tabla de unión "actuan".
 */
@Entity
@Table(name = "actores")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actor")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "nacionalidad", nullable = false, length = 100)
    private String nacionalidad;

    /**
     * Relación ManyToMany con Pelicula, usando la tabla "actuan" como join table.
     * Bidireccional: en Pelicula habrá un Set<Actor> anotado de forma simétrica.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "actuan",
            joinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id_actor"),
            inverseJoinColumns = @JoinColumn(name = "pelicula_id", referencedColumnName = "id_pelicula")
    )
    private Set<Pelicula> peliculas = new HashSet<>();

    // ---------------- Constructores ----------------

    public Actor() { }

    public Actor(String nombre, LocalDate fechaNacimiento, String nacionalidad) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
    }

    // ---------------- Getters / Setters ----------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public Set<Pelicula> getPeliculas() { return peliculas; }
    public void setPeliculas(Set<Pelicula> peliculas) { this.peliculas = peliculas; }

    // Método auxiliar para mantener la relación bidireccional
    public void agregarPelicula(Pelicula p) {
        this.peliculas.add(p);
        p.getActores().add(this);
    }

    public void removerPelicula(Pelicula p) {
        this.peliculas.remove(p);
        p.getActores().remove(this);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", nacionalidad='" + nacionalidad + '\'' +
                '}';
    }
}
