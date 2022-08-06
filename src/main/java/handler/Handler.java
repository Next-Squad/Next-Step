package handler;

import webserver.Request.Request;
import webserver.Response.Response;

public interface Handler {

    Response doGet(Request request, Response response);

    Response doPost(Request request, Response response);
}
