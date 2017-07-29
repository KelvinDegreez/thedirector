package org.kelvin.webapp;


import org.kelvin.webapp.apiObjects.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class FakeApiServer implements ApiServer {

    private Random ran = new Random();
    private Map<String, OAuthToken> apiRefreshTokenStringUserTokenMap = new HashMap<>();

    public FakeApiServer(){

    }

    //============= User API =================
    @Override
    public DataResult<OAuthToken> getUserToken(String username, String password){
        if(username == null) {
            return new DataResult<OAuthToken>(
                    HttpCode.BAD_REQUEST,
                    "Username is empty");
        }else if(password == null) {
            return new DataResult<OAuthToken>(
                    HttpCode.BAD_REQUEST,
                    "Password is empty");
        }else if(password.equals("fail")){
            return new DataResult<OAuthToken>(
                    HttpCode.UNAUTHORIZED,
                    "Invalid Password or Username");
        }
//        OAuthToken token = DataGenerator.generateUserToken();
        apiRefreshTokenStringUserTokenMap.put(new OAuthToken().refresh_token, new OAuthToken());
        return new DataResult<OAuthToken>(HttpCode.SUCCESS, "Success", new OAuthToken());
    }

    @Override
    public DataResult<OAuthToken> getRefreshToken(String refreshToken, String grantType){
        if(refreshToken == null) {
            return new DataResult<OAuthToken>(
                    HttpCode.BAD_REQUEST,
                    "Refresh Token is empty");
        }else if(grantType == null) {
            return new DataResult<OAuthToken>(
                    HttpCode.BAD_REQUEST,
                    "Grant Type is empty");
        }else if(!apiRefreshTokenStringUserTokenMap.containsKey(refreshToken)){
            return new DataResult<OAuthToken>(
                    HttpCode.UNAUTHORIZED,
                    "Refresh Token not found");
        }else{
            apiRefreshTokenStringUserTokenMap.remove(refreshToken);

//            OAuthToken token = DataGenerator.generateUserToken();
            apiRefreshTokenStringUserTokenMap.put(new OAuthToken().refresh_token, new OAuthToken());
            return new DataResult<OAuthToken>(HttpCode.SUCCESS, "Success", new OAuthToken());
        }
    }


    private DataResult generateFakeError(DataResult dataResult){
        String errorMessage;
//        HttpCode code = DataGenerator.ranListItem(Arrays.asList(HttpCode.values()));
        HttpCode code = HttpCode.NOT_FOUND;
        if(code == null){
            code = HttpCode.UNKNOWN;
        }
        switch (code) {
            case UNAUTHORIZED: errorMessage = "Access Token invalid"; break;
            case REQUEST_TIMEOUT: errorMessage = "Request timed out"; break;
            case FORBIDDEN: errorMessage = "Request forbidden"; break;
            case BAD_REQUEST:
                errorMessage = "Bad Request";
                break;
            case NOT_FOUND:
                errorMessage = "Request not found";
                break;
            case UNKNOWN:
                errorMessage = "Unknown Error";
                break;
            case UNPROCESSABLE_ENTITY: errorMessage = "Unprocessable Entity"; break;
            case SUCCESS:
                code = HttpCode.INTERNAL_SERVER_ERROR;
            case TOO_MANY_REQUESTS: errorMessage = "Too many requests"; break;
            case INTERNAL_SERVER_ERROR:
            default: errorMessage = "Internal Server Error"; break;
        }
        return new DataResult(code, errorMessage);
    }


}
