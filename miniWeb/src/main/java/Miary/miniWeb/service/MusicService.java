package Miary.miniWeb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.function.Consumer;

@Service
@Slf4j
public class MusicService {
    private String clientId = "kd9peerq63";
    private String clientSecret = "kIt19rB5YFihpiKdDVcOi4CX4SoMmULYrmLyIhZT";
    private String apiUrl = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";

    private WebClient webClient
            =  WebClient
            .builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public String musicTest() throws SQLException {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("content", "와 기분 좋다");

        webClient.post()
                .uri(apiUrl)
                .header("X-NCP-APIGW-API-KEY-ID", clientId)
                .header("X-NCP-APIGW-API-KEY", clientSecret)
                .body(BodyInserters.fromFormData(data))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(ss -> log.info("result is {}", ss));

        return "Umm";
    }
}
