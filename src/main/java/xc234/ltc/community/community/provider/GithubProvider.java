package xc234.ltc.community.community.provider;


import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import xc234.ltc.community.community.dto.AccessTokenDTO;
import xc234.ltc.community.community.dto.GithubUser;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDto)
    {
       MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create( mediaType,JSON.toJSONString(accessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/authorize")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
           String string =  response.body().string();
           String token = string.split("&")[0].split("=")[1];
           return token;
        } catch (IOException e) {

        }
        return null;
    }
    public GithubUser getUser(String accessToken)
    {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.github.com/user?access_token="+ accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser= JSON.parseObject(string, GithubUser.class);
            return githubUser;
        }catch (IOException e)
        {
        return null;
        }
    }
}
