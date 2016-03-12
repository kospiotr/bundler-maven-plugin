package io.github.kospiotr.bundler

import groovy.util.logging.Log

import java.nio.file.Path

@Log
class FileProcessor {

    Tokenizer tokenizer
    ResourceAccess resourceAccess = new ResourceAccess()


    public FileProcessor(Tokenizer tokenizer) {
        this.tokenizer = tokenizer
    }

    def void process(Path inputFilePath, Path outputFilePath) {
        String inputFileContent = resourceAccess.read(inputFilePath);
        String outputFileContent = tokenizer.process(inputFileContent)
        resourceAccess.write(outputFilePath, outputFileContent);
    }
}
