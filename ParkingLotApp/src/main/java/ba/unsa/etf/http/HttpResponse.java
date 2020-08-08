package ba.unsa.etf.http;


import javax.json.JsonArray;
import javax.json.JsonObject;

public class HttpResponse {
    private int code;
    private JsonArray message;

    public HttpResponse(int code, JsonArray message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JsonArray getMessage() {
        return message;
    }

    public void setMessage(JsonArray message) {
        this.message = message;
    }
}
