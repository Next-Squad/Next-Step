package db;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils;

class DataBaseTest {

    @BeforeEach
    void setUp() {
        String queryString = "userId=donggi&password=123&name=이동기&email=af1505@naver.com";
        Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(queryString);
        User user = new User(
            parseQueryString.get("userId"),
            parseQueryString.get("password"),
            parseQueryString.get("name"),
            parseQueryString.get("email"));
        DataBase.addUser(user);
    }

    @Test
    void findById() {
        String userId = "donggi";
        User findUser = DataBase.findUserById(userId);
        assertThat(findUser.getUserId()).isEqualTo("donggi");
    }
}
