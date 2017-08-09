package org.kelvin.webapp;



import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.kelvin.webapp.apiObjects.*;
import org.kelvin.webapp.schedule.LifeTask;
import org.kelvin.webapp.tools.CommonUtils;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Path("/api")
@Singleton
public class FakeApi {

    private Gson gson = new GsonBuilder()
            .setDateFormat(CommonUtils.DateType.API_DATE.dateFormat.toPattern()).create();
    JsonParser parser = new JsonParser();
    private FakeApiServer fakeData;

    public FakeApi() {
        if (fakeData == null) {
            fakeData = new FakeApiServer();
        }
    }

    @POST
    @Consumes("application/json")
    @Path("post-example/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response postExample(LifeTask task) {
        int pause=0;
        pause++;
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity("test").build();
    }

    @GET
    @Path("{param}")
    public Response getMessage(@PathParam("param") String message) {
        return Response.status(200).entity("You have successfully sent this message: \n\n \"" + message + "\"").build();
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
                fakeData.getRefreshToken(refreshToken, grantType) :
                fakeData.getUserToken(userName, password);

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
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity("test").build();
    }


    @PUT
    @Consumes("application/json")
    @Path("put-example/{id}/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response putExample(@PathParam("id") String id, Object object) {
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity("test").build();
    }



    @POST
    @Path("post-with-file-example/")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response postWithFileExample(@PathParam("id") String id,
                                        @FormParam("file") InputStream in) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("testFile", "test");
            tempFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tempFile);
            IOUtils.copy(in, out);
        } catch (IOException e) {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity("test").build();
    }

    @DELETE
    @Path("delete-example/")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteExample(@PathParam("id") String id, @PathParam("filename") String filename) {
        return Response.status(ApiServer.HttpCode.SUCCESS.code).entity("test").build();
    }
}