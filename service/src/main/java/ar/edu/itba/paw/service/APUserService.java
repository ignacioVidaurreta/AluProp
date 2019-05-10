package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.dao.CareerDao;
import ar.edu.itba.paw.interfaces.dao.UniversityDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class APUserService implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UniversityDao universityDao;
    @Autowired
    private CareerDao careerDao;

    private List<String> errors;

    @Override
    public User get(long id) {
        return userDao.get(id);
    }

    @Override
    public User getByEmail(String username) {
        return userDao.getByEmail(username);
    }

    @Override
    public Either<User, List<String>> CreateUser(User user) {
        errors = new LinkedList<>();
        checkRelatedEntitiesExist(user);
        if(!errors.isEmpty())
            return Either.alternativeFrom(errors);
        return Either.valueFrom(userDao.create(user));
    }

    private void checkRelatedEntitiesExist(User user) {
        checkUniversityExists(user.getUniversityId());
        checkCareerExists(user.getCareerId());
    }

    private void checkUniversityExists(long universityId) {
        if(universityDao.get(universityId) == null)
            errors.add("The specified university does not exist");
    }

    private void checkCareerExists(long careerId) {
        if(careerDao.get(careerId) == null)
            errors.add("The specified career does not exist");
    }

    @Override
    public User getUserWithRelatedEntitiesByEmail(String email) {
        return userDao.getUserWithRelatedEntitiesByEmail(email);
    }
}
