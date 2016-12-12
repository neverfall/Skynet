package com.pernix_central.domain;

import java.util.List;

/**
 * Created by Gabriel on 18/11/2016.
 */
public class ParticipationWrapper {

    private Participation participation;

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Participation getParticipation() {
        return participation;
    }

    public void setParticipation(Participation participation) {
        this.participation = participation;
    }


}
