package test.tao.harness;

import com.example.hongyunbasic.R;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hongyunmhy on 15/11/12.
 */
public class Talk extends BaseContainerFragment {

	/** Define global variables over here */
	//private ProgressDialog pDialog;
//	StaticApiList sal;
//	TalkModelAll  tma;
	JSONObject myJasonObject = null;
	private ListView lv;
//	private ArrayList<TalkModelAll> m_ArrayList = null;
	//ArrayList<String> stringArrayList = new ArrayList<String>();
//	TalkArrayAdapter taa;
	Set<String> uniqueValues = new HashSet<String>();
	TextView    rowTextView  = null;
	boolean     vivek        = false;

	int    postid;
	String title;
	String thumsrc;
	String largeimg;
	String excert;
	String description;
	String cat;
	String myUrl;
	String jsonString;
	int    mCurCheckPosition;
	String check_state = null;
	String       ccc;
	LinearLayout myLinearLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.talk, container, false);

		Button btn = (Button) rootView.findViewById(R.id.btn_click);
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
				((BaseContainerFragment)getParentFragment()).replaceFragment(fragment, true);
			}
		});

		return rootView;
	}
}
