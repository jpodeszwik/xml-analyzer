package pl.jp.analyzer.api.analyze;

import com.google.common.io.Resources;
import java.net.URL;
import java.nio.charset.Charset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AnalyzeControllerRemoteFileTest {
    private static final int MOCK_SERVER_PORT = 1080;

    @Autowired
    private MockMvc mockMvc;

    private ClientAndServer mockServer;

    @Before
    public void setUp() {
        mockServer = startClientAndServer(MOCK_SERVER_PORT);
    }

    @After
    public void tearDown() {
        mockServer.stop();
    }

    @Test
    public void shouldDownloadAndProcessRemoteFile() throws Exception {
        var xmlBody = Resources.toString(Resources.getResource("two-posts.xml"), Charset.defaultCharset());
        mockServer.when(
                request()
                        .withPath("/file.xml")
        ).respond(
                response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(xmlBody)
        );

        String analyzeRequestBody = Helper.analyzeRequestJsonForUrl(new URL("http://localhost:" + MOCK_SERVER_PORT + "/file.xml"));

        this.mockMvc.perform(
                post("/analyze")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(analyzeRequestBody)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{'details' : {'totalPosts': 2, 'avgScore': 7}}"));
    }

    @Test
    public void forUrlReturning404_shouldReturnBadRequest() throws Exception {
        mockServer.when(request())
                .respond(response()
                        .withStatusCode(HttpStatusCode.NOT_FOUND_404.code())
                );

        String analyzeRequestBody = Helper.analyzeRequestJsonForUrl(new URL("http://localhost:" + MOCK_SERVER_PORT + "/file.xml"));

        this.mockMvc.perform(
                post("/analyze")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(analyzeRequestBody)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void forInvalidXmlFile_shouldReturnBadRequest() throws Exception {
        var xmlBody = Resources.toString(Resources.getResource("invalid-file.xml"), Charset.defaultCharset());
        mockServer.when(
                request()
                        .withPath("/file.xml")
        ).respond(
                response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(xmlBody)
        );

        String analyzeRequestBody = Helper.analyzeRequestJsonForUrl(new URL("http://localhost:" + MOCK_SERVER_PORT + "/file.xml"));

        this.mockMvc.perform(
                post("/analyze")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(analyzeRequestBody)
        )
                .andExpect(status().isBadRequest());
    }
}
