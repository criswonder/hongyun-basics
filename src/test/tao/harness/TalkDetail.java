package test.tao.harness;

import com.example.hongyunbasic.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hongyunmhy on 15/11/12.
 */
public class TalkDetail extends BaseContainerFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.talk_detail, container, false);
		return rootView;
	}
}
