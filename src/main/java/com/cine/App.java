package com.cine;
/**
 * Main: contiene el menú con 15 opciones tal como pide el enunciado:
 *
 * 1. Crear actor
 * 2. Eliminar actor
 * 3. Crear pelicula
 * 4. Eliminar pelicula
 * 5. Crear premio
 * 6. Eliminar premio
 * 7. Modificar género de una película
 * 8. Asignar un premio a una película
 * 9. Asignar un actor a una película
 * 10. Asignar una película a una sala en una fecha y hora (LocalDate.now y LocalTime.now)
 * 11. Consulta 1: nombres de actores estadounidenses ordenados por fechaNacimiento desc
 * 12. Consulta 2: nombre+nacionalidad de actores que participaron en película X
 * 13. Consulta 3: título+género de películas proyectadas en fecha dada
 * 14. Consulta 4: nacionalidades de actores en películas premiadas con “Premio BAFTA”
 * 15. Salir
 */

import com.cine.entidades.*;
import com.cine.repositorio.*;
import org.hibernate.HibernateException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class App {

    // DAOs
    private static final ActorDAO actorDAO = new ActorDAO();
    private static final PeliculaDAO peliculaDAO = new PeliculaDAO();
    private static final PremioDAO premioDAO = new PremioDAO();
    private static final SalaDAO salaDAO = new SalaDAO();
    private static final ProyeccionDAO proyeccionDAO = new ProyeccionDAO();

    // Scanner para leer consola
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Antes de arrancar el menú, Hibernate validará la estructura de la DB.
        // Si hubiera algún fallo en las tablas/columnas, lanzará excepción aquí.
        try {
            HibernateUtil.getSessionFactory().openSession().close();
        } catch (HibernateException he) {
            System.err.println("Error validando la base de datos: " + he.getMessage());
            System.exit(1);
        }

        int opcion = 0;
        while (opcion != 15) {
            mostrarMenu();
            opcion = pedirInt("Elige una opción (1-15): ");

            switch (opcion) {
                case 1:
                    crearActor();
                    break;
                case 2:
                    eliminarActor();
                    break;
                case 3:
                    crearPelicula();
                    break;
                case 4:
                    eliminarPelicula();
                    break;
                case 5:
                    crearPremio();
                    break;
                case 6:
                    eliminarPremio();
                    break;
                case 7:
                    modificarGeneroPelicula();
                    break;
                case 8:
                    asignarPremioAPelicula();
                    break;
                case 9:
                    asignarActorAPelicula();
                    break;
                case 10:
                    asignarPeliculaASala();
                    break;
                case 11:
                    consulta1();
                    break;
                case 12:
                    consulta2();
                    break;
                case 13:
                    consulta3();
                    break;
                case 14:
                    consulta4();
                    break;
                case 15:
                    System.out.println("Saliendo... ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
            System.out.println();
        }

        // Cerramos SessionFactory al final
        // Al terminar, cerramos el SessionFactory
        HibernateUtil.getSessionFactory().close(); // o si tienes un método shutdown(), llámalo aquí
        sc.close();
    }

    private static void mostrarMenu() {
        String mensaje =
                "1.  Crear actor\n" +
                        "2.  Eliminar actor\n" +
                        "3.  Crear pelicula\n" +
                        "4.  Eliminar pelicula\n" +
                        "5.  Crear premio\n" +
                        "6.  Eliminar premio\n" +
                        "7.  Modificar género de una película\n" +
                        "8.  Asignar un premio a una película\n" +
                        "9.  Asignar un actor a una película\n" +
                        "10. Asignar una película a una sala en fecha y hora actuales\n" +
                        "11. Consulta 1 (Actores estadounidenses por fechaNacimiento desc)\n" +
                        "12. Consulta 2 (Actores en película X)\n" +
                        "13. Consulta 3 (Películas proyectadas en fecha)\n" +
                        "14. Consulta 4 (Nacionalidades de actores en película premiada con BAFTA)\n" +
                        "15. Salir";
        System.out.println(mensaje);
    }

    /**
     * Lee un entero desde consola (con bucle de validación)
     */
    private static int pedirInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " ");
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número entero válido.");
            }
        }
    }

    /**
     * Lee un String desde consola
     */
    private static String pedirString(String mensaje) {
        System.out.print(mensaje + " ");
        return sc.nextLine().trim();
    }

    // ----------------- OPCIÓN 1: Crear actor -----------------
    private static void crearActor() {
        System.out.println("+++ CREAR ACTOR +++");
        String nombre = pedirString("Nombre:");
        String fechaStr = pedirString("Fecha de nacimiento (yyyy-MM-dd):");
        String nacionalidad = pedirString("Nacionalidad:");

        try {
            LocalDate fechaNac = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            Actor a = new Actor(nombre, fechaNac, nacionalidad);
            actorDAO.salvar(a);
            System.out.println("Actor creado con ID = " + a.getId());
        } catch (Exception e) {
            System.out.println("Error al crear Actor: " + e.getMessage());
        }
    }

    // ----------------- OPCIÓN 2: Eliminar actor -----------------
    private static void eliminarActor() {
        System.out.println("+++ ELIMINAR ACTOR +++");
        int id = pedirInt("ID del actor a eliminar:");
        Actor a = actorDAO.buscarPorId(id);
        if (a == null) {
            System.out.println("No existe un actor con ID = " + id);
            return;
        }
        actorDAO.borrar(a);
        System.out.println("Actor con ID " + id + " eliminado.");
    }

    // ----------------- OPCIÓN 3: Crear película -----------------
    private static void crearPelicula() {
        System.out.println("+++ CREAR PELÍCULA +++");
        String titulo = pedirString("Título:");
        int año = pedirInt("Año de estreno (ej: 2010):");
        String genero = pedirString("Género:");

        try {
            Pelicula p = new Pelicula(titulo, Year.of(año), genero);
            peliculaDAO.salvar(p);
            System.out.println("Película creada con ID = " + p.getId());
        } catch (Exception e) {
            System.out.println("Error al crear Pelicula: " + e.getMessage());
        }
    }

    // ----------------- OPCIÓN 4: Eliminar película -----------------
    private static void eliminarPelicula() {
        System.out.println("+++ ELIMINAR PELÍCULA +++");
        int id = pedirInt("ID de la película a eliminar:");
        Pelicula p = peliculaDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("No existe una película con ID = " + id);
            return;
        }
        peliculaDAO.borrar(p);
        System.out.println("Película con ID " + id + " eliminada.");
    }

    // ----------------- OPCIÓN 5: Crear premio -----------------
    private static void crearPremio() {
        System.out.println("+++ CREAR PREMIO +++");
        String nombrePrem = pedirString("Nombre del premio:");
        int año = pedirInt("Año del premio (ej: 2011):");

        try {
            Premio pr = new Premio(nombrePrem, Year.of(año));
            premioDAO.salvar(pr);
            System.out.println("Premio creado con ID = " + pr.getId());
        } catch (Exception e) {
            System.out.println("Error al crear Premio: " + e.getMessage());
        }
    }

    // ----------------- OPCIÓN 6: Eliminar premio -----------------
    private static void eliminarPremio() {
        System.out.println("+++ ELIMINAR PREMIO +++");
        int id = pedirInt("ID del premio a eliminar:");
        Premio pr = premioDAO.buscarPorId(id);
        if (pr == null) {
            System.out.println("No existe un premio con ID = " + id);
            return;
        }
        premioDAO.borrar(pr);
        System.out.println("Premio con ID " + id + " eliminado.");
    }

    // ----------------- OPCIÓN 7: Modificar género de una película -----------------
    private static void modificarGeneroPelicula() {
        System.out.println("+++ MODIFICAR GÉNERO DE PELÍCULA +++");
        int id = pedirInt("ID de la película a modificar:");
        Pelicula p = peliculaDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("No existe una película con ID = " + id);
            return;
        }
        String nuevoGenero = pedirString("Nuevo género para \"" + p.getTitulo() + "\":");
        p.setGenero(nuevoGenero);
        peliculaDAO.actualizar(p);
        System.out.println("Género actualizado. Ahora \"" + p.getTitulo() + "\" es de género \"" + p.getGenero() + "\".");
    }

    // ----------------- OPCIÓN 8: Asignar premio a una película -----------------
    private static void asignarPremioAPelicula() {
        System.out.println("+++ ASIGNAR PREMIO A PELÍCULA +++");
        int idPremio = pedirInt("ID del premio:");
        Premio pr = premioDAO.buscarPorId(idPremio);
        if (pr == null) {
            System.out.println("No existe un premio con ID = " + idPremio);
            return;
        }

        int idPelicula = pedirInt("ID de la película:");
        Pelicula p = peliculaDAO.buscarPorId(idPelicula);
        if (p == null) {
            System.out.println("No existe una película con ID = " + idPelicula);
            return;
        }

        // Verificamos si la película ya tiene un premio
        if (p.getPremio() != null) {
            System.out.println("Esa película ya tiene asignado un premio (" + p.getPremio().getNombrePremio() + ").");
            return;
        }

        // Asignamos bidireccionalmente
        pr.setPelicula(p); // internamente hará p.setPremio(this)
        premioDAO.actualizar(pr); // actualiza la relación
        System.out.println("Premio \"" + pr.getNombrePremio() + "\" asignado a=\"" + p.getTitulo() + "\".");
    }

    // ----------------- OPCIÓN 9: Asignar actor a una película -----------------
    private static void asignarActorAPelicula() {
        System.out.println("+++ ASIGNAR ACTOR A PELÍCULA +++");
        int idActor = pedirInt("ID del actor:");
        Actor a = actorDAO.buscarPorId(idActor);
        if (a == null) {
            System.out.println("No existe un actor con ID = " + idActor);
            return;
        }

        int idPelicula = pedirInt("ID de la película:");
        Pelicula p = peliculaDAO.buscarPorId(idPelicula);
        if (p == null) {
            System.out.println("No existe una película con ID = " + idPelicula);
            return;
        }

        // Verificamos si ya existe la relación
        if (p.getActores().contains(a)) {
            System.out.println("El actor \"" + a.getNombre() + "\" ya está asignado a \"" + p.getTitulo() + "\".");
            return;
        }

        // Asignamos bidireccional
        a.agregarPelicula(p);
        actorDAO.actualizar(a);
        // También podríamos hacer peliculaDAO.actualizar(p); pero actualizar "a" es suficiente porque cascade no afecta ManyToMany
        System.out.println("Actor \"" + a.getNombre() + "\" asignado a \"" + p.getTitulo() + "\".");
    }

    // ----------------- OPCIÓN 10: Asignar película a sala (fecha y hora actuales) -----------------
    private static void asignarPeliculaASala() {
        System.out.println("+++ ASIGNAR PELÍCULA A SALA (FECHA/HORA ACTUAL) +++");
        int idPelicula = pedirInt("ID de la película:");
        Pelicula p = peliculaDAO.buscarPorId(idPelicula);
        if (p == null) {
            System.out.println("No existe una película con ID = " + idPelicula);
            return;
        }

        int idSala = pedirInt("ID de la sala:");
        Sala s = salaDAO.buscarPorId(idSala);
        if (s == null) {
            System.out.println("No existe una sala con ID = " + idSala);
            return;
        }

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        // Creamos la proyección con clave compuesta
        Proyeccion pr = new Proyeccion();
        pr.setPelicula(p);
        pr.setSala(s);
        pr.setFecha(hoy);
        pr.setHorario(ahora);

        // Guardamos la proyección
        try {
            proyeccionDAO.salvar(pr);
            System.out.println("Proyección creada: \"" + p.getTitulo() + "\" en sala \"" + s.getNombre() +
                    "\" el " + hoy + " a las " + ahora);
        } catch (Exception e) {
            System.out.println("Error al asignar proyección: " + e.getMessage());
        }
    }

    // ----------------- OPCIÓN 11: Consulta 1 -----------------
    private static void consulta1() {
        System.out.println("+++ CONSULTA 1: Actores estadounidenses (nac. desc) +++");
        List<Actor> lista = actorDAO.listaActoresEUAOrdenadosPorNacimientoDesc();
        if (lista.isEmpty()) {
            System.out.println("No hay actores con nacionalidad Estadounidense.");
        } else {
            for (Actor a : lista) {
                System.out.println(a.getNombre() + "  |  Fecha Nac: " + a.getFechaNacimiento());
            }
        }
    }

    // ----------------- OPCIÓN 12: Consulta 2 -----------------
    private static void consulta2() {
        System.out.println("+++ CONSULTA 2: Actores en película dada +++");
        String titulo = pedirString("Introduce el título de la película:");
        List<Actor> lista = actorDAO.listaActoresPorTituloPelicula(titulo);
        if (lista.isEmpty()) {
            System.out.println("No se encontraron actores para \"" + titulo + "\" (o la película no existe).");
        } else {
            for (Actor a : lista) {
                System.out.println(a.getNombre() + "  |  Nacionalidad: " + a.getNacionalidad());
            }
        }
    }

    // ----------------- OPCIÓN 13: Consulta 3 -----------------
    private static void consulta3() {
        System.out.println("+++ CONSULTA 3: Películas proyectadas en fecha específica +++");
        String fechaStr = pedirString("Introduce la fecha (yyyy-MM-dd):");
        try {
            LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            List<Object[]> resultados = peliculaDAO.peliculasProyectadasEnFecha(fecha);
            if (resultados.isEmpty()) {
                System.out.println("No hay proyecciones para la fecha " + fecha);
            } else {
                for (Object[] fila : resultados) {
                    // fila[0] = título, fila[1] = género
                    System.out.println("Título: " + fila[0] + "  |  Género: " + fila[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("Formato de fecha incorrecto. Usa yyyy-MM-dd.");
        }
    }

    // ----------------- OPCIÓN 14: Consulta 4 -----------------
    private static void consulta4() {
        System.out.println("+++ CONSULTA 4: Nacionalidades actores en película con BAFTA +++");
        List<String> lista = peliculaDAO.nacionalidadesActoresEnBAFTA();
        if (lista.isEmpty()) {
            System.out.println("No hay actores en películas premiadas con “Premio BAFTA”.");
        } else {
            System.out.println("Nacionalidades encontradas:");
            for (String nat : lista) {
                System.out.println("  • " + nat);
            }
        }
    }
}