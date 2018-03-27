package org.kelvin.webapp.standAlone;



import com.google.gson.*;
import org.kelvin.webapp.ApiServer;
import org.kelvin.webapp.FakeApiServer;
import org.kelvin.webapp.apiObjects.*;
import org.kelvin.webapp.director.DataValues;
import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.tools.CommonUtils;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(ServerGateway.path)
@Singleton
public class ServerGateway {

    final public static String path = "/director";

    private Gson gson = new GsonBuilder()
            .setDateFormat(CommonUtils.DateType.API_DATE.dateFormat.toPattern()).create();
    private FakeApiServer apiServer;

    public ServerGateway() {
        if (apiServer == null) {
            apiServer = new FakeApiServer();
        }
    }

    @POST
    @Consumes("application/json")
    @Path("post-example/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response postExample(LifeTask task) {
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity(gson.toJson(task)).build();
    }

    @GET
    @Path("{param}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMessage(@PathParam("param") String message) {


        return Response.status(200).entity("You have successfully sent this message: \n\n \"" + message + "\"").build();
    }

    @POST
    @Path("question/can-i-do-today/")
    @Consumes("application/json")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAnswerToQuestion(LifeTask task) {

        ApiServer.DataResult<String> response = apiServer.getAnswer_CanIDoToday(task);
        String msg;
        switch (response.getCode()){
            case SUCCESS:
                msg = response.getValue();
                break;
            default:
                msg = response.getValue();
                break;
        }
        return Response.status(response.getCode().code).entity(msg).build();
    }

    @POST
    @Path("token/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAccessToken(
            @FormParam("username") String userName,
            @FormParam("password") String password,
            @FormParam("refresh_token") String refreshToken,
            @FormParam("grant_type") String grantType) {

        boolean refreshLogin = refreshToken != null;

        if (refreshLogin)
            return Response.status(ApiServer.HttpCode.FORBIDDEN.code).entity("your code sucks").build();

        ApiServer.DataResult<OAuthToken> result = refreshLogin ?
                apiServer.getRefreshToken(refreshToken, grantType) :
                apiServer.getUserToken(userName, password);

        switch (result.getCode()) {
            case SUCCESS:
                String json = gson.toJson(result.getValue());
                return Response.status(result.getCode().code).entity(json).build();
            default:
                return Response.status(result.getCode().code).entity(result.getMessage()).build();
        }
    }

    @GET
    @Path("get-example/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getExample() {
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity("Success: GET").build();
    }

    @PUT
    @Consumes("application/json")
    @Path("put-example/{id}/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response putExample(@PathParam("id") String id, Object object) {
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity("Success:  PUT").build();
    }

    @DELETE
    @Path("delete-example/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteExample(@PathParam("id") String id, @PathParam("filename") String filename) {
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity("test").build();
    }
}