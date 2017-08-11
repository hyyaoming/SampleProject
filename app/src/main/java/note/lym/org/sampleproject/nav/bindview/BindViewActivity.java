package note.lym.org.sampleproject.nav.bindview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import note.lym.org.sampleproject.R;

/**
 * 通过反射注册控件
 *
 * @author yaoming.li
 * @since 2017-08-04 17:01
 */
public class BindViewActivity extends Activity {

    @BindView(R.id.btn_register)
    Button btn;

    @BindView(R.id.btn_bind)
    Button btnBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_view);
        RegisterView.inject(this);
    }

    @OnClick(R.id.btn_register)
    public void onClick(){
        btn.setText("这是我注入的");
        Toast.makeText(BindViewActivity.this,"哈哈哈",Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btn_bind)
    public void bind(){
        btnBind.setText("绑定事件成功");
        Toast.makeText(BindViewActivity.this,"嘻嘻嘻",Toast.LENGTH_SHORT).show();
    }
}
