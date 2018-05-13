package pl.jp.analyzer.api.analyze;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.OffsetDateTime;
import javax.xml.stream.XMLStreamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.jp.analyzer.analysis.PostStats;
import pl.jp.analyzer.analysis.XmlFileException;
import pl.jp.analyzer.analysis.XmlPostFileAnalysisService;

@RestController
@RequestMapping("/analyze")
class AnalyzeController {
    private final XmlPostFileAnalysisService xmlPostFileAnalysisService;

    AnalyzeController(XmlPostFileAnalysisService xmlPostFileAnalysisService) {
        this.xmlPostFileAnalysisService = xmlPostFileAnalysisService;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Could not download the file")
    @ExceptionHandler(FileNotFoundException.class)
    void fileNotFoundExceptionHandler() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Xml file cannot be parsed")
    @ExceptionHandler(XmlFileException.class)
    void xmlFileExceptionHandler() {
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    AnalyzeResponse analyze(@RequestBody AnalyzeRequest analyzeRequest) throws IOException, XMLStreamException {
        PostStats analysisDetails = xmlPostFileAnalysisService.analyze(analyzeRequest.url());
        return ImmutableAnalyzeResponse.builder()
                .analyseDate(OffsetDateTime.now())
                .details(analysisDetails)
                .build();
    }
}
