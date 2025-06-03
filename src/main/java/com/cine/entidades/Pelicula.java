package com.cine.entidades;

import jakarta.persistence.*;

import java.time.Year;
import java.util.HashSet;
import java.util.Set;

/**
 * Pelicula mapea la tabla "peliculas".
 * Relación ManyToMany con Actor y OneToOne con Premio.
 * Relación OneToMany con Proyeccion (porque cada proyección “pertenece” a esta película).
 */
@Entity
@Table(name = "peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula")
    private Integer id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "año_estreno", nullable = false, columnDefinition = "YEAR")
    private Year añoEstreno;


    @Column(name = "genero", nullable = false, length = 50)
    private String genero;

    /**
     * Relación ManyToMany con Actor (tabla “actuan”).
     * Bidireccional: mapeado en Actor como well.
     */
    @ManyToMany(mappedBy = "peliculas", fetch = FetchType.LAZY)
    private Set<Actor> actores = new HashSet<>();

    /**
     * Relación OneToOne con Premio.
     * En la tabla “premios”, existe la columna película_id UNIQUE.
     */
    @OneToOne(mappedBy = "pelicula", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Premio premio;

    /**
     * Relación OneToMany: una Pelicula puede tener varias Proyecciones (en distintas salas/horarios).
     * Mapeado en Proyeccion como “pelicula”.
     */
    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Proyeccion> proyecciones = new HashSet<>();

    // ---------------- Constructores ----------------

    public Pelicula() { }

    public Pelicula(String titulo, Year añoEstreno, String genero) {
        this.titulo = titulo;
        this.añoEstreno = añoEstreno;
        this.genero = genero;
    }

    // ---------------- Getters / Setters ----------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Year getAñoEstreno() { return añoEstreno; }
    public void setAñoEstreno(Year añoEstreno) { this.añoEstreno = añoEstreno; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Set<Actor> getActores() { return actores; }
    public void setActores(Set<Actor> actores) { this.actores = actores; }

    public Premio getPremio() { return premio; }
    public void setPremio(Premio premio) {
        this.premio = premio;
        if (premio != null) {
            premio.setPelicula(this);
        }
    }

    public Set<Proyeccion> getProyecciones() { return proyecciones; }
    public void setProyecciones(Set<Proyeccion> proyecciones) { this.proyecciones = proyecciones; }

    // Helper para relación bidireccional Actor-Pelicula
    public void agregarActor(Actor a) {
        this.actores.add(a);
        a.getPeliculas().add(this);
    }

    public void removerActor(Actor a) {
        this.actores.remove(a);
        a.getPeliculas().remove(this);
    }

    // Helper para agregar Proyeccion
    public void agregarProyeccion(Proyeccion p) {
        this.proyecciones.add(p);
        p.setPelicula(this);
    }

    public void removerProyeccion(Proyeccion p) {
        this.proyecciones.remove(p);
        p.setPelicula(null);
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", añoEstreno=" + añoEstreno +
                ", genero='" + genero + '\'' +
                '}';
    }
}
