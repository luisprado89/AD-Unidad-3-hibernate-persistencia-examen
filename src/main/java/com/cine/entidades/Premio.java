package com.cine.entidades;

import jakarta.persistence.*;

import java.time.Year;

/**
 * Premio mapea la tabla "premios".
 * Tiene relación OneToOne con Pelicula (columna pelicula_id UNIQUE).
 */
@Entity
@Table(name = "premios")
public class Premio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_premio")
    private Integer id;

    @Column(name = "nombre_premio", nullable = false, length = 100)
    private String nombrePremio;

    @Column(name = "año_premio", nullable = false, columnDefinition = "YEAR")
    private Year añoPremio;

    /**
     * Relación OneToOne a Pelicula.
     * En la tabla “premios”, la columna pelicula_id es UNIQUE y FOREIGN KEY a peliculas(id_pelicula).
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pelicula_id", unique = true)
    private Pelicula pelicula;

    // ---------------- Constructores ----------------

    public Premio() { }

    public Premio(String nombrePremio, Year añoPremio) {
        this.nombrePremio = nombrePremio;
        this.añoPremio = añoPremio;
    }

    // ---------------- Getters / Setters ----------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombrePremio() { return nombrePremio; }
    public void setNombrePremio(String nombrePremio) { this.nombrePremio = nombrePremio; }

    public Year getAñoPremio() { return añoPremio; }
    public void setAñoPremio(Year añoPremio) { this.añoPremio = añoPremio; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
        // Mantenemos la bidireccionalidad en la entidad Pelicula
        if (pelicula != null && pelicula.getPremio() != this) {
            pelicula.setPremio(this);
        }
    }

    @Override
    public String toString() {
        return "Premio{" +
                "id=" + id +
                ", nombrePremio='" + nombrePremio + '\'' +
                ", añoPremio=" + añoPremio +
                '}';
    }
}
