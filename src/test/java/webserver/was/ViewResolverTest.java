package webserver.was;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.FileUtils;
import webserver.http.HttpResponseModel;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ViewResolverTest {

    private ViewResolver viewResolver;

    @BeforeEach
    void setUp() {
        viewResolver = new ViewResolver(getTestWebAppPath());
    }

    // classpath:webapp 경로를 반환
    private String getTestWebAppPath() {
        String fileName = "/viewResolverTest.html";
        String fullPath = "webapp" + fileName;
        URL url = getClass().getClassLoader().getResource(fullPath);
        String path = url.getPath();

        String[] split = path.split(fileName);
        return split[0];
    }

    @Test
    void 문법에_맞는_파일과_모델이_들어오면_파일의_특정위치에_정보를_추가한다() {
        // given
        String testWebAppPath = getTestWebAppPath();
        byte[] expectedFile = FileUtils.readFile(testWebAppPath + "/viewResolverTestExpected.html");
        HttpResponseModel model = new HttpResponseModel();
        model.put("users", List.of(new User("jay", "1234", "김진완", "jay@mail.com")));

        // when
        byte[] bytes = viewResolver.resolveView("/viewResolverTest.html", model);

        System.out.println(new String(bytes));
        System.out.println("------------");
        System.out.println(new String(expectedFile));

        // then
        assertThat(bytes).isEqualTo(expectedFile);
    }
}