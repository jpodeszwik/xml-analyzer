package pl.jp.api.index;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
class IndexController {
    private static final ImmutableIndexResponse APPLICATION_NAME_RESPONSE = ImmutableIndexResponse.builder()
            .applicationName("xml-analyzer").build();

    @GetMapping
    IndexResponse getIndex() {
        return APPLICATION_NAME_RESPONSE;
    }
}
