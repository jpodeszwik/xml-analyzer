package pl.jp.api.analyze;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.OffsetDateTime;
import javax.xml.stream.XMLStreamException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analyze")
public class AnalyzeController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AnalyzeResponse analyze(@RequestBody AnalyzeRequest analyzeRequest) throws IOException, XMLStreamException {
        URL website = new URL(analyzeRequest.url());
        try (InputStream in = website.openStream()) {
            AnalyzeDetails analyzeDetails = PostAnalyzer.analyze(in);

            return ImmutableAnalyzeResponse.builder()
                    .analyseDate(OffsetDateTime.now())
                    .details(analyzeDetails)
                    .build();
        }
    }
}
