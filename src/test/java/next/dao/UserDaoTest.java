package next.dao;



import static org.assertj.core.api.Assertions.assertThat;

import next.model.User;
import org.junit.jupiter.api.Test;

public class UserDaoTest {

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert(expected);

        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

}
