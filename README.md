# bundler-maven-plugin

Maven plugin for creating bundle package of js and css files in Maven project.

Inspired by: https://github.com/dciccale/grunt-processhtml

# Usage

Configure plugin:

```xml
      <plugin>
        <groupId>io.github.kospiotr</groupId>
        <artifactId>bundler-maven-plugin</artifactId>
        <version>1.0</version>
        <executions>
          <execution>
            <id>js</id>
            <goals>
              <goal>process</goal>
            </goals>
            <configuration>
              <inputFilePah>${project.basedir}/src/main/resources/index-dev.html</inputFilePah>
              <outputFilePath>${project.build.outputDirectory}/index.html</outputFilePath>
            </configuration>
          </execution>
        </executions>
      </plugin>
```

Create source file ```${project.basedir}/src/main/resources/index-dev.html```

```html
<!DOCTYPE html>
<html lang="en">
<body>

<!-- bundle:js app-#hash#.min.js-->
<script src="js/lib.js"></script>
<script src="js/app.js"></script>
<!-- /bundle -->

<!-- bundle:css app-#hash#.min.css-->
<link href="css/lib.css"/>
<link href="css/app.css"/>
<!-- /bundle -->

</body>
</html>
```

After running plugin the result will be outputted to ```${project.build.outputDirectory}/index.html```


```html
<!DOCTYPE html>
<html lang="en">
<body>

<script src="app-0874ac8910c7b3d2e73da106ebca7329.min.js"></script>
<link rel="stylesheet" href="app-4971211a240c63874c6ae8c82bd0c88c.min.css" />

</body>
</html>
```