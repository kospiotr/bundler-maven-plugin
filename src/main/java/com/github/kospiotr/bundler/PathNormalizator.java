package com.github.kospiotr.bundler;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class PathNormalizator {

    public String relativize(String baseBefore, String baseAfter, String url, String currentFile) {
        Path baseBeforePath = FileSystems.getDefault().getPath(baseBefore);
//        System.out.println("baseBeforePath = \t\t" + baseBeforePath);

        Path absoluteBeforePath = baseBeforePath.toAbsolutePath();
//        System.out.println("absoluteBeforePath = \t" + absoluteBeforePath);

        Path absoluteBeforeParentPath = absoluteBeforePath.getParent();
//        System.out.println("absoluteBeforePath = \t" + absoluteBeforeParentPath);

        Path urlPath = absoluteBeforeParentPath.resolve(url);
//        System.out.println("urlPath = \t\t\t\t" + urlPath);

        Path parentFullUrlPath = urlPath.getParent();
//        System.out.println("parentFullUrlPath = \t" + parentFullUrlPath);

        Path fullCurrentFilePath = parentFullUrlPath.resolve(currentFile);
//        System.out.println("fullCurrentFilePath = \t" + fullCurrentFilePath);

        Path absoluteAfterPath = FileSystems.getDefault().getPath(baseAfter).toAbsolutePath().getParent().normalize();
//        System.out.println("absoluteAfterPath = \t" + absoluteAfterPath);

        Path relativize = absoluteAfterPath.relativize(fullCurrentFilePath);
//        System.out.println("relativize = \t\t\t" + relativize);

        Path normalized = relativize.normalize();
//        System.out.println("normalized = \t\t\t" + normalized);
        return normalized.toString();
    }

}
