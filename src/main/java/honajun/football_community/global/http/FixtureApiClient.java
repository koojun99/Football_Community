package honajun.football_community.global.http;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FixtureApiClient {

    @Value("${football-api.base-url}")
    private String BASE_URL;

    @Value("${football-api.key}")
    private String API_KEY;

    @Value("${football-api.host}")
    private String API_HOST;

    private final OkHttpClient client;


    public FixtureApiClient() {
        this.client = new OkHttpClient();
    }

    public String getTeamFixtures(Long teamId, String season, int next, String timezone) throws IOException {
        // URL 구성
        String url = String.format(
                "%s?season=%s&team=%d&next=%d&timezone=%s",
                BASE_URL, season, teamId, next, timezone
        );

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", API_KEY)
                .addHeader("x-rapidapi-host", API_HOST)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixtures. HTTP Code: " + response.code());
            }
        }
    }

    public String getLeagueFixtures(Long leagueId, String season, int next, String timezone) throws IOException {
        // URL 구성
        String url = String.format(
                "%s?season=%s&league=%d&next=%d&timezone=%s",
                BASE_URL, season, leagueId, next, timezone
        );

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", API_KEY)
                .addHeader("x-rapidapi-host", API_HOST)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixtures. HTTP Code: " + response.code());
            }
        }
    }

    public String getFixture(Long fixtureId, String timezone) throws IOException {
        // URL 구성
        String url = String.format(
                "%s?id=%d&timezone=%s",
                BASE_URL, fixtureId, timezone
        );

        System.out.println("url = " + url);

        // 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", API_KEY)
                .addHeader("x-rapidapi-host", API_HOST)
                .build();

        // 요청 실행 및 응답 반환
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Failed to fetch fixtures. HTTP Code: " + response.code());
            }
        }
    }
}
