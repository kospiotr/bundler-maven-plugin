package com.github.kospiotr.bundler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PathNormalizatorTest {

    private static final Object[][] CONFIG = new Object[][]{
            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "file/img.png", "url/file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "img.png", "url/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "../img.png", "img.png"},

            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "file/img.png", "file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "img.png", "img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "../img.png", "../img.png"},

            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "file/img.png", "../file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "img.png", "../img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "../img.png", "../../img.png"},

            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "file/img.png", "base/url/file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "img.png", "base/url/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "../img.png", "base/img.png"},

            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "app.css", "file/img.png", "base/file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "app.css", "img.png", "base/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "app.css", "../img.png", "img.png"},

            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "file/img.png", "file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "img.png", "img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "../img.png", "../img.png"},

            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "url/app.css", "file/img.png", "src/base/url/file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "url/app.css", "img.png", "src/base/url/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "url/app.css", "../img.png", "src/base/img.png"},

            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "app.css", "file/img.png", "src/base/file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "app.css", "img.png", "src/base/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "app.css", "../img.png", "src/img.png"},

            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "../app.css", "file/img.png", "src/file/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "../app.css", "img.png", "src/img.png"},
            {"work/proj/src/base/index-dev.html", "work/proj/index-prod.html", "../app.css", "../img.png", "img.png"},

            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "file/img.png", "../url/file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "img.png", "../url/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "../img.png", "../img.png"},

            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "file/img.png", "../file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "img.png", "../img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "../img.png", "../../img.png"},

            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "file/img.png", "../../file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "img.png", "../../img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "../img.png", "../../../img.png"},

            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "file/img.png", "url/file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "img.png", "url/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "../img.png", "img.png"},

            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "app.css", "file/img.png", "file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "app.css", "img.png", "img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "app.css", "../img.png", "../img.png"},

            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "file/img.png", "../file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "img.png", "../img.png"},
            {"work/proj/src/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "../img.png", "../../img.png"},

            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "url/app.css", "file/img.png", "src/url/file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "url/app.css", "img.png", "src/url/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "url/app.css", "../img.png", "src/img.png"},

            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "app.css", "file/img.png", "src/file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "app.css", "img.png", "src/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "app.css", "../img.png", "img.png"},

            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "../app.css", "file/img.png", "file/img.png"},
            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "../app.css", "img.png", "img.png"},
            {"work/proj/src/index-dev.html", "work/proj/index-prod.html", "../app.css", "../img.png", "../img.png"},

            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "file/img.png", "../../url/file/img.png"},
            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "img.png", "../../url/img.png"},
            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "url/app.css", "../img.png", "../../img.png"},

            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "file/img.png", "../../file/img.png"},
            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "img.png", "../../img.png"},
            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "app.css", "../img.png", "../../../img.png"},

            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "file/img.png", "../../../file/img.png"},
            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "img.png", "../../../img.png"},
            {"work/proj/index-dev.html", "work/proj/src/base/index-prod.html", "../app.css", "../img.png", "../../../../img.png"},

            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "file/img.png", "../url/file/img.png"},
            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "img.png", "../url/img.png"},
            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "url/app.css", "../img.png", "../img.png"},

            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "app.css", "file/img.png", "../file/img.png"},
            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "app.css", "img.png", "../img.png"},
            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "app.css", "../img.png", "../../img.png"},

            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "file/img.png", "../../file/img.png"},
            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "img.png", "../../img.png"},
            {"work/proj/index-dev.html", "work/proj/src/index-prod.html", "../app.css", "../img.png", "../../../img.png"},

            {"work/proj/index-dev.html", "work/proj/index-prod.html", "url/app.css", "file/img.png", "url/file/img.png"},
            {"work/proj/index-dev.html", "work/proj/index-prod.html", "url/app.css", "img.png", "url/img.png"},
            {"work/proj/index-dev.html", "work/proj/index-prod.html", "url/app.css", "../img.png", "img.png"},

            {"work/proj/index-dev.html", "work/proj/index-prod.html", "app.css", "file/img.png", "file/img.png"},
            {"work/proj/index-dev.html", "work/proj/index-prod.html", "app.css", "img.png", "img.png"},
            {"work/proj/index-dev.html", "work/proj/index-prod.html", "app.css", "../img.png", "../img.png"},

            {"work/proj/index-dev.html", "work/proj/index-prod.html", "../app.css", "file/img.png", "../file/img.png"},
            {"work/proj/index-dev.html", "work/proj/index-prod.html", "../app.css", "img.png", "../img.png"},
            {"work/proj/index-dev.html", "work/proj/index-prod.html", "../app.css", "../img.png", "../../img.png"},
    };

    @Parameterized.Parameters(name = "{index} {0} {1} {2} {3}")
    public static Collection<Object[]> data() {
        return asList(CONFIG);
    }

    String baseBefore;
    String baseAfter;
    String url;
    String currentFile;
    String result;

    public PathNormalizatorTest(String baseBefore, String baseAfter, String url, String currentFile, String result) {
        this.baseBefore = baseBefore;
        this.baseAfter = baseAfter;
        this.url = url;
        this.currentFile = currentFile;
        this.result = result;
    }

    private PathNormalizator pathNormalizator = new PathNormalizator();

    @Test
    public void test() throws Exception {
        assertThat(pathNormalizator.normalize(baseBefore, baseAfter, url, currentFile)).isEqualTo(result);
    }

}