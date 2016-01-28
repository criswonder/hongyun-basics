package test.tao.harness;

import com.example.hongyunbasic.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hongyunmhy on 15/11/12.
 */
public class BaseContainerFragment extends Fragment {
	private static final String TAG = "TMFragment";
	private static final boolean isDebug = true;
	public void replaceFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.replace(R.id.container_framelayout, fragment);
		transaction.commit();
		getChildFragmentManager().executePendingTransactions();
	}

	public boolean popFragment() {
		Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
		boolean isPop = false;
		if (getChildFragmentManager().getBackStackEntryCount() > 0) {
			isPop = true;
			getChildFragmentManager().popBackStack();
		}
		return isPop;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if(isDebug)Log.d(TAG,"onAttach,activity="+activity+",this="+this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(isDebug)Log.d(TAG,"onCreate,"+this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if(isDebug)Log.d(TAG,"onCreateView,"+this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if(isDebug)Log.d(TAG,"onActivityCreated,"+this);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if(isDebug)Log.d(TAG,"onStart,"+this);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if(isDebug)Log.d(TAG,"onResume,"+this);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if(isDebug)Log.d(TAG,"onPause,"+this);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		if(isDebug)Log.d(TAG,"onStop,"+this);
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		if(isDebug)Log.d(TAG,"onDestroyView,"+this);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(isDebug)Log.d(TAG,"onDestroy,"+this);
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		if(isDebug)Log.d(TAG,"onDetach,"+this);
	}
}
