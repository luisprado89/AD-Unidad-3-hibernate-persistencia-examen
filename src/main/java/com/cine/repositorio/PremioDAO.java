package com.cine.repositorio;

import com.cine.HibernateUtil;
import com.cine.entidades.Premio;
import com.cine.entidades.Pelicula;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PremioDAO {

    public void salvar(Premio pr) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // Hibernate 7 usa persist para insertar un entity nuevo
        session.persist(pr);
        //session.save(pr);
        tx.commit();
        session.close();
    }
    /**
     * Actualiza una Sala existente.
     * En Hibernate 7 lo habitual es usar session.merge(...).
     */
    public void actualizar(Premio pr) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.merge(pr);
        //session.update(pr);
        tx.commit();
        session.close();
    }
    /**
     * Elimina una Sala de la base de datos.
     * En Hibernate 7 usamos session.remove(...) en lugar de session.delete(...).
     */
    public void borrar(Premio pr) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // remove elimina la entidad
        session.remove(pr);
        //session.delete(pr);
        tx.commit();
        session.close();
    }
    /**
     * Busca una Sala por su id.
     * El método session.get(...) ahora está marcado como obsoleto,
     * se recomienda usar session.find(...).
     */
    public Premio buscarPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        // find es el equivalente moderno a get()
        Premio p = session.find(Premio.class, id);
        //Premio p = session.get(Premio.class, id);
        session.close();
        return p;
    }

    /**
     * Asignar un premio a una película (bidireccional).
     * - comprueba que la película no tenga ya otro premio (pelicula_id UNIQUE)
     * - vincula el objeto Premio a Pelicula y persiste.
     */
    public void asignarPremioAPelicula(Premio pr, Pelicula p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // Si la película ya tiene premio asignado, p.getPremio() != null, se podría lanzar error.
        //p.setPremio(pr);
        //session.saveOrUpdate(pr);
       // session.saveOrUpdate(p);

        // Verificar si la película ya tiene premio (opcional, se puede hacer antes de llamar aquí)
        if (p.getPremio() != null) {
            tx.rollback();
            session.close();
            throw new IllegalStateException("La película ya tiene asignado un premio.");
        }
        // Asignamos bidireccionalmente
        p.setPremio(pr); // esto internamente hace pr.setPelicula(this)

        // Si pr es nuevo (id == null), usamos persist; si ya existía, usamos merge.
        if (pr.getId() == null) {
            session.persist(pr);
        } else {
            session.merge(pr);
        }

        // Ahora propagamos la relación en la película. Dado que p ya existe en BD:
        session.merge(p);


        tx.commit();
        session.close();
    }
}
