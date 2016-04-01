package com.github.kospiotr.bundler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ResourcePathExtractorTest {

    private static final Object[][] CONFIG = new Object[][]{
            {"work/proj/src/base/index-dev.html", "url/app.css", "file/img.png", "work/proj/src/base/url/file/img.png"},
            {"work/proj/src/base/index-dev.html", "url/app.css", "img.png", "work/proj/src/base/url/img.png"},
            {"work/proj/src/base/index-dev.html", "url/app.css", "../img.png", "work/proj/src/base/img.png"},

            {"work/proj/src/base/index-dev.html", "app.css", "file/img.png", "work/proj/src/base/file/img.png"},
            {"work/proj/src/base/index-dev.html", "app.css", "img.png", "work/proj/src/base/img.png"},
            {"work/proj/src/base/index-dev.html", "app.css", "../img.png", "work/proj/src/img.png"},

            {"work/proj/src/base/index-dev.html", "../app.css", "file/img.png", "work/proj/src/file/img.png"},
            {"work/proj/src/base/index-dev.html", "../app.css", "img.png", "work/proj/src/img.png"},
            {"work/proj/src/base/index-dev.html", "../app.css", "../img.png", "work/proj/img.png"},

            {"work/proj/src/index-dev.html", "url/app.css", "file/img.png", "work/proj/src/url/file/img.png"},
            {"work/proj/src/index-dev.html", "url/app.css", "img.png", "work/proj/src/url/img.png"},
            {"work/proj/src/index-dev.html", "url/app.css", "../img.png", "work/proj/src/img.png"},

            {"work/proj/src/index-dev.html", "app.css", "file/img.png", "work/proj/src/file/img.png"},
            {"work/proj/src/index-dev.html", "app.css", "img.png", "work/proj/src/img.png"},
            {"work/proj/src/index-dev.html", "app.css", "../img.png", "work/proj/img.png"},

            {"work/proj/src/index-dev.html", "../app.css", "file/img.png", "work/proj/file/img.png"},
            {"work/proj/src/index-dev.html", "../app.css", "img.png", "work/proj/img.png"},
            {"work/proj/src/index-dev.html", "../app.css", "../img.png", "work/img.png"},

            {"work/proj/src/base/lib/index-dev.html", "url/app.css", "file/img.png", "work/proj/src/base/lib/url/file/img.png"},
            {"work/proj/src/base/lib/index-dev.html", "url/app.css", "img.png", "work/proj/src/base/lib/url/img.png"},
            {"work/proj/src/base/lib/index-dev.html", "url/app.css", "../img.png", "work/proj/src/base/lib/img.png"},

            {"work/proj/src/base/lib/index-dev.html", "app.css", "file/img.png", "work/proj/src/base/lib/file/img.png"},
            {"work/proj/src/base/lib/index-dev.html", "app.css", "img.png", "work/proj/src/base/lib/img.png"},
            {"work/proj/src/base/lib/index-dev.html", "app.css", "../img.png", "work/proj/src/base/img.png"},

            {"work/proj/src/base/lib/index-dev.html", "../app.css", "file/img.png", "work/proj/src/base/file/img.png"},
            {"work/proj/src/base/lib/index-dev.html", "../app.css", "img.png", "work/proj/src/base/img.png"},
            {"work/proj/src/base/lib/index-dev.html", "../app.css", "../img.png", "work/proj/src/img.png"},
    };

    @Parameterized.Parameters(name = "{index} {0} {1} {2} {3}")
    public static Collection<Object[]> data() {
        return asList(CONFIG);
    }

    String sourceBasePath;
    String sourceCssPath;
    String resourcePath;
    String absoluteResourcePath;

    public ResourcePathExtractorTest(String sourceBasePath, String sourceCssPath, String resourcePath, String absoluteResourcePath) {
        this.sourceBasePath = sourceBasePath;
        this.sourceCssPath = sourceCssPath;
        this.resourcePath = resourcePath;
        this.absoluteResourcePath = absoluteResourcePath;
    }

    private PathNormalizator pathNormalizator = new PathNormalizator();

    @Test
    public void test() throws Exception {
        assertThat(pathNormalizator.getAbsoluteResourcePath(sourceBasePath, sourceCssPath, resourcePath).toString())
                .endsWith(absoluteResourcePath);
    }

}