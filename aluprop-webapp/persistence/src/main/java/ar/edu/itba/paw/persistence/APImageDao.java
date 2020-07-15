package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.Image;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
public class APImageDao implements ImageDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Image get(long id) {
        return entityManager.find(Image.class, id);
    }

    @Override
    public Collection<Image> getByProperty(long propertyId) {
        final TypedQuery<Image> query = entityManager.createQuery("FROM Image i WHERE i.property.id = :propertyId", Image.class);
        query.setParameter("propertyId", propertyId);
        return query.getResultList();
    }

    @Override
    @Transactional
    public long create(byte[] image) {
        Image i = new Image(image);
        entityManager.persist(i);
        return i.getId();
    }

    @Override
    @Transactional
    public Collection<Image> getAll() {
        Collection<Image> images = entityManager.createQuery("FROM Image i WHERE i.property IS NOT NULL AND i.property.id IN (SELECT id FROM Property)", Image.class).getResultList();
        images.forEach(i -> i.getProperty().getDescription());
        return images;
    }

    @Override
    @Transactional
    public void update(Image image) {
        if (image == null || image.getId() < 1)
            return;
        entityManager.merge(image);
    }

    @Override
    @Transactional
    public void delete(Image image) {
        if(image == null || image.getId() < 1)
            return;
        entityManager.remove(image);
    }
}
