package com.github.kospiotr.bundler;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class PathNormalizator {
    /**
     * Transforming project/src/resources/static/index-dev.html<br/>
     * with styles in project/src/resources/static/styles/app.css (styles/app.css in index-dev.html)<br/>
     * that uses resource from project/src/resources/static/lib/image.png (../lib/image.png in app.css)<br/>
     * <br/>
     * to: project/src/resources/static/index-prod.html<br/>
     * where style bundle process to project/src/resources/static/resources/app.min.css (resources/app.min.css in index-prod.html)<br/>
     * so resources must still point to project/src/resources/static/lib/image.png (../lib/image.png)<br/>
     *
     * @param sourceBase
     * @param resource
     * @return
     */
    public String relativize(String sourceBase, String sourceCss, String resource, String targetBase, String targetCss) {
        Path baseBeforePath = FileSystems.getDefault().getPath(sourceBase);
//        System.out.println("baseBeforePath = \t\t" + baseBeforePath);

        Path absoluteBeforePath = baseBeforePath.toAbsolutePath();
//        System.out.println("absoluteBeforePath = \t" + absoluteBeforePath);

        Path absoluteBeforeParentPath = absoluteBeforePath.getParent();
//        System.out.println("absoluteBeforePath = \t" + absoluteBeforeParentPath);

        Path urlPath = absoluteBeforeParentPath.resolve(sourceCss);
//        System.out.println("urlPath = \t\t\t\t" + urlPath);

        Path parentFullUrlPath = urlPath.getParent();
//        System.out.println("parentFullUrlPath = \t" + parentFullUrlPath);

        Path fullCurrentFilePath = parentFullUrlPath.resolve(resource);
//        System.out.println("fullCurrentFilePath = \t" + fullCurrentFilePath);

        Path absoluteAfterPath = FileSystems.getDefault().getPath(targetBase).toAbsolutePath().getParent().normalize();
//        System.out.println("absoluteAfterPath = \t" + absoluteAfterPath);

        Path targetCssDirPath = absoluteAfterPath.resolve(targetCss).normalize().getParent();

        Path relativize = targetCssDirPath.relativize(fullCurrentFilePath);
//        System.out.println("relativize = \t\t\t" + relativize);

        Path normalized = relativize.normalize();
//        System.out.println("normalized = \t\t\t" + normalized);
        return normalized.toString();
    }

}
