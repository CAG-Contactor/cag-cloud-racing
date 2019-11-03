package se.caglabs.cloudracing.common.persistence.session.dao;

import se.caglabs.cloudracing.common.persistence.session.model.Session;

import java.util.List;

public interface SessionDao {
    Session findSessionByToken(String token);
    List<Session> findSessionsForUser(String userId);
    void saveSession(Session session);
}
