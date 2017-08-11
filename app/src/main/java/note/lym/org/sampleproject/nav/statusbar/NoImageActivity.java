package note.lym.org.sampleproject.nav.statusbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import note.lym.org.sampleproject.R;
import note.lym.org.sampleproject.nav.statusbar.StatusBarManager;

/**
 * Desp.
 *
 * @author yaoming.li
 * @since 2017-07-26 18:10
 */
public class NoImageActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_big_image);
        StatusBarManager.setStatusBar(this, ContextCompat.getColor(this, R.color.colorPrimaryDark), false);
    }
}
