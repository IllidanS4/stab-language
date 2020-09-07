OUTFILE = stabal.jar
CLASSPATH = "../bin/stabrt.jar"
SFLAGS = -cp:$(CLASSPATH) -out:$(OUTFILE)
SC = java -jar ../bin/stabc.jar

CLASSES = $(wildcard src/*.stab)

default: build

build:
	$(SC) $(SFLAGS) $(CLASSES)

clean:
	$(RM) $(OUTFILE)
