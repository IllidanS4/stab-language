OUTFILE = stab.tools.eclipse.jar
PLUGINS = eclipse/plugins/org.eclipse.core.commands_3.6.0.I20100512-1500.jar eclipse/plugins/org.eclipse.core.contenttype_3.4.100.v20100505-1235.jar eclipse/plugins/org.eclipse.core.expressions_3.4.200.v20100505.jar eclipse/plugins/org.eclipse.core.filebuffers_3.5.100.v20100520-0800.jar eclipse/plugins/org.eclipse.core.filesystem_1.3.1.R36x_v20100727-0745.jar eclipse/plugins/org.eclipse.core.jobs_3.5.1.R36x_v20100824.jar eclipse/plugins/org.eclipse.core.resources_3.6.0.R36x_v20100825-0600.jar eclipse/plugins/org.eclipse.core.runtime_3.6.0.v20100505.jar eclipse/plugins/org.eclipse.debug.ui_3.6.1.v20100901_r361.jar eclipse/plugins/org.eclipse.equinox.common_3.6.0.v20100503.jar eclipse/plugins/org.eclipse.equinox.preferences_3.3.0.v20100503.jar eclipse/plugins/org.eclipse.equinox.registry_3.5.0.v20100503.jar eclipse/plugins/org.eclipse.jface.text_3.6.1.r361_v20100825-0800.jar eclipse/plugins/org.eclipse.jface_3.6.1.M20100825-0800.jar eclipse/plugins/org.eclipse.osgi_3.6.1.R36x_v20100806.jar eclipse/plugins/org.eclipse.swt.win32.win32.x86_3.6.1.v3655c.jar eclipse/plugins/org.eclipse.text_3.5.0.v20100601-1300.jar eclipse/plugins/org.eclipse.ui.console_3.5.0.v20100526.jar eclipse/plugins/org.eclipse.ui.editors_3.6.1.r361_v20100825-0800.jar eclipse/plugins/org.eclipse.ui.ide_3.6.1.M20100825-0800.jar eclipse/plugins/org.eclipse.ui.workbench.texteditor_3.6.1.r361_v20100714-0800.jar eclipse/plugins/org.eclipse.ui.workbench_3.6.1.M20100826-1330.jar
CLASSPATH = "$(subst $(eval) ,;,$(wildcard ../bin/*.jar));$(subst $(eval) ,;,$(PLUGINS))"
RESOURCES = resources
SFLAGS = -cp:$(CLASSPATH) -res:$(RESOURCES) -out:$(OUTFILE) -mf:MANIFEST.MF
SC = java -jar ../bin/stabc.jar
REPO = http://ftp.gnome.org/mirror/eclipse.org/eclipse/updates/3.6/R-3.6.1-201009090800/plugins

CLASSES = $(wildcard src/*.stab) $(wildcard src/*/*.stab)

default: plugins classes

plugins: eclipse/plugins $(PLUGINS)

eclipse:
	mkdir $@

eclipse/plugins: eclipse
	mkdir $@

eclipse/plugins/%:
	wget -O $@ $(REPO)/$(notdir $@)

resources/libs:
	mkdir $@

resources/libs/%:
	cp ../bin/$(notdir $@) $@

classes: resources/libs resources/libs/asm-8.0.1.jar resources/libs/stabal.jar resources/libs/stabc.jar resources/libs/stabrt.jar
	$(SC) $(SFLAGS) $(CLASSES)

clean:
	$(RM) $(OUTFILE)
