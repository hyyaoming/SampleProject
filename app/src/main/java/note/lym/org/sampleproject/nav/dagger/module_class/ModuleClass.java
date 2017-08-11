package note.lym.org.sampleproject.nav.dagger.module_class;

/**
 * Module注入
 *
 * @author yaoming.li
 * @since 2017-08-11 18:24
 */
public class ModuleClass {

    public ModuleClass(){

    }


    public String getString(){
        return ModuleClass.class.getSimpleName();
    }

}
