package pl.jp.api.analyze;

import java.time.OffsetDateTime;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AnalyzeResponse analyze(@RequestBody AnalyzeRequest analyzeRequest) {
        AnalyseDetails analyzeResponse = ImmutableAnalyseDetails.builder()
                .firstPost(OffsetDateTime.now())
                .lastPost(OffsetDateTime.now())
                .totalPosts(0)
                .totalAcceptedPosts(0)
                .avgScore(0)
                .build();

        return ImmutableAnalyzeResponse.builder()
                .analyseDate(OffsetDateTime.now())
                .details(analyzeResponse)
                .build();
    }
}
