package test.java;

import main.java.User;
import main.java.database.AlreadyExistingException;
import main.java.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Slon on 16.02.2017.
 */
public class RepositoryStub implements UserRepository {
    private ArrayList<User> users = new ArrayList<>();

    public RepositoryStub() {
        users.add(new User("pupok666", "12345", 0));
        users.add(new User("vasil", "qwerty", 0));
    }

    @Override
    public User get(String login, String password) {
        User user = null;
        for(User u: users) {
            if(u.getLogin().equals(login) && u.getPassword().equals(password)) {
                user = u;
            }
        }
        return user;
    }

    @Override
    public User create(User user) throws AlreadyExistingException {
        return null;
    }

    @Override
    public User update(User user) {
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getId() == user.getId()) {
                users.set(i, user);
            }
        }
        return user;
    }

    @Override
    public User removeUser(Integer id) {
        return null;
    }

    @Override
    public List<User> getAll() {

        return null;
    }

    public String getScores() {
        StringBuilder scores = new StringBuilder();
        for(User user: users) {
            scores.append(user.getLogin() + " " + user.getScore() + "\n");
        }
        return scores.toString();
    }

}
