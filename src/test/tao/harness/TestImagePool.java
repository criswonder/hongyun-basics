package test.tao.harness;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.taobao.imagebinder.ImagePoolBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.hongyunbasic.R;
import com.taobao.tao.imagepool.ImageCache;

public class TestImagePool extends FragmentActivity {
	public class AdapterImagePool extends BaseAdapter {
		public AdapterImagePool() {
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i;

			if (convertView == null) {
				i = new ImageView(TestImagePool.this);
				i.setScaleType(ImageView.ScaleType.FIT_CENTER);
				i.setLayoutParams(new GridView.LayoutParams(50, 50));
			} else {
				i = (ImageView) convertView;
			}

			mImagePool.setImageDrawable(getItem(position) + "", i);
			return i;
		}

		public final int getCount() {
			return Images.testImages.length;
		}

		public final Object getItem(int position) {
			return Images.testImages[position];
		}

		public final long getItemId(int position) {
			return position;
		}
	}

	public class FragmentImagePool extends Fragment {
		GridView mGv;
		AdapterImagePool mAdapter;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View inflate = inflater.inflate(R.layout.frg_image_pool, null);
			mGv = (GridView) inflate.findViewById(R.id.gridView1);
			mAdapter = new AdapterImagePool();
			mGv.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
			return inflate;
		}
	}

	private ImagePoolBinder mImagePool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().add(android.R.id.content,new FragmentImagePool(), "frg").commit();
		mImagePool = new ImagePoolBinder("andy_test", getApplication(),
				ImagePoolBinder.ImageGroupImp.PRIORITY_TOP,
				ImageCache.CACHE_CATEGORY_MRU);
		
	}
}
