package org.kelvin.webapp;


import org.kelvin.webapp.apiObjects.*;
import org.kelvin.webapp.schedule.LifeTask;

public interface ApiServer {

    //============= User API =================
    DataResult<OAuthToken> getUserToken(String username, String password);

    DataResult<OAuthToken> getRefreshToken(String refreshToken, String grantType);

    DataResult<String> getAnswer_CanIDoToday(LifeTask task);

    public enum HttpCode {
        SUCCESS(200),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404),
        UNPROCESSABLE_ENTITY(422),
        TOO_MANY_REQUESTS(429),
        REQUEST_TIMEOUT(408),
        INTERNAL_SERVER_ERROR(500),
        UNKNOWN(0);
        public int code;

        HttpCode(int code) {
            this.code = code;
        }

        public static HttpCode fromCode(int code) {
            for (HttpCode errorCode : HttpCode.values()) {
                if (errorCode.code == code) {
                    return errorCode;
                }
            }
            return UNKNOWN;
        }
    }

    public class DataResult<T> {
        private HttpCode code;
        private String message = "";
        private T value;

        public DataResult(){

        }

        public DataResult(HttpCode code, String message, T value){
            this.code = code;
            this.message = message;
            this.value = value;
        }

        public DataResult(HttpCode code, String message){
            this.code = code;
            this.message = message;
        }

        public HttpCode getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getValue() {
            return value;
        }

        public void setCode(HttpCode code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

}
