decrypt: phony
	javac -target 1.8 Decrypt.java Password.java Guess.java DecryptJson.java DecryptString.java

.PHONY: phony
