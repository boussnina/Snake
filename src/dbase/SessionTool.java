package dbase;


import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

public class SessionTool
{
	//This is the constructor, which accepts a session factory and save it in a local variable
    private final SessionFactory sessionFactory;

    public SessionTool(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    //methods for CRUD @ database
    @Transactional
    public void save(Object object)
    {
        Session session = this.getCurrentSession();
        session.save(object);
    }

    @Transactional
    public void update(Object object)
    {
        Session session = this.getCurrentSession();
        session.update(object);
    }
    
    @Transactional
    public void saveOrUpdate(Object object)
    {
        Session session = this.getCurrentSession();
        session.saveOrUpdate(object);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public <E> E get(Class<E> clazz, Serializable id)
    {
        Session session = this.getCurrentSession();
        E element = (E) session.get(clazz, id);
        return element;
    }

    @Transactional
    public void delete(Object object)
    {
        Session session = this.getCurrentSession();
        session.delete(object);
    }
}