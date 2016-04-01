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
     * @param absoluteResourcePath - resource absolute path
     * @param absoluteTargetCssPath - target css absolute path
     * @return - relative path from target css file to source resource
     */
    public String relativize(Path absoluteResourcePath, Path absoluteTargetCssPath) {
        Path absoluteTargetCssParentPath = absoluteTargetCssPath.getParent();
        return absoluteTargetCssParentPath.relativize(absoluteResourcePath).toString();
    }

    public Path getAbsoluteResourcePath(String sourceBasePath, String sourceCssPath, String resourcePath){
        Path absoluteBasePath = FileSystems.getDefault().getPath(sourceBasePath).toAbsolutePath();
        Path absoluteSourceCssPath = absoluteBasePath.getParent().resolve(sourceCssPath);
        Path absoluteParentSourceCssPath = absoluteSourceCssPath.getParent();
        Path absoluteResourcePath = absoluteParentSourceCssPath.resolve(resourcePath);
        return absoluteResourcePath.normalize();
    }

    public Path getAbsoluteTargetCssPath(String targetBasePath, String targetCss){
        Path absoluteTargetBasePath = FileSystems.getDefault().getPath(targetBasePath).toAbsolutePath();
        Path absoluteAfterPath = absoluteTargetBasePath.getParent();
        Path absoluteTargetCssPath = absoluteAfterPath.resolve(targetCss);
        return absoluteTargetCssPath.normalize();

    }

}
