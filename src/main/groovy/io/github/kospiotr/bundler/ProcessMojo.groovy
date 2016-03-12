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

    public ProcessMojo() {
    }

    ProcessMojo(File inputFilePah, File outputFilePath) {
        this.inputFilePah = inputFilePah
        this.outputFilePath = outputFilePath
    }

    public void execute() {
        Tokenizer tokenizer = new Tokenizer(this);
        tokenizer.registerProcessor(new RemoveTagProcessor());
        tokenizer.registerProcessor(new JsTagProcessor());

        FileProcessor fileProcessor = new FileProcessor(tokenizer);
        fileProcessor.process(inputFilePah.toPath(), outputFilePath.toPath());
    }
}
