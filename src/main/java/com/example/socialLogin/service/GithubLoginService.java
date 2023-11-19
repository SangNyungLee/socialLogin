package com.example.socialLogin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubLoginService {

    //발급한 클라이언트 id
    @Value("${github.clientId}")
    private String clientId;

    //secret 코드
    @Value("${github.clientSecret}")
    private String clientSecret;

    //콜백 uri
    @Value("${github.redirectUri}")
    private String redirectUri;

    public void gitLogin(String code){
        String accessToken = getGithubAccessToken(code);
        System.out.println("AccessToken값은? : " + accessToken);
       JsonNode userResourceNode = getUserResource(accessToken);
        System.out.println("유저 리소스" + userResourceNode);

    }
    public String getGithubAccessToken(String code){
        //여기로 보내서 토큰 발급받아야됨
        String accessTokenUrl = "https://github.com/login/oauth/access_token";

        //헤더설정
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/json");

        //요청 파라미터 설정
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
        data.add("code", code);
        data.add("redirect_uri", redirectUri);

        //REST 템플릿을 사용하여 토큰 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(accessTokenUrl, new HttpEntity<>(data, headers), JsonNode.class);
        JsonNode accessTokenNode = response.getBody();
        return accessTokenNode.get("access_token").asText();

    }

    public JsonNode getUserResource(String accessToken){
        //이 주소로 accesstoken값으로 get 요청을 보내서 유저 정보를 받아옴
        String resourceUri = "https://api.github.com/user";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

}
