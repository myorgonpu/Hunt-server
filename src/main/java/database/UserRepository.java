
package main.java.database;

import main.java.User;

/**
 * Created by Marina on 12.02.2017.
 */
public interface UserRepository {
    User get(String login, String password);
    User create(User user) throws AlreadyExistingException;
    User delete(Integer id);

}
