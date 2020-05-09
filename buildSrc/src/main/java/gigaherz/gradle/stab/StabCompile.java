package gigaherz.gradle.stab;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.AbstractCompile;
import org.gradle.api.tasks.compile.CompileOptions;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@CacheableTask
public class StabCompile extends AbstractCompile
{
    private final CompileOptions compileOptions;

    @Inject
    public StabCompile(ObjectFactory objectFactory) {
        this.compileOptions = objectFactory.newInstance(CompileOptions.class);
    }

    @TaskAction
    public void compile() {

        SourceSetContainer cont = (SourceSetContainer) getProject().getProperties().get("sourceSets");
        cont.all((SourceSet ss) -> {
            StabSourceSet sss = getProject()
                    .getExtensions()
                    .getByType(StabExtension.class)
                    .getSourceSetsContainer()
                    .maybeCreate(ss.getName());
            runCompileApp(sss);
        });
    }

    private int runCompileApp(StabSourceSet sss) {

        List<String> args = new ArrayList<>();
        args.add("-al:bin/stabal.jar");
        args.add("-cp:bin/stabrt.jar;bin/asm-8.0.1.jar;bin/stabc.jar");
        args.add("-manifest:tools/build/MANIFEST.MF");
        args.add("-out:" + sss.getStab().getOutputDir());

        for(File f : sss.getStab().getSrcDirs())
        {
            addSourceFiles(args, f);
        }

        return new stab.tools.compiler.Application().run(args.toArray(new String[0]));
    }

    private void addSourceFiles(List<String> args, File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addSourceFiles(args, file);
                } else if (stab.tools.helpers.PathHelper.getExtension(file.getName()).equals(".stab")) {
                    args.add(file.getAbsolutePath());
                }
            }
        }
    }

    @Nonnull
    public CompileOptions getOptions()
    {
        return compileOptions;
    }
}
