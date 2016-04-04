package com.github.kospiotr.bundler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class RelativizeResourcesTest {

    private static final Object[][] CONFIG = new Object[][]{
            {"src/base/image.png", "src/base/target/app.css", "../image.png"},
            {"src/base/image.png", "src/base/app.css", "image.png"},
            {"src/base/image.png", "src/app.css", "base/image.png"},

            {"src/image.png", "src/base/target/app.css", "../../image.png"},
            {"src/image.png", "src/base/app.css", "../image.png"},
            {"src/image.png", "src/app.css", "image.png"},

            {"src/base/lib/image.png", "src/base/target/app.css", "../lib/image.png"},
            {"src/base/lib/image.png", "src/base/app.css", "lib/image.png"},
            {"src/base/lib/image.png", "src/app.css", "base/lib/image.png"},
    };

    @Parameterized.Parameters(name = "{index} {0} {1} {2}")
    public static Collection<Object[]> data() {
        return asList(CONFIG);
    }

    String resourcePath;
    String targetCssPath;
    String relativeResourcePath;

    public RelativizeResourcesTest(String resourcePath, String targetCssPath, String relativeResourcePath) {
        this.resourcePath = resourcePath;
        this.targetCssPath = targetCssPath;
        this.relativeResourcePath = relativeResourcePath;
    }

    private PathNormalizator pathNormalizator = new PathNormalizator();

    @Test
    public void test() throws Exception {
        assertThat(pathNormalizator.relativize(absolute(resourcePath), absolute(targetCssPath)))
                .isEqualTo(relativeResourcePath);
    }

    private Path absolute(String relativePath) {
        return FileSystems.getDefault().getPath(relativePath).toAbsolutePath();
    }

}