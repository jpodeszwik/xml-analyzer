package pl.jp.analyzer.api.analyze;

import java.io.IOException;
import java.time.OffsetDateTime;
import javax.xml.stream.XMLStreamException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jp.analyzer.analysis.AnalysisDetails;
import pl.jp.analyzer.analysis.XmlPostFileAnalysisService;

@RestController
@RequestMapping("/analyze")
class AnalyzeController {
    private final XmlPostFileAnalysisService xmlPostFileAnalysisService;

    AnalyzeController(XmlPostFileAnalysisService xmlPostFileAnalysisService) {
        this.xmlPostFileAnalysisService = xmlPostFileAnalysisService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    AnalyzeResponse analyze(@RequestBody AnalyzeRequest analyzeRequest) throws IOException, XMLStreamException {
        AnalysisDetails analysisDetails = xmlPostFileAnalysisService.analyze(analyzeRequest.url());
        return ImmutableAnalyzeResponse.builder()
                .analyseDate(OffsetDateTime.now())
                .details(analysisDetails)
                .build();
    }
}
