package note.lym.org.sampleproject.nav.dagger.compoent;

import dagger.Component;
import note.lym.org.sampleproject.nav.dagger.activity.ModuleActivity;
import note.lym.org.sampleproject.nav.dagger.module.ModuleActivityModule;

/**
 * Desp.
 *
 * @author yaoming.li
 * @since 2017-08-11 18:26
 */
@Component(modules = ModuleActivityModule.class)
public interface ModuleActivityComponent {
    void inject(ModuleActivity activity);
}
