package com.cine.repositorio;

import com.cine.HibernateUtil;
import com.cine.entidades.Proyeccion;
import com.cine.entidades.ProyeccionId;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProyeccionDAO {

    public void salvar(Proyeccion p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // merge devuelve una instancia gestionada; opcionalmente podrías usarlo así:
        // Sala managed = session.merge(s);
        session.merge(p);
        //session.save(p);
        tx.commit();
        session.close();
    }

    public void borrar(Proyeccion p) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // remove elimina la entidad
        session.remove(p);
        //session.delete(p);
        tx.commit();
        session.close();
    }

    public Proyeccion buscarPorId(ProyeccionId id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        // find es el equivalente moderno a get()
        Proyeccion p = session.find(Proyeccion.class, id);
        //Proyeccion p = session.get(Proyeccion.class, id);
        session.close();
        return p;
    }

    @SuppressWarnings("unchecked")
    public List<Proyeccion> listarTodas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Proyeccion> lista = session.createQuery("from Proyeccion").list();
        session.close();
        return lista;
    }
}
