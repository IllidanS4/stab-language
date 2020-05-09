package gigaherz.gradle.stab;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.gradle.api.*;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.internal.JvmPluginsHelper;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

import java.io.File;
import java.util.Objects;

public class StabPlugin implements Plugin<ProjectInternal>
{
    @Override
    public void apply(ProjectInternal project) {

        project.getPluginManager().apply(JavaPlugin.class);

        StabExtension ext = project.getExtensions().create(
                "stab",
                StabExtension.class,
                project,
                project.getFileResolver()
        );

        SourceSetContainer cont = (SourceSetContainer) project.getProperties().get("sourceSets");

        cont.all((SourceSet ss) -> {
            String name = ss.getName();
            File sources = project.file("src/" + name + "/stab");
            StabSourceSet fss = ext.getSourceSetsContainer().maybeCreate(name);
            SourceDirectorySet sds = fss.getStab();
            sds.srcDir(sources);
            Convention sourceSetConvention = (Convention) InvokerHelper.getProperty(ss, "convention");
            sourceSetConvention.getPlugins().put("stab", fss);

            configureStabCompile(project, ss);
        });
    }

    private static void configureStabCompile(final Project project, final SourceSet sourceSet) {
        Convention StabConvention = (Convention) InvokerHelper.getProperty(sourceSet, "convention");
        final StabSourceSet stabSourceSet = Objects.requireNonNull(StabConvention.findPlugin(StabSourceSet.class));

        final TaskProvider<StabCompile> stabCompileTask = project.getTasks().register(sourceSet.getCompileTaskName("stab"), StabCompile.class, stabCompile -> {
            JvmPluginsHelper.configureForSourceSet(sourceSet, stabSourceSet.getStab(), stabCompile, stabCompile.getOptions(), project);
            stabCompile.setDescription("Compiles the " + stabSourceSet.getStab() + ".");
            stabCompile.setSource(stabSourceSet.getStab());
        });
        JvmPluginsHelper.configureOutputDirectoryForSourceSet(sourceSet, stabSourceSet.getStab(), project, stabCompileTask, stabCompileTask.map(gigaherz.gradle.stab.StabCompile::getOptions));

        project.getTasks().named(sourceSet.getClassesTaskName(), task -> task.dependsOn(stabCompileTask));
    }

    /*private static void configureCompileDefaults(final Project project, final StabRuntime stabRuntime) {
        project.getTasks()
                .withType(StabCompile.class)
                .configureEach(compile ->
                        compile.getConventionMapping()
                                .map("stabClasspath", (Callable<FileCollection>) () -> stabRuntime.inferStabClasspath(compile.getClasspath())));
    } */
}

