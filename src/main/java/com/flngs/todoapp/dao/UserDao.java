package com.flngs.todoapp.dao;

import com.flngs.todoapp.model.User;

import javax.persistence.*;

public class UserDao {

    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.flngs.todoapp.model");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void registerUser(User user) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    public int getUser(String username, String password) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        TypedQuery<User> query = em.createQuery("SELECT user FROM User user WHERE username = :username" +
                                                        " AND password = :password", User.class);
        query.setParameter("username", username)
                .setParameter("password", password);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
        em.getTransaction().commit();
        em.close();
        return 1;
    }
}
