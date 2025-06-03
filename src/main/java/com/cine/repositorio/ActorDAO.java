package com.cine.repositorio;

import com.cine.HibernateUtil;
import com.cine.entidades.Actor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ActorDAO {
    /**
     * Guarda una nueva Sala en la base de datos.
     * Con Hibernate 7 usamos session.persist(...) en lugar de session.save(...).
     */
    public void salvar(Actor a) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // Hibernate 7 usa persist para insertar un entity nuevo
        session.persist(a);
        //session.save(a);
        tx.commit();
        session.close();
    }
    /**
     * Actualiza una Sala existente.
     * En Hibernate 7 lo habitual es usar session.merge(...).
     */
    public void actualizar(Actor a) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.merge(a);
        //session.update(a);
        tx.commit();
        session.close();
    }
    /**
     * Elimina una Sala de la base de datos.
     * En Hibernate 7 usamos session.remove(...) en lugar de session.delete(...).
     */
    public void borrar(Actor a) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // remove elimina la entidad
        session.remove(a);
        //session.delete(a);
        tx.commit();
        session.close();
    }
    /**
     * Busca una Sala por su id.
     * El método session.get(...) ahora está marcado como obsoleto,
     * se recomienda usar session.find(...).
     */
    public Actor buscarPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        // find es el equivalente moderno a get()
        Actor a = session.find(Actor.class, id);
        //Actor a = session.get(Actor.class, id);
        session.close();
        return a;
    }

    @SuppressWarnings("unchecked")
    public List<Actor> listarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Actor> lista = session.createQuery("from Actor").list();
        session.close();
        return lista;
    }

    /**
     * Consulta 1: Obtener nombre de todos los actores estadounidenses
     * ordenados de forma descendente por fecha de nacimiento.
     */
    @SuppressWarnings("unchecked")
    public List<Actor> listaActoresEUAOrdenadosPorNacimientoDesc() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "from Actor a where a.nacionalidad = :nacio order by a.fechaNacimiento desc";
        Query<Actor> q = session.createQuery(hql);
        q.setParameter("nacio", "Estadounidense");
        List<Actor> resultado = q.list();
        session.close();
        return resultado;
    }

    /**
     * Consulta 2: Obtener nombre y nacionalidad de todos los actores que han
     * participado en una película dada (por título de la película).
     */
    @SuppressWarnings("unchecked")
    public List<Actor> listaActoresPorTituloPelicula(String tituloPelicula) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql =
                "select a from Actor a join a.peliculas p " +
                        "where p.titulo = :titulo";
        Query<Actor> q = session.createQuery(hql);
        q.setParameter("titulo", tituloPelicula);
        List<Actor> resultado = q.list();
        session.close();
        return resultado;
    }
}
