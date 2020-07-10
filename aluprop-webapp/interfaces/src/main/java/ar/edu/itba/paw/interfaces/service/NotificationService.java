package ar.edu.itba.paw.interfaces.service;

import java.util.Collection;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.model.Notification;
import ar.edu.itba.paw.model.User;

public interface NotificationService {
    PageResponse<Notification> getAllNotificationsForUser(PageRequest pageRequest);
    Collection<Notification> getAllUnreadNotificationsForUser();
    Notification createNotification(long userId, String subjectCode, String textCode, String link);
    void sendNotifications(String subjectCode, String textCode, String link, Collection<User> users, long currentUserId);
    void sendNotification(String subjectCode, String textCode, String link, User user);
    void markRead(long notificationId);
    void delete(long id);
}
