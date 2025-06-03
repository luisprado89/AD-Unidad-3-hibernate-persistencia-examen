package com.cine.repositorio;

import com.cine.HibernateUtil;
import com.cine.entidades.Sala;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SalaDAO {

    public void salvar(Sala s) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // Hibernate 7 usa persist para insertar un entity nuevo
        session.persist(s);
        //session.save(s);
        tx.commit();
        session.close();
    }

    public void actualizar(Sala s) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // merge devuelve una instancia gestionada; opcionalmente podrías usarlo así:
        // Sala managed = session.merge(s);
        session.merge(s);
        //session.update(s);
        tx.commit();
        session.close();
    }

    public void borrar(Sala s) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // remove elimina la entidad
        session.remove(s);
        //session.delete(s);
        tx.commit();
        session.close();
    }

    public Sala buscarPorId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        // find es el equivalente moderno a get()
        Sala s = session.find(Sala.class, id);
        //Sala s = session.get(Sala.class, id);
        session.close();
        return s;
    }

    @SuppressWarnings("unchecked")
    public List<Sala> listarTodas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Sala> lista = session.createQuery("from Sala").list();
        session.close();
        return lista;
    }
}
