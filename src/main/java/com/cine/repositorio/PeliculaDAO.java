package com.cine.repositorio;

import com.cine.HibernateUtil;
import com.cine.entidades.Pelicula;
import com.cine.entidades.Premio;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.Year;
import java.util.List;

public class PeliculaDAO {
    /**
     * Guarda una nueva Sala en la base de datos.
     * Con Hibernate 7 usamos session.persist(...) en lugar de session.save(...).
     */
    public void salvar(Pelicula p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // Hibernate 7 usa persist para insertar un entity nuevo
        session.persist(p);
        //session.save(p);
        tx.commit();
        session.close();
    }
    /**
     * Actualiza una Sala existente.
     * En Hibernate 7 lo habitual es usar session.merge(...).
     */
    public void actualizar(Pelicula p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.merge(p);
        //session.update(p);
        tx.commit();
        session.close();
    }

    /**
     * Elimina una Sala de la base de datos.
     * En Hibernate 7 usamos session.remove(...) en lugar de session.delete(...).
     */
    public void borrar(Pelicula p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // remove elimina la entidad
        session.remove(p);
        //session.delete(p);
        tx.commit();
        session.close();
    }
    /**
     * Busca una Sala por su id.
     * El método session.get(...) ahora está marcado como obsoleto,
     * se recomienda usar session.find(...).
     */
    public Pelicula buscarPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        // find es el equivalente moderno a get()
        Pelicula p = session.find(Pelicula.class, id);
        //Pelicula p = session.get(Pelicula.class, id);
        session.close();
        return p;
    }

    @SuppressWarnings("unchecked")
    public List<Pelicula> listarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Pelicula> lista = session.createQuery("from Pelicula").list();
        session.close();
        return lista;
    }

    /**
     * Consulta 3: Listar título y género de todas las películas proyectadas
     * en una fecha específica (pasa la fecha como parámetro).
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> peliculasProyectadasEnFecha(java.time.LocalDate fecha) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql =
                "select p.titulo, p.genero " +
                        "from Proyeccion pr join pr.pelicula p " +
                        "where pr.id.fecha = :fecha";
        Query<Object[]> q = session.createQuery(hql);
        q.setParameter("fecha", fecha);
        List<Object[]> resultado = q.list();
        session.close();
        return resultado;
    }

    /**
     * Consulta 4: Obtener la lista de nacionalidades de los actores
     * que han participado en una película que fuera galardonada con un Premio BAFTA.
     */
    @SuppressWarnings("unchecked")
    public List<String> nacionalidadesActoresEnBAFTA() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql =
                "select distinct a.nacionalidad " +
                        "from Premio pr " +
                        "join pr.pelicula p " +
                        "join p.actores a " +
                        "where pr.nombrePremio = :nombreBAFTA";
        Query<String> q = session.createQuery(hql);
        q.setParameter("nombreBAFTA", "Premio BAFTA");
        List<String> resultado = q.list();
        session.close();
        return resultado;
    }
}
