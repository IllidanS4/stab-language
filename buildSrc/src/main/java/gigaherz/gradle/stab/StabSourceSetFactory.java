package gigaherz.gradle.stab;

import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.model.ObjectFactory;

public class StabSourceSetFactory implements NamedDomainObjectFactory<StabSourceSet>
{
    private final ObjectFactory objectFactory;

    public StabSourceSetFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public StabSourceSet create(String name) {
        return new DefaultStabSourceSet(name, objectFactory);
    }
}
