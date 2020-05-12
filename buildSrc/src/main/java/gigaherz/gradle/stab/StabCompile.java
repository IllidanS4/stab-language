package gigaherz.gradle.stab;

import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.tasks.compile.CompilationFailedException;
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

            int errorCount = 0;
            int warningCount = 0;

            for (CodeError error : results.getErrors())
            {
                if (!this.getOptions().isWarnings() && error.getLevel() > 0)
                    continue;

                StringBuilder sb = new StringBuilder();

                String filename = error.getFilename();
                if (filename != null)
                {
                    sb.append((new File(error.getFilename())).getAbsolutePath());
                }
                else
                {
                    sb.append("Unknown source");
                }

                if (error.getLine() > 0)
                {
                    sb.append(":");
                    sb.append(error.getLine());
                    /*if (error.getColumn() > 0)
                    {
                        sb.append(":");
                        sb.append(error.getColumn());
                    }*/

                    sb.append(":");
                }

                if (error.getLevel() == 0)
                {
                    sb.append(" error: ");
                }
                else
                {
                    sb.append(" warning: ");
                }

                //sb.append(error.getId());
                //sb.append(": ");
                sb.append(error.getMessage());

                if (error.getLevel() == 0)
                {
                    errorCount++;
                    getProject().getLogger().error(sb.toString());
                }
                else
                {
                    warningCount++;
                    getProject().getLogger().warn(sb.toString());
                }

            }

            if (errorCount > 0 && warningCount > 0)
            {
                if (errorCount > 1 && warningCount > 1)
                    getProject().getLogger().error("{} errors, {} warnings", errorCount, warningCount);
                else if (errorCount > 1)
                    getProject().getLogger().error("{} errors, {} warning", errorCount, warningCount);
                else if(warningCount > 1)
                    getProject().getLogger().error("{} error, {} warnings", errorCount, warningCount);
                else
                    getProject().getLogger().error("{} error, {} warning", errorCount, warningCount);
            }
            else if(errorCount > 0)
            {
                if (errorCount > 1)
                    getProject().getLogger().error("{} errors", errorCount);
                else
                    getProject().getLogger().error("{} error", errorCount);
            }
            else if(warningCount > 0)
            {
                if (warningCount > 1)
                    getProject().getLogger().warn("{} warnings", warningCount);
                else
                    getProject().getLogger().warn("{} warning", warningCount);
            }

            if (errorCount == 0)
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
            else
            {
                throw new CompilationFailedException();
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
