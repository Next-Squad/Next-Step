package http;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void responseForward() throws Exception{
        //http_forward.txt 결과는 응답 body에 index.html이 포함되어 있어야 한다.
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
        response.forward("/index.html");
    }

    @Test
    public void responseRedirect() throws Exception{
        // http_redirect.txt 결과는 응답 header에
        // location 정보가 /index.html로 포함되어 있어야 한다
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
        response.sendRedirect("/index.html");
    }

    @Test
    public void responseCookies() throws Exception{
        //http_cookie.txt 결과는 응답 header에 set-cookie 값으로
        //logined=true 값이 포함되어 있어야 한다.
        HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
