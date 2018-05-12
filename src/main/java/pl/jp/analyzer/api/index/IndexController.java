package pl.jp.analyzer.api.index;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/")
class IndexController {
    private static final ImmutableIndexResponse APPLICATION_NAME_RESPONSE = ImmutableIndexResponse.builder()
            .applicationName("xml-analyzer").build();

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    IndexResponse getIndex() {
        return APPLICATION_NAME_RESPONSE;
    }
}
