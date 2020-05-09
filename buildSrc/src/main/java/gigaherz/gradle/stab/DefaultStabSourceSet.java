package gigaherz.gradle.stab;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.file.collections.DefaultDirectoryFileTreeFactory;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.reflect.HasPublicType;
import org.gradle.api.reflect.TypeOf;
import org.gradle.api.tasks.ScalaSourceSet;
import org.gradle.util.ConfigureUtil;

import javax.annotation.Nonnull;

@SuppressWarnings("UnstableApiUsage")
public class DefaultStabSourceSet implements StabSourceSet
{
    private final String name;
    private final SourceDirectorySet stab;

    public DefaultStabSourceSet(String displayName, ObjectFactory objectFactory) {
        this.name = displayName;
        this.stab = objectFactory.sourceDirectorySet("stab", displayName + " Stab source");
        this.stab.getFilter().include("**/*.java", "**/*.stab");
    }

    @Nonnull
    @Override
    public String getName()
    {
        return name;
    }

    @Nonnull
    @Override
    public SourceDirectorySet getStab() {
        return stab;
    }

    @Nonnull
    @Override
    public StabSourceSet stab(Closure<?> clsr) {
        ConfigureUtil.configure(clsr, stab);
        return this;
    }

    @Nonnull
    @Override
    public StabSourceSet stab(Action<? super SourceDirectorySet> configureAction) {
        configureAction.execute(this.getStab());
        return this;
    }

    @Override
    public String toString()
    {
        return String.format("stab source set '%s'", name);
    }
}
