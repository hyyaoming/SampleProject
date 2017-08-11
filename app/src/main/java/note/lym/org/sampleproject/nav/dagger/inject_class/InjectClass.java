package note.lym.org.sampleproject.nav.dagger.inject_class;

import javax.inject.Inject;

/**
 * Inject注入
 *
 * @author yaoming.li
 * @since 2017-08-11 18:17
 */
public class InjectClass {
    @Inject
    public InjectClass() {

    }

    public String getString() {
        return InjectClass.class.getSimpleName();
    }

}
