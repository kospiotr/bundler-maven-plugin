package com.github.kospiotr.bundler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


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
            verify(resourceAccess, never()).read(any(Path.class));
            verify(resourceAccess, never()).write(any(Path.class), any(String.class));
            verify(resourceAccess, never()).write(any(Path.class), any(String.class));
        }
    }

    @Test
    public void shouldProcessEmptyTag() throws Exception {
        Tag jsTag = createJsTag("", "app.js");
        String result = jsTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<script src=\"app.js\"></script>");
        verify(resourceAccess, never()).read(any(Path.class));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.js")), any(String.class));
    }

    @Test
    public void shouldProcessSingleTag() throws Exception {
        Tag jsTag = createJsTag("<script src=\"my/lib/path/lib.js\"></script>", "app.js");
        String result = jsTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<script src=\"app.js\"></script>");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib.js")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.js")), any(String.class));
    }

    @Test
    public void shouldProcessMultipleInlineTags() throws Exception {
        Tag jsTag = createJsTag("<script src=\"my/lib/path/lib1.js\"></script><script src=\"my/lib/path/lib2.js\"></script>", "app.js");
        String result = jsTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<script src=\"app.js\"></script>");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib1.js")));
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib2.js")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.js")), any(String.class));
    }

    @Test
    public void shouldProcessMultipleMultiLineTags() throws Exception {
        Tag jsTag = createJsTag("<script src=\"my/lib/path/lib1.js\"></script>\n<!-- sample comment -->\n<script src=\"my/lib/path/lib2.js\"></script>", "app.js");
        String result = jsTagProcessor.process(jsTag);

        assertThat(result).isEqualTo("<script src=\"app.js\"></script>");
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib1.js")));
        verify(resourceAccess).read(argThat(new PathHamcrestMatcher("glob:**/lib2.js")));
        verify(resourceAccess).write(argThat(new PathHamcrestMatcher("glob:**/app.js")), any(String.class));
    }

    private Tag createJsTag(String content, String... attributes) {
        return new Tag(content, "js", attributes);
    }

}