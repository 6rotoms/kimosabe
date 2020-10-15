package kimosabe.api;

import kimosabe.api.entity.LoginDetailsRequestBody;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
}
