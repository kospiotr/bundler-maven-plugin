package com.github.kospiotr.bundler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class ResourceAccess {

    public String read(Path path) {
        try {
            byte[] encoded = Files.readAllBytes(path);
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(Path path, String s) {
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, s.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
