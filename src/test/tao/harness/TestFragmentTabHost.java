package test.tao.harness;

import com.example.hongyunbasic.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by hongyunmhy on 15/11/12.
 */
public class TestFragmentTabHost extends FragmentActivity{

	private static final String TAB_1_TAG = "tab_1";
	private static final String TAB_2_TAG = "tab_2";
	private static final String TAB_3_TAG = "tab_3";
	private static final String TAB_4_TAG = "tab_4";
	private static final String TAB_5_TAG = "tab_5";
	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		initView();
	}

	private void initView()
	{
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setBackground(getResources().getDrawable(R.drawable.tm_maintab_rect));
//		mTabHost.getTabWidget().setPadding(0, 10, 0, 0);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mTabHost.getTabWidget().getLayoutParams();
		params.setMargins(0,50,0,0);

		mTabHost.getTabWidget().setLayoutParams(params);

		// mTabHost.addTab(mTabHost.newTabSpec(TAB_1_TAG).setIndicator("Talk", getResources().getDrawable(R.drawable.ic_launcher)), TalkContainerFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_1_TAG).setIndicator("Talk"), TalkContainerFragment.class, null);
//		mTabHost.addTab(mTabHost.newTabSpec(TAB_2_TAG).setIndicator("Learn"), LearnContainerFragment.class, null);
//		mTabHost.addTab(mTabHost.newTabSpec(TAB_3_TAG).setIndicator("Go"), GoContainerFragment.class, null);
//		mTabHost.addTab(mTabHost.newTabSpec(TAB_4_TAG).setIndicator("Watch"), WatchContainerFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_5_TAG).setIndicator("More"), MoreContainerFragment.class, null);

            /* Increase tab height programatically
             * tabs.getTabWidget().getChildAt(1).getLayoutParams().height = 150;
             */

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			final TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			if (tv == null)
				continue;
			else
				tv.setTextSize(10);

		}

	}

	@Override
	public void onBackPressed() {
		boolean isPopFragment = false;
		String currentTabTag = mTabHost.getCurrentTabTag();
		if (currentTabTag.equals(TAB_1_TAG)) {
//			isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_1_TAG)).popFragment();
			FragmentManager childFragmentMgr = getSupportFragmentManager().findFragmentByTag(TAB_1_TAG).getChildFragmentManager();
			if(childFragmentMgr!=null){
				int count = childFragmentMgr.getBackStackEntryCount();
				if(count>0){
					childFragmentMgr.popBackStack();
					isPopFragment = true;
				}
			}
		} else if (currentTabTag.equals(TAB_2_TAG)) {
			isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_2_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_3_TAG)) {
			isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_3_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_4_TAG)) {
			isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_4_TAG)).popFragment();
		} else if (currentTabTag.equals(TAB_5_TAG)) {
//			isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_5_TAG)).popFragment();
			int count = getSupportFragmentManager().getBackStackEntryCount();
			if(count>0){
				getSupportFragmentManager().popBackStack();
				isPopFragment = true;
			}
		}
		if (!isPopFragment) {
			finish();
		}
	}


}
