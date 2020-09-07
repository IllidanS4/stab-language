GITHUBURL = https://github.com/IllidanS4/stab-language
VERSION = v1.0-beta5

default: prepare compiler/stabc.jar runtime/stabrt.jar annotated/stabal.jar

bin:
	mkdir bin

bin/asm-8.0.1.jar:
	wget -O bin/asm-8.0.1.jar https://repo1.maven.org/maven2/org/ow2/asm/asm/8.0.1/asm-8.0.1.jar

bin/stab%:
	wget -O $@ $(GITHUBURL)/releases/download/$(VERSION)/$(notdir $@)

prepare: bin bin/asm-8.0.1.jar bin/stabal.jar bin/stabc.jar bin/stabrt.jar

compiler/stabc.jar:
	$(MAKE) -C compiler

runtime/stabrt.jar:
	$(MAKE) -C runtime

annotated/stabal.jar:
	$(MAKE) -C annotated

clean:
	$(RM) compiler/stabc.jar runtime/stabrt.jar annotated/stabal.jar
