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

String singleContent = new File(targetDir, "single.js").text
assert singleContent.contains("hello world1")

String multipleContent = new File(targetDir, "multiple.js").text
assert multipleContent.contains("hello world2")
assert multipleContent.contains("hello world3")
assert multipleContent.contains("hello world4")

String levelUpContent = new File(targetDir, "../level-up.js").text
assert levelUpContent.contains("hello world1")

String levelDownContent = new File(targetDir, "app/level-down.js").text
assert levelDownContent.contains("hello world1")


