package io.github.kospiotr.bundler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(MockitoJUnitRunner.class)
public class JsTagProcessorTest {

    @Spy
    ProcessMojo processMojo = new ProcessMojo(new File("index-dev.html"), new File("index.html"));

    @Mock
    ResourceAccess resourceAccess;

    @InjectMocks
    JsTagProcessor jsTagProcessor;

    @Test
    public void shouldRejectWhenNoFileNameAttributeGiven() throws Exception {
        Tag jsTag = createJsTag("");

        try {
            jsTagProcessor.process(jsTag);
            fail("Should have thrown exception");
        } catch (Exception e) {
            assertThat(e).hasMessage("File Name attribute is required");
        }
    }

    @Test
    public void shouldProcessEmptyTag() throws Exception {
        Tag jsTag = createJsTag("", "app.js");
        String result = jsTagProcessor.process(jsTag);
        assertThat(result).isEqualTo("<script src=\"app.js\"></script>");
    }

    @Test
    public void shouldProcessSingleTag() throws Exception {
        Tag jsTag = createJsTag("<script src=\"my/lib/path/lib.js\"></script>", "app.js");
        String result = jsTagProcessor.process(jsTag);
        assertThat(result).isEqualTo("<script src=\"app.js\"></script>");
    }

    @Test
    public void shouldProcessMultipleInlineTags() throws Exception {
        Tag jsTag = createJsTag("<script src=\"my/lib/path/lib1.js\"></script><script src=\"my/lib/path/lib2.js\"></script>", "app.js");
        String result = jsTagProcessor.process(jsTag);
        assertThat(result).isEqualTo("<script src=\"app.js\"></script>");
    }

    @Test
    public void shouldProcessMultipleMultiLineTags() throws Exception {
        Tag jsTag = createJsTag("<script src=\"my/lib/path/lib1.js\"></script>\n<!-- sample comment -->\n<script src=\"my/lib/path/lib2.js\"></script>", "app.js");
        String result = jsTagProcessor.process(jsTag);
        assertThat(result).isEqualTo("<script src=\"app.js\"></script>");
    }

    private Tag createJsTag(String content, String... attributes) {
        return new Tag(content, "js", attributes);
    }

}