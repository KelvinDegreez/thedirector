package org.kelvin.webapp.apiObjects;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class OAuthToken implements Serializable {

    @SerializedName("access_token")
    public String access_token = null;
    @SerializedName("expires_in")
    public Integer expires_in = null;
    @SerializedName("refresh_token")
    public String refresh_token = null;
    @SerializedName("refresh_expires_in")
    public Integer refresh_expires_in = null;
    @SerializedName("token_type")
    public String token_type = null;
    @SerializedName("id_token")
    public String id_token = null;
    @SerializedName("not-before-policy")
    public String not_before_policy = null;
    @SerializedName("session-state")
    public String session_state = null;

    public long timeStamp;

    public OAuthToken() {
        this.timeStamp = System.currentTimeMillis();
    }

    public OAuthToken(String access_token, Integer expires_in, String refresh_token, Integer refresh_expires_in, String token_type, String id_token, String not_before_policy, String session_state) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.refresh_expires_in = refresh_expires_in;
        this.token_type = token_type;
        this.id_token = id_token;
        this.not_before_policy = not_before_policy;
        this.session_state = session_state;
        this.timeStamp = System.currentTimeMillis();
    }

    public OAuthToken(String access_token, Integer expires_in, String refresh_token, Integer refresh_expires_in, long timeStamp) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.refresh_expires_in = refresh_expires_in;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "OAuthToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_expires_in='" + refresh_expires_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", id_token='" + id_token + '\'' +
                ", not_before_policy='" + not_before_policy + '\'' +
                ", session_state='" + session_state + '\'' +
                '}';
    }

}
