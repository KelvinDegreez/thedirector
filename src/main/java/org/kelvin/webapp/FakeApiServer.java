package org.kelvin.webapp;


import org.kelvin.webapp.apiObjects.*;
import org.kelvin.webapp.director.Director;
import org.kelvin.webapp.director.TestDirector;
import org.kelvin.webapp.googleAPIs.GoogleCalendarApi;
import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.schedule.TestScheduleDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class FakeApiServer implements ApiServer {

    private Random ran = new Random();
    private Map<String, OAuthToken> apiRefreshTokenStringUserTokenMap = new HashMap<>();
    private Director director;
    private TestScheduleDatabase db;

    public FakeApiServer(){
        db = TestScheduleDatabase.createWithFakeData();
        //TODO: Default init should automatically be done to setup db
        director = new TestDirector();
        director.setScheduleDatabase(db);
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

    @Override
    public DataResult<String> getAnswer_CanIDoToday(LifeTask task){
            boolean answer = director.canDoToday(task);
            String message = "Director: \"You can"+(answer ? "" : "\'t")+" do \""+task.getName()+"\" today.\"";
            return new DataResult<>(HttpCode.SUCCESS, "Success", message);
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
