OUTFILE = stabrt.jar
CLASSPATH = "../bin/stabal.jar"
SFLAGS = -cp:$(CLASSPATH) -out:$(OUTFILE)
SC = java -jar ../bin/stabc.jar

CLASSES = $(wildcard src/*/*.stab)

default: build

build:
	$(SC) $(SFLAGS) $(CLASSES)

clean:
	$(RM) $(OUTFILE)
