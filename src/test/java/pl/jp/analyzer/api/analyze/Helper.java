package pl.jp.analyzer.api.analyze;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;

class Helper {
    private static String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    static String analyzeRequestJsonForUrl(URL url) throws JsonProcessingException {
        return serializeJson(AnalyzeRequest.of(url));
    }
}
