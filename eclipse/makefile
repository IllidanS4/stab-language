OUTFILE = stab.tools.eclipse.jar
CLASSPATH = "../bin/asm-8.0.1.jar;../bin/stabrt.jar;../bin/stabal.jar;../bin/stabc.jar;$(subst $(eval) ,;,$(wildcard eclipse/plugins/*.jar))"
RESOURCES = resources
SFLAGS = -cp:$(CLASSPATH) -res:$(RESOURCES) -out:$(OUTFILE) -mf:MANIFEST.MF
SC = java -jar ../bin/stabc.jar

CLASSES = $(wildcard src/*.stab) $(wildcard src/*/*.stab)

default: classes

classes:
	$(SC) $(SFLAGS) $(CLASSES)

clean:
	$(RM) *.class
