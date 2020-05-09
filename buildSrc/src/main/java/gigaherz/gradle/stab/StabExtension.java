package gigaherz.gradle.stab;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.internal.file.FileResolver;

public class StabExtension
{
    private final NamedDomainObjectContainer<StabSourceSet> sourceSetsContainer;

    public StabExtension(Project project, FileResolver fileResolver) {
        sourceSetsContainer = project.container(
                StabSourceSet.class,
                new StabSourceSetFactory(project.getObjects())
        );
    }

    public NamedDomainObjectContainer<StabSourceSet> getSourceSetsContainer() {
        return sourceSetsContainer;
    }

    public void srcDir(String file) {
        sourceSetsContainer.getByName("main").getStab().srcDir(file);
    }
}
