package io.github.kospiotr.bundler;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Goal which touches a timestamp file.
 */
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
        this.inputFilePah = inputFilePah;
        this.outputFilePath = outputFilePath;
        this.hashingAlgorithm = "MD5";
    }

    public void execute() {
        Tokenizer tokenizer = new Tokenizer(this);
        tokenizer.registerProcessor(new RemoveTagProcessor());
        tokenizer.registerProcessor(new JsTagProcessor());
        tokenizer.registerProcessor(new CssTagProcessor());

        FileProcessor fileProcessor = new FileProcessor(tokenizer);
        fileProcessor.process(inputFilePah.toPath(), outputFilePath.toPath());
    }

    public File getInputFilePah() {
        return inputFilePah;
    }

    public File getOutputFilePath() {
        return outputFilePath;
    }

    public String getHashingAlgorithm() {
        return hashingAlgorithm;
    }

    public boolean isMunge() {
        return munge;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public boolean isPreserveAllSemiColons() {
        return preserveAllSemiColons;
    }

    public boolean isDisableOptimizations() {
        return disableOptimizations;
    }
}