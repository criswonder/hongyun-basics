package test.tao.harness;

import com.example.hongyunbasic.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by hongyunmhy on 15/11/12.
 */
public class MoreContainerFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.talk, container, false);

		Button btn = (Button) rootView.findViewById(R.id.btn_click);
		btn.setText("MoreContainerFragment");
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
//Here TalkDetail is name of class that needs to open
				TalkDetail fragment = new TalkDetail();
				// if U need to pass some data
				Bundle bundle = new Bundle();

//				bundle.putString("title", m_ArrayList.get(arg2).title);
//				bundle.putString("largeimg", m_ArrayList.get(arg2).largeimg);
//				bundle.putString("excert", m_ArrayList.get(arg2).excert);
//				bundle.putString("description", m_ArrayList.get(arg2).description);
//				bundle.putString("cat", m_ArrayList.get(arg2).cat);
				//bundle.putInt("postid", m_ArrayList.get(arg2).postid);

				fragment.setArguments(bundle);
//				((BaseContainerFragment)getParentFragment()).replaceFragment(fragment, true);
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.add(R.id.fl_bottom_content,fragment);
				transaction.addToBackStack("more2");
				transaction.commit();
			}
		});

		return rootView;
	}
}
