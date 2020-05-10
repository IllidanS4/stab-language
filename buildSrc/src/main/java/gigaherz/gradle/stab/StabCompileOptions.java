package gigaherz.gradle.stab;

import org.gradle.api.tasks.compile.AbstractOptions;

public class StabCompileOptions extends AbstractOptions
{
    private boolean includeRuntime = true;
    private boolean includeDefaultAnnotations = true;

    public boolean getIncludeRuntime()
    {
        return includeRuntime;
    }

    public void includeRuntime(boolean includeRuntime)
    {
        this.includeRuntime = includeRuntime;
    }

    public boolean getIncludeDefaultAnnotations()
    {
        return includeDefaultAnnotations;
    }

    public void includeDefaultAnnotations(boolean includeDefaultAnnotations)
    {
        this.includeDefaultAnnotations = includeDefaultAnnotations;
    }

    @Override
    public String toString()
    {
        return String.format("{StabCompileOptions includeRuntime=%s includeDefaultAnnotations=%s}", includeRuntime, includeDefaultAnnotations);
    }
}
