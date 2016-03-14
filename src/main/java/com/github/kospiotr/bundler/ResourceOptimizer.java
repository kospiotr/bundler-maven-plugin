package com.github.kospiotr.bundler;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.mozilla.javascript.tools.ToolErrorReporter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class ResourceOptimizer {


    public String optimizeJs(String content, boolean munge, boolean verbose, boolean preserveAllSemiColons, boolean disableOptimizations) {
        try {
            StringWriter out = new StringWriter();
            ToolErrorReporter toolErrorReporter = new ToolErrorReporter(true);
            JavaScriptCompressor compressor = new JavaScriptCompressor(new StringReader(content), toolErrorReporter);
            compressor.compress(out, -1, munge, verbose, preserveAllSemiColons, disableOptimizations);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String optimizeCss(String content) {
        try {
            StringWriter out = new StringWriter();
            CssCompressor compressor = new CssCompressor(new StringReader(content));
            compressor.compress(out, -1);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
