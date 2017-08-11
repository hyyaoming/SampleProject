package note.lym.org.sampleproject.nav.dagger.activity;

import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import note.lym.org.sampleproject.R;
import note.lym.org.sampleproject.nav.base.BaseActivity;
import note.lym.org.sampleproject.nav.dagger.compoent.DaggerInjectActivityComponent;
import note.lym.org.sampleproject.nav.dagger.inject_class.InjectClass;

/**
 * @author yaoming.li
 * @since 2017-08-11 18:07
 */
public class InjectActivity extends BaseActivity {

    @Inject
    InjectClass data;
    TextView mTv;


    @Override
    protected void initData() {
        DaggerInjectActivityComponent.create().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inject;
    }

    @Override
    protected void initView() {
        mTv = (TextView) findViewById(R.id.tv_inject);
    }

    public void click_inject(View view) {
        mTv.setText(data.getString());
    }

}
