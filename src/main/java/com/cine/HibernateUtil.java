package com.cine;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY;

    static {
        try {
            // Aquí asumimos que hibernate.cfg.xml está en la carpeta raíz del proyecto
            String hibernatePropsFilePath = "src/hibernate.cfg.xml"; // Ruta al fichero

            File hibernatePropsFile = new File(hibernatePropsFilePath);

            SESSION_FACTORY = new Configuration().configure(hibernatePropsFile).buildSessionFactory();

        }catch(Throwable ex) {
            System.err.println("Error al crear la configuración de hibernate" + ex.getMessage());
            throw new ExceptionInInitializerError();
        }
    }
    /**
     * Devuelve el SessionFactory que se creó estáticamente.
     */
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}
