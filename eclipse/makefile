OUTFILE = stab.tools.eclipse.jar
CLASSPATH = "$(subst $(eval) ,;,$(wildcard ../bin/*.jar));$(subst $(eval) ,;,$(wildcard eclipse/plugins/org.*.jar))"
RESOURCES = resources
SFLAGS = -cp:$(CLASSPATH) -res:$(RESOURCES) -out:$(OUTFILE) -mf:MANIFEST.MF
SC = java -jar ../bin/stabc.jar

CLASSES = $(wildcard src/*.stab) $(wildcard src/*/*.stab)

default: classes

classes:
	$(SC) $(SFLAGS) $(CLASSES)

clean:
	$(RM) $(OUTFILE)
