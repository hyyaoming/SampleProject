package note.lym.org.sampleproject.nav.dagger.compoent;

import dagger.Component;
import note.lym.org.sampleproject.nav.dagger.activity.InjectActivity;

/**
 *
 * @author yaoming.li
 * @since 2017-08-11 18:15
 */
@Component
public interface InjectActivityComponent {
    void inject(InjectActivity activity);
}
