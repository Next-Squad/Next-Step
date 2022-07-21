package webserver.Request;

public enum HttpMethod {

    GET, POST;

    public static HttpMethod from(String method) {
        if(method.equals(GET.name())) {
            return GET;
        }
        return POST;
    }

}
