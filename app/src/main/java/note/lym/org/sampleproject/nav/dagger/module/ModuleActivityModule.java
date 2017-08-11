package note.lym.org.sampleproject.nav.dagger.module;

import dagger.Module;
import dagger.Provides;
import note.lym.org.sampleproject.nav.dagger.module_class.ModuleClass;

/**
 *
 * @author yaoming.li
 * @since 2017-08-11 18:24
 */
@Module
public class ModuleActivityModule {

    @Provides
    public ModuleClass getModuleClass() {
        return new ModuleClass();
    }

}
