package Miary.miniWeb.Controller;

import Miary.miniWeb.diary.Diary;
import Miary.miniWeb.diary.music.Content;
import Miary.miniWeb.service.DiaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;


import javax.servlet.http.HttpServletResponse;
import java.util.Map;



@RestController
@Slf4j
public class MusicController{

    @Autowired
    private DiaryService diaryService;
    @Autowired
    private DiaryController diaryController;

    private String clientId = "kd9peerq63";
    private String clientSecret = "kIt19rB5YFihpiKdDVcOi4CX4SoMmULYrmLyIhZT";
    private String apiUrl = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";

    WebClient webClient =  WebClient.builder()
                            .defaultHeader("X-NCP-APIGW-API-KEY-ID", clientId)
                            .defaultHeader("X-NCP-APIGW-API-KEY", clientSecret)
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();

    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value="/diary/{diaryIdx}/music")
    public ModelAndView music(@PathVariable("diaryIdx") Long diaryIdx) throws JsonProcessingException {
        ModelAndView modelAndView = new ModelAndView();

        Diary diary = diaryService.findDiaryById(diaryIdx);
        String content = diary.getContent();

        Content requestData = new Content(content);

        String requestJsonContent = mapper.writeValueAsString(requestData);

        String block = webClient.post()
                .uri(apiUrl)
                .body(BodyInserters.fromValue(requestJsonContent))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String resultSentiment = responseToJson(block);

        modelAndView.setViewName("diary/music");
        modelAndView.addObject("sentiment", resultSentiment);

        return modelAndView;
    }


    public String responseToJson(String response){
        try{
            Map<String, Object> result = mapper.readValue(response, new TypeReference<Map<String, Object>>() {});

            Map<String, String> document = (Map<String, String>) result.get("document");
            String sentiment = document.get("sentiment");

            return sentiment;
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "Error";
    }

}
