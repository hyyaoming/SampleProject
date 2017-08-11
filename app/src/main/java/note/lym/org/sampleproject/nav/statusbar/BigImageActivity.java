package note.lym.org.sampleproject.nav.statusbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import note.lym.org.sampleproject.R;

/**
 * Desp.
 *
 * @author yaoming.li
 * @since 2017-07-26 18:08
 */
public class BigImageActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        StatusBarManager.setStatusBar(this, Color.TRANSPARENT, true);
    }
}
