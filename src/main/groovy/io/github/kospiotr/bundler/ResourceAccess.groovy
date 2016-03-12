package io.github.kospiotr.bundler

import groovy.util.logging.Log

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

@Log
class ResourceAccess {

    public String read(Path path) {
        log.info("Reading from file: " + path.toAbsolutePath())
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public void write(Path path, String s) {
        log.info("Writing to file: "+path.toAbsolutePath())
        Files.createDirectories(path.getParent());
        Files.write(path, s.bytes)
    }
}
