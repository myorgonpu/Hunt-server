
package main.java.database;

import main.java.User;



public interface UserRepository {

    User get(String login, String password);
    User create(User user) throws AlreadyExistingException;
    User update(User user);
    User removeUser(Integer id);
    User getAll();

}
