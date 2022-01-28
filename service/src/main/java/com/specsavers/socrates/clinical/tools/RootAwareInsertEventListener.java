package com.specsavers.socrates.clinical.tools;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RootAwareInsertEventListener
        implements PersistEventListener {

//    private static final Logger LOGGER =
//            LoggerFactory.getLogger(RootAwareInsertEventListener.class);

    public static final RootAwareInsertEventListener INSTANCE =
            new RootAwareInsertEventListener();

    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        final Object entity = event.getObject();

        if(entity instanceof RootAware) {
            RootAware rootAware = (RootAware) entity;
            Object root = rootAware.root();
            event.getSession().lock(root, LockMode.OPTIMISTIC_FORCE_INCREMENT);
            //LOGGER.info("Incrementing {} entity version because a {} child entity has been inserted", root, entity);
        }
    }

    @Override
    public void onPersist(PersistEvent event, Map createdAlready)
            throws HibernateException {
        onPersist(event);
    }
}
