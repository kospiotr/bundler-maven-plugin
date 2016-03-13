String sourceDir = "$basedir/src/main/resources"
String targetDir = "$basedir/target/classes"
String srcContent;
String destContent;

//validate empty
srcContent = new File(sourceDir, "empty.html").text
destContent = new File(targetDir, "empty.html").text
assert srcContent == destContent

//validate remove processor
destContent = new File(targetDir, "remove-processor.html").text
assert !destContent.contains("Content to remove")

//validate js processor
destContent = new File(targetDir, "js-processor.html").text

assert destContent.contains("<script src=\"single.js\"></script>")
assert destContent.contains("<script src=\"multiple.js\"></script>")

String singleJsContent = new File(targetDir, "single.js").text
assert singleJsContent.contains("hello world1")

String multipleJsContent = new File(targetDir, "multiple.js").text
assert multipleJsContent.contains("hello world2")
assert multipleJsContent.contains("hello world3")
assert multipleJsContent.contains("hello world4")

String levelUpJsContent = new File(targetDir, "../level-up.js").text
assert levelUpJsContent.contains("hello world1")

String levelDownJsContent = new File(targetDir, "app/level-down.js").text
assert levelDownJsContent.contains("hello world1")

File[] hashJsFiles = new File(targetDir).listFiles(new FilenameFilter(){
    @Override
    boolean accept(File dir, String name) {
        return name.startsWith("hash-") && name.endsWith(".js")
    }
})
assert hashJsFiles.length == 1

//validate css processor
destContent = new File(targetDir, "css-processor.html").text

assert destContent.contains("<link rel=\"stylesheet\" href=\"single.css\" />")
assert destContent.contains("<link rel=\"stylesheet\" href=\"single-prefix.css\" />")
assert destContent.contains("<link rel=\"stylesheet\" href=\"single-suffix.css\" />")
assert destContent.contains("<link rel=\"stylesheet\" href=\"multiple.css\" />")
assert destContent.contains("<link rel=\"stylesheet\" href=\"../level-up.css\" />")
assert destContent.contains("<link rel=\"stylesheet\" href=\"app/level-down.css\" />")

String singleContent = new File(targetDir, "single.css").text
assert singleContent.contains("body{background:white}")

String singlePrefixContent = new File(targetDir, "single-prefix.css").text
assert singlePrefixContent.contains("body{background:white}")

String singleSuffixContent = new File(targetDir, "single-suffix.css").text
assert singleSuffixContent.contains("body{background:white}")

String multipleContent = new File(targetDir, "multiple.css").text
assert multipleContent.contains("body{background:white}")
assert multipleContent.contains("h1{color:blue}")
assert multipleContent.contains("p{font-family:Arial,serif}")

String levelUpContent = new File(targetDir, "../level-up.css").text
assert levelUpContent.contains("body{background:white}")

String levelDownContent = new File(targetDir, "app/level-down.css").text
assert levelDownContent.contains("body{background:white}")

File[] hashFiles = new File(targetDir).listFiles(new FilenameFilter(){
    @Override
    boolean accept(File dir, String name) {
        return name.startsWith("hash-") && name.endsWith(".css")
    }
})
assert hashFiles.length == 1