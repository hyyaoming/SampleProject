package note.lym.org.sampleproject.nav;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import note.lym.org.sampleproject.R;
import note.lym.org.sampleproject.nav.autofittextview.TextActivity;
import note.lym.org.sampleproject.nav.base.BaseActivity;
import note.lym.org.sampleproject.nav.bindview.BindViewActivity;
import note.lym.org.sampleproject.nav.dagger.activity.DaggerActivity;
import note.lym.org.sampleproject.nav.domen.HomeBean;
import note.lym.org.sampleproject.nav.statusbar.StatusBarActivity;
import project.recyclerview.lym.org.recyclerviewlibrary.adapter.BaseFastAdapter;
import project.recyclerview.lym.org.recyclerviewlibrary.listener.OnItemClickListener;
import project.recyclerview.lym.org.recyclerviewlibrary.viewholder.BaseViewHolder;

/**
 * @author yaoming.li
 * @since 2017-07-11 14:30
 */
public class MainActivity extends BaseActivity {


    private static final String[] sSampleName = {"沉浸式状态栏", "dagger小案例", "autoFitTextView", "bindView注解"};
    private static final Class[] sClassList = {StatusBarActivity.class, DaggerActivity.class, TextActivity.class, BindViewActivity.class};
    private ArrayList list = new ArrayList();

    private RecyclerView mRvList;

    @Override
    protected void initData() {
        for (int i = 0; i < sSampleName.length; i++) {
            HomeBean homeBean = new HomeBean();
            homeBean.setClassName(sSampleName[i]);
            homeBean.setmClazz(sClassList[i]);
            list.add(homeBean);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mRvList = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvList.setLayoutManager(manager);
        mRvList.setHasFixedSize(true);
        mRvList.setAdapter(new SampleListAdapter(R.layout.rv_list_item, list));
    }

    @Override
    protected void initListener() {
            mRvList.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseFastAdapter adapter, View view, int position) {
                    Intent intent = new Intent(MainActivity.this,sClassList[position]);
                    startActivity(intent);
                }
            });
    }

    private class SampleListAdapter extends BaseFastAdapter<HomeBean, BaseViewHolder> {

        public SampleListAdapter(int layoutResId, List<HomeBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeBean item) {
            TextView tv = helper.getView(R.id.tv_list);
            tv.setText(item.getClassName());
        }
    }


}
