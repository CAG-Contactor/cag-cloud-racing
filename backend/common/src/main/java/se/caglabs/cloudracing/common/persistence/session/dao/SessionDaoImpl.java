package se.caglabs.cloudracing.common.persistence.session.dao;

import se.caglabs.cloudracing.common.persistence.session.model.Session;

import java.util.Arrays;
import java.util.List;

public class SessionDaoImpl implements SessionDao {
    @Override
    public Session findSessionByToken(String token) {
        return Session.of("token", "userName");
    }

    @Override
    public List<Session> findSessionsForUser(String userName) {
        return Arrays.asList(Session.of("token","userName"), Session.of("token","userName"));
    }

    @Override
    public void saveSession(Session session) {

    }
}
