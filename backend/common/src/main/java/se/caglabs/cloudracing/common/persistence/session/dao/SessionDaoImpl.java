package se.caglabs.cloudracing.common.persistence.session.dao;

import se.caglabs.cloudracing.common.persistence.session.model.Session;

import java.util.Arrays;
import java.util.List;

public class SessionDaoImpl implements SessionDao {
    @Override
    public Session findSessionByToken(String token) {
        return Session.of("token", "userId");
    }

    @Override
    public List<Session> findSessionsForUser(String userId) {
        return Arrays.asList(Session.of("token","userId"), Session.of("token","userId"));
    }

    @Override
    public void saveSession(Session session) {

    }
}
