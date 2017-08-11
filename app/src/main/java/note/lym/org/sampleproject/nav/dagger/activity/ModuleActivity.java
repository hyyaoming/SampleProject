package note.lym.org.sampleproject.nav.dagger.activity;

import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import note.lym.org.sampleproject.R;
import note.lym.org.sampleproject.nav.base.BaseActivity;
import note.lym.org.sampleproject.nav.dagger.compoent.DaggerModuleActivityComponent;
import note.lym.org.sampleproject.nav.dagger.module.ModuleActivityModule;
import note.lym.org.sampleproject.nav.dagger.module_class.ModuleClass;

/**
 * Dagger-> Module注入
 *
 * @author yaoming.li
 * @since 2017-08-11 18:07
 */
public class ModuleActivity extends BaseActivity {

    @Inject
    ModuleClass data;
    TextView mTv;

    @Override
    protected void initData() {
        DaggerModuleActivityComponent.builder().moduleActivityModule(new ModuleActivityModule()).build().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_module;
    }

    @Override
    protected void initView() {
        mTv = (TextView) findViewById(R.id.tv_module);
    }

    public void click_module(View view) {
        mTv.setText(data.getString());
    }

}
