ANTLR_VERSION := 4.13.2
ANTLR_JAR := tools/antlr-$(ANTLR_VERSION)-complete.jar

$(ANTLR_JAR):
	mkdir -p tools
	curl -L -o $(ANTLR_JAR) https://www.antlr.org/download/antlr-$(ANTLR_VERSION)-complete.jar

antlr: $(ANTLR_JAR)
	cd src/main/antlr4/madlang && java -jar ../../../../$(ANTLR_JAR) -no-listener -visitor -package madlang -o ../../java/madlang Program.g4

build: antlr
	mkdir -p out
	javac -cp $(ANTLR_JAR):src/main/java -d out src/main/java/madlang/*.java

run: build
	java -cp $(ANTLR_JAR):out madlang.Main $(FILE)

clean:
	rm -rf out
	rm -f src/main/java/madlang/ProgramLexer.java
	rm -f src/main/java/madlang/ProgramParser.java
	rm -f src/main/java/madlang/ProgramBaseVisitor.java
	rm -f src/main/java/madlang/ProgramVisitor.java
	rm -f src/main/java/madlang/*.tokens