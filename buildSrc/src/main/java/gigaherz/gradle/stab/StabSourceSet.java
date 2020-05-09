package gigaherz.gradle.stab;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.file.SourceDirectorySet;

import javax.annotation.Nonnull;

public interface StabSourceSet
{
    @Nonnull
    String getName();

    @Nonnull
    SourceDirectorySet getStab();

    @Nonnull
    StabSourceSet stab(Closure<?> closure);

    @Nonnull
    StabSourceSet stab(Action<? super SourceDirectorySet> configureAction);
}
