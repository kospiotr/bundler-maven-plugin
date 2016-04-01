package com.github.kospiotr.bundler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TargetCssPathExtractorTest {

    private static final Object[][] CONFIG = new Object[][]{
            {"work/proj/src/base/index-prod.html","url/app.css","work/proj/src/base/url/app.css"},
            {"work/proj/src/base/index-prod.html","app.css","work/proj/src/base/app.css"},
            {"work/proj/src/base/index-prod.html","../app.css","work/proj/src/app.css"},

            {"work/proj/src/index-prod.html","url/app.css","work/proj/src/url/app.css"},
            {"work/proj/src/index-prod.html","app.css","work/proj/src/app.css"},
            {"work/proj/src/index-prod.html","../app.css","work/proj/app.css"},

            {"work/proj/src/base/lib/index-prod.html","url/app.css","work/proj/src/base/lib/url/app.css"},
            {"work/proj/src/base/lib/index-prod.html","app.css","work/proj/src/base/lib/app.css"},
            {"work/proj/src/base/lib/index-prod.html","../app.css","work/proj/src/base/app.css"},
    };

    @Parameterized.Parameters(name = "{index} {0} {1} {2}")
    public static Collection<Object[]> data() {
        return asList(CONFIG);
    }

    String targetBasePath; String targetCss; String absoluteTargetCssPath;

    public TargetCssPathExtractorTest(String targetBasePath, String targetCss, String absoluteTargetCssPath) {
        this.targetBasePath = targetBasePath;
        this.targetCss = targetCss;
        this.absoluteTargetCssPath = absoluteTargetCssPath;
    }

    private PathNormalizator pathNormalizator = new PathNormalizator();

    @Test
    public void test() throws Exception {
        assertThat(pathNormalizator.getAbsoluteTargetCssPath(targetBasePath, targetCss).toString())
                .endsWith(absoluteTargetCssPath);
    }

}