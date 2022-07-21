package webserver.Request;

public class Header {

    private final String fieldName;
    private final String fieldKey;

    public Header(String fieldName, String fieldKey) {
        this.fieldName = fieldName;
        this.fieldKey = fieldKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldKey() {
        return fieldKey;
    }
}
