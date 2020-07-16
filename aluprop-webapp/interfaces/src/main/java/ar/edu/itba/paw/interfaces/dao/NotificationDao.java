package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.model.Notification;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;

import java.util.Collection;

public interface NotificationDao {
    Notification get(long id);
    Collection<Notification> getAll(PageRequest pageRequest);
    Long count(long userId);
    Collection<Notification> getAllNotificationsForUser(long id, PageRequest pageRequest);
    Collection<Notification> getAllUnreadNotificationsForUser(long id);
    Notification createNotification(Notification notification);
    void markRead(long notificationId);
    void delete(long id);
    Collection<Notification> getUnreadForUserWithProposal(User u, Proposal proposal);
}
