package note.lym.org.sampleproject.nav.bindview;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author yaoming.li
 * @since 2017-08-04 17:03
 */
public class RegisterView {

    public static void inject(Activity activity){
        parseViewId(activity);
        parseViewClickId(activity);
    }

    private static void parseViewClickId(final Activity activity) {
        View view = null;
        Class<?> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for(final Method method : methods){
            if(method.isAnnotationPresent(OnClick.class)){
                OnClick click = method.getAnnotation(OnClick.class);
                int[] id = click.value();
                for(Integer integer : id){
                    view = activity.findViewById(integer);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.invoke(activity);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        }
    }

    private static void parseViewId(Activity activity) {
        View view = null;
        Class<?> clazz = activity.getClass();
        Field[] fields =clazz.getDeclaredFields();
        for(Field field : fields){
            if(field.isAnnotationPresent(BindView.class)){
                BindView bindView = field.getAnnotation(BindView.class);
                int id = bindView.value();
                field.setAccessible(true);
                view = activity.findViewById(id);
                try {
                    field.set(activity,view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
