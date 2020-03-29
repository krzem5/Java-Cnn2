echo off
echo NUL>_.class&&del /s /f /q *.class
cls
javac -encoding utf8 com/krzem/cnn2/Main.java&&java -Dfile.encoding=UTF8 com/krzem/cnn2/Main
start /min cmd /c "echo NUL>_.class&&del /s /f /q *.class"