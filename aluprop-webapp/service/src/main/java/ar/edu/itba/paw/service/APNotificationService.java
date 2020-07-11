package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.interfaces.dao.NotificationDao;
import ar.edu.itba.paw.interfaces.service.NotificationService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Notification;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.NotificationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@Service
public class APNotificationService implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationDao notificationDao;
    @Autowired
    private UserService userService;

    @Override
    public PageResponse<Notification> getAllNotificationsForUser(PageRequest pageRequest) {
        User u = userService.getCurrentlyLoggedUser();
        return new PageResponse<>(pageRequest,
                                notificationDao.count(u.getId()),
                                notificationDao.getAllNotificationsForUser(u.getId(), pageRequest));
    }

    @Override
    public Collection<Notification> getAllUnreadNotificationsForUser() {
        User u = userService.getCurrentlyLoggedUser();
        return notificationDao.getAllUnreadNotificationsForUser(u.getId());
    }

    @Override
    public Notification createNotification(long userId, String subjectCode, String textCode, String link) {
        Notification notification = new Notification.Builder()
                                                .withUser(userService.get(userId))
                                                .withSubjectCode(subjectCode)
                                                .withTextCode(textCode)
                                                .withLink(link)
                                                .withState(NotificationState.UNREAD)
                                                .build();
        return notificationDao.createNotification(notification);
    }

    @Override
    public void sendNotifications(String subjectCode, String textCode, String link, Collection<User> users, long currentUserId){
        for (User user: users){
            if (user.getId() == currentUserId)
                continue;
            Notification result = createNotification(user.getId(), subjectCode, textCode, link);
            if (result == null)
                logger.error("Failed to deliver notification to user with id: " + user.getId());
        }
    }

    @Override
    public void sendNotification(String subjectCode, String textCode, String link, User user){
        Notification result = createNotification(user.getId(), subjectCode, textCode, link);
        if (result == null)
            logger.error("Failed to deliver notification to user with id: " + user.getId());
    }

    @Override
    public void markRead(long notificationId) {
        Notification n = notificationDao.get(notificationId);
        User u = userService.getCurrentlyLoggedUser();
        if (n.getUser().getId() != u.getId()) return;
        notificationDao.markRead(notificationId);
    }

    @Override
    public void delete(long id) {
        notificationDao.delete(id);
    }
}
