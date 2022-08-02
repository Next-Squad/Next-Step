package http;
import java.io.*;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class HttpRequestTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception{
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest request = new HttpRequest(in);


        assertThat("GET").isEqualTo(request.getMethod());
        assertThat("/user/create").isEqualTo(request.getPath());
        assertThat("keep-alive").isEqualTo(request.getHeader("Connection"));
        assertThat("javajigi").isEqualTo(request.getParameter("userId"));

    }
}
