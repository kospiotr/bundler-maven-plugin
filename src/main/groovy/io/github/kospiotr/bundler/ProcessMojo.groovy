package io.github.kospiotr.bundler

import groovy.util.logging.Log
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter

/**
 * Goal which touches a timestamp file.
 */
@Log
@Mojo(name = "process", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class ProcessMojo extends AbstractMojo {

    /**
     * Input file.
     */
    @Parameter(property = "inputFile", required = true)
    File inputFilePah;

    /**
     * Location of the output file.
     */
    @Parameter(property = "outputFilePath", required = true)
    File outputFilePath;


    /**
     * Hashing Algrithm. Possible values for shipped providers:
     * MD5,
     * SHA-1,
     * SHA-256
     */
    @Parameter(defaultValue = "MD5", property = "hashingAlgorithm", required = true)
    String hashingAlgorithm;

    @Parameter(defaultValue = "true", property = "munge", required = true)
    boolean munge;

    @Parameter(defaultValue = "false", property = "verbose", required = true)
    boolean verbose;

    @Parameter(defaultValue = "true", property = "preserveAllSemiColons", required = true)
    boolean preserveAllSemiColons;

    @Parameter(defaultValue = "true", property = "disableOptimizations", required = true)
    boolean disableOptimizations;

    public ProcessMojo() {
    }

    ProcessMojo(File inputFilePah, File outputFilePath) {
        this.inputFilePah = inputFilePah
        this.outputFilePath = outputFilePath
        this.hashingAlgorithm = "MD5"
    }

    public void execute() {
        Tokenizer tokenizer = new Tokenizer(this);
        tokenizer.registerProcessor(new RemoveTagProcessor());
        tokenizer.registerProcessor(new JsTagProcessor());
        tokenizer.registerProcessor(new CssTagProcessor());

        FileProcessor fileProcessor = new FileProcessor(tokenizer);
        fileProcessor.process(inputFilePah.toPath(), outputFilePath.toPath());
    }
}