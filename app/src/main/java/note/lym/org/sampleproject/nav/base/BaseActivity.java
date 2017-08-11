package note.lym.org.sampleproject.nav.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 *
 * @author yaoming.li
 * @since 2017-08-11 17:03
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData();
        initView();
        initListener();
    }

    protected  void initListener(){};

    protected abstract void initData();

    protected abstract int getLayoutId();

    protected  abstract  void initView() ;
    
}
