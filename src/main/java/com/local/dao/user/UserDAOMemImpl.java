package com.local.dao.user;

import com.local.dao.Persistable;
import com.local.model.User;
import com.local.util.DiskPersistenceManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDAOMemImpl implements UserDAO, Persistable {
    private ConcurrentHashMap<Integer, User> users;
    private AtomicInteger autoGeneratedId;
    private DiskPersistenceManager<Integer, User> diskPersistenceManager;

    public UserDAOMemImpl() {
        users = new ConcurrentHashMap<>();
        autoGeneratedId = new AtomicInteger(1);
        diskPersistenceManager = new DiskPersistenceManager<>(this, users, Integer.class, User.class);
    }

    @Override
    public void loadData(){
        diskPersistenceManager.loadData();
    }

    @Override
    public void persistData(){
        diskPersistenceManager.persistData();
    }

    @Override
    public User addUser(User user) {
        int id = autoGeneratedId.getAndIncrement();
        user.setId(id);
        users.put(id, user);
        return new User(user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User getUserById(int id) {
        User user = users.get(id);
        return new User(user);
    }

    @Override
    public User getUserByUsername(String username) {
        User user = users.searchValues(16, (u) -> u.getUsername().equals(username) ? u : null);
        if(user != null){
            return new User(user);
        }
        return null;
    }
}
