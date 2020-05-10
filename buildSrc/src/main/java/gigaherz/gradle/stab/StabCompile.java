package gigaherz.gradle.stab;

import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.AbstractCompile;
import org.gradle.api.tasks.compile.CompileOptions;
import stab.reflection.TypeLoadException;
import stab.tools.compiler.Compiler;
import stab.tools.compiler.CompilerParameters;
import stab.tools.compiler.CompilerResults;
import stab.tools.helpers.CodeError;
import stab.tools.helpers.PathHelper;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@CacheableTask
public class StabCompile extends AbstractCompile
{

    private FileCollection annotationsClasspath;
    private final CompileOptions compileOptions;
    private final StabCompileOptions stabCompileOptions;

    @Inject
    public StabCompile(ObjectFactory objectFactory) {
        this.compileOptions = objectFactory.newInstance(CompileOptions.class);
        this.stabCompileOptions = new StabCompileOptions();
    }

    @Nested
    @Nonnull
    public CompileOptions getOptions()
    {
        return compileOptions;
    }

    @Nested
    @Nonnull
    public StabCompileOptions getStabOptions()
    {
        return stabCompileOptions;
    }

    @Classpath
    public FileCollection getAnnotationsClasspath() {
        return this.annotationsClasspath;
    }

    public void setAnnotationsClasspath(FileCollection collection)
    {
        this.annotationsClasspath = collection;
    }

    @TaskAction
    public void compile() {
        stab.tools.compiler.CompilerParameters parameters = new CompilerParameters();

        List<String> annotationLibraries = parameters.getAnnotatedLibraryPaths();
        if (getStabOptions().getIncludeDefaultAnnotations())
            annotationLibraries.add(locateDefaultAnnotations().getAbsolutePath());
        getAnnotationsClasspath().getFiles().stream().filter(File::exists).map(File::getAbsolutePath).forEach(annotationLibraries::add);

        // Calculate compile classpath entries
        List<String> cp = parameters.getClassPath();
        if (getStabOptions().getIncludeRuntime())
            cp.add(locateRuntime().getAbsolutePath());
        getClasspath().getFiles().stream().filter(File::exists).map(File::getAbsolutePath).forEach(cp::add);


        try {
            CompilerResults results = (new Compiler()).compileFromFiles(parameters, getSource().getFiles().toArray(new File[0]));

            boolean hasErrors = false;

            for (CodeError error : results.getErrors())
            {
                String filename = error.getFilename();
                if (filename != null)
                {
                    System.out.print((new File(error.getFilename())).getAbsolutePath());
                }
                else
                {
                    System.out.print("Unknown source");
                }

                if (error.getLine() > 0)
                {
                    System.out.print(":");
                    System.out.print(error.getLine());
                    /*if (error.getColumn() > 0)
                    {
                        System.out.print(",");
                        System.out.print(error.getColumn());
                    }*/

                    System.out.print(":");
                }

                if (error.getLevel() == 0)
                {
                    hasErrors = true;
                    System.out.print(" error: ");
                }
                else
                {
                    System.out.print(" warning: ");
                }

                System.out.print(error.getId());
                System.out.print(": ");
                System.out.println(error.getMessage());
            }

            if (!hasErrors)
            {
                File outputPath = getDestinationDir();

                outputPath.mkdirs();

                for (Map.Entry<String, byte[]> e : results.getClassFiles().entrySet())
                {
                    File classFile = new File(outputPath, String.format("%s.class", e.getKey().replace('.', '/')));
                    File dir = classFile.getParentFile();
                    if (!dir.exists())
                    {
                        dir.mkdirs();
                    }

                    try (FileOutputStream s = new FileOutputStream(classFile))
                    {
                        s.write(e.getValue());
                    }
                }
            }
        }
        catch (TypeLoadException e)
        {
            throw new RuntimeException(String.format("Cannot find type %s. The class is missing from the classpath.", e.getTypeName()), e);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error writing class file", e);
        }
    }

    private File locateDefaultAnnotations()
    {
        // FIXME
        return getProject().getRootProject().file("bin/stabal.jar");
    }

    private File locateRuntime()
    {
        // FIXME
        return getProject().getRootProject().file("bin/stabrt.jar");
    }
}
