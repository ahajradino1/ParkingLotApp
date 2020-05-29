package ba.unsa.etf.http;


import javax.json.JsonObject;

public class HttpResponse {
    private int code;
    private JsonObject message;

    public HttpResponse(int code, JsonObject message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JsonObject getMessage() {
        return message;
    }

    public void setMessage(JsonObject message) {
        this.message = message;
    }
}
