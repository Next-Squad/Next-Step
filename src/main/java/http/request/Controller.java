package http.request;

import http.response.HttpResponse;

public interface Controller {

    void service(HttpRequest request, HttpResponse response);

}
