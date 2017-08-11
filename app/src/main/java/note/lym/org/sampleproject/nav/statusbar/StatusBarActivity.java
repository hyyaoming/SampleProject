package note.lym.org.sampleproject.nav.statusbar;

import android.content.Intent;
import android.view.View;

import note.lym.org.sampleproject.R;
import note.lym.org.sampleproject.nav.base.BaseActivity;

/**
 * Desp.
 *
 * @author yaoming.li
 * @since 2017-08-11 17:16
 */
public class StatusBarActivity extends BaseActivity {
    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_statusbar;
    }

    @Override
    protected void initView() {

    }

    public void immersion(View view) {
        startActivity(new Intent(this, BigImageActivity.class));
    }

    public void lucency(View view) {
        startActivity(new Intent(this, NoImageActivity.class));
    }

}
