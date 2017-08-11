package note.lym.org.sampleproject.nav.dagger.activity;

import android.content.Intent;
import android.view.View;

import note.lym.org.sampleproject.R;
import note.lym.org.sampleproject.nav.base.BaseActivity;

/**
 *
 * @author yaoming.li
 * @since 2017-08-11 17:16
 */
public class DaggerActivity extends BaseActivity {
    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dagger;
    }

    @Override
    protected void initView() {

    }

    public void inject(View view){
        startActivity(new Intent(this,InjectActivity.class));
    }

    public void module(View view){
        startActivity(new Intent(this,ModuleActivity.class));
    }

}
