decrypt: phony
	javac *.java

clean: phony
	find . -name "*.class" -delete

.PHONY: phony
