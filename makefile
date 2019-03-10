JAVA_COMPILE = javac
WARN_ALL = -Xlint               # Enable all warnings
WARNING_TERMINATION = -Werror   # Terminate on compilation error
DEBUG_FLAG = -g
SOURCE_VERSION = -source 1.8    # Java version 1.8 or higher
ENCODING = -encoding UTF-8      # Encode all the files as UTF-8
TARGET_FOLDER = -d build/

JFLAGS = $(WARN_ALL) $(WARNING_TERMINATION) $(DEBUG_FLAG) \
				 $(SOURCE_VERSION) $(ENCODING) $(TARGET_FOLDER)

FIRST = $(wildcard first/basics/*.java)
SECOND = $(wildcard second/blockcipher/*.java)

.SUFFIXES: .java .class

.java.class:
	$(JAVA_COMPILE) $(JFLAGS) $*.java

default: second

first: first_package
first_package: $(FIRST:.java=.class)

second: first second_package
second_package: $(SECOND:.java=.class)

clean:
	rm -rf build/*

