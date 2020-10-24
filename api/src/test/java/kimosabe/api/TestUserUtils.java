package kimosabe.api;

import kimosabe.api.entity.FriendAnswerRequestBody;
import kimosabe.api.entity.FriendInviteRequestBody;
import kimosabe.api.entity.LoginDetailsRequestBody;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public class TestUserUtils {
    public static final UUID user1Id = UUID.fromString("41096c69-47db-4fdb-9a84-bef10e571546");
    public static final UUID user2Id = UUID.fromString("b72cfbb3-565d-4711-81ad-e6cdf1f349c1");
    public static HttpHeaders loginUser1(TestRestTemplate restTemplate, int portNumber) {
        String loginUrl = "http://localhost:"+ portNumber + "/auth/login";
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user1");
        requestBody.setPassword("password1");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);
        ResponseEntity<String> resp = restTemplate.postForEntity(loginUrl, request, String.class);
        List<String> cookie = resp.getHeaders().get("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookie);
        return headers;
    }

    public static HttpHeaders loginUser2(TestRestTemplate restTemplate, int portNumber) {
        String loginUrl = "http://localhost:"+ portNumber + "/auth/login";
        LoginDetailsRequestBody requestBody = new LoginDetailsRequestBody();
        requestBody.setUsername("user2");
        requestBody.setPassword("password1");
        HttpEntity<LoginDetailsRequestBody> request = new HttpEntity<>(requestBody);
        ResponseEntity<String> resp = restTemplate.postForEntity(loginUrl, request, String.class);
        List<String> cookie = resp.getHeaders().get("Set-Cookie");
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookie);
        return headers;
    }

    public static HttpHeaders user1CreateNewGroupBaldursGate(TestRestTemplate restTemplate, int portNumber) {
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, portNumber);
        String groupUrl = "http://localhost:" + portNumber + "/groups/baldur-s-gate-enhanced-edition";
        restTemplate.postForEntity(groupUrl, new HttpEntity<String>(headers), String.class);
        return headers;
    }

    public static HttpHeaders user1JoinBaldursGateGroup(TestRestTemplate restTemplate, int portNumber) {
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, portNumber);
        String groupUrl = "http://localhost:" + portNumber + "/groups/baldur-s-gate-enhanced-edition/join";
        restTemplate.postForEntity(groupUrl, new HttpEntity<String>(headers), String.class);
        return headers;
    }

    public static HttpHeaders user1RequestFriendUser2(TestRestTemplate restTemplate, int portNumber) {
        FriendInviteRequestBody requestBody= new FriendInviteRequestBody();
        requestBody.setFriendUsername("user2");
        HttpHeaders headers = TestUserUtils.loginUser1(restTemplate, portNumber);
        HttpEntity<FriendInviteRequestBody> request = new HttpEntity<>(requestBody, headers);
        restTemplate.postForEntity("http://localhost:"+ portNumber + "/user/friends", request, String.class);
        return headers;
    }

    public static HttpHeaders user2AcceptUser1FriendRequest(TestRestTemplate restTemplate, int portNumber) {
        user1RequestFriendUser2(restTemplate, portNumber);
        FriendAnswerRequestBody requestBody= new FriendAnswerRequestBody();
        requestBody.setFrom("user1");
        requestBody.setAccept(true);
        HttpHeaders headers = TestUserUtils.loginUser2(restTemplate, portNumber);
        HttpEntity<FriendAnswerRequestBody> request = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(
                "http://localhost:"+ portNumber + "/user/friends",
                HttpMethod.PUT, request,
                String.class
        );
        return headers;
    }
}
