package test.webview;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TestWebView extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		WebView webview = new WebView(this);
		setContentView(webview);
		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		// String summary =
		// "<html><body>You scored <b>192</b> points.</body></html>";
		// webview.loadData(summary, "text/html", null);

		// String url = "http://www.baidu.com";
		// webview.loadUrl(url);

		webview.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
//		webview.setWebChromeClient(new WebChromeClient() {
//			public void onProgressChanged(WebView view, int progress) {
//				// Activities and WebViews measure progress with different
//				// scales.
//				// The progress meter will automatically disappear when we reach
//				// 100%
//				activity.setProgress(progress * 1000);
//			}
//			@Override
//			public boolean onJsAlert(WebView view, String url, String message,
//					JsResult result) {
//				Toast.makeText(activity, message, 1000).show();
//				return true;
//			}
//		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("kmm"))
					return true;
				else
					return super.shouldOverrideUrlLoading(view, url);
			}
		});

//		webview.loadUrl("kmm://www.360kanmm.com/albums?id=53e837784563875d2e18396b");
		Intent it = new Intent();
		it.setAction(Intent.ACTION_VIEW);	
		it.addCategory(Intent.CATEGORY_DEFAULT);
		it.addCategory(Intent.CATEGORY_BROWSABLE);
		it.setData(Uri.parse("http://www.360kanmm.com/albums"));
		
		
		final PackageManager packageManager = this.getPackageManager();
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				it, PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfo.size() > 0) {
			System.out.println("" + resolveInfo.size());
		}
		
		
		   
//		startActivity(it);
//		webview.loadUrl("http://tb.cn/x/tm/?locate=icon-1&spm=a2141.1.1.icon-1&actparam=1_46784_h17639_%E5%85%A5%E5%8F%A3-%E5%A4%A9%E7%8C%AB");
//		webview.loadUrl("http://h5.m.taobao.com/dd/index.htm?_ddid=carte/delivery/233448");
		webview.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		
//		Intent intent = new Intent( "com.android.launcher.action.INSTALL_SHORTCUT");
//		BitmapDrawable bitmapDraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_launcher);
//		Bitmap icon = bitmapDraw.getBitmap();
//		Intent shortcut = new Intent();
//		shortcut.setAction("com.taobao.huoyan.scan");
//		shortcut.addCategory("android.intent.category.DEFAULT");
//		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcut); 
//		
//		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"huoyan"); 
//		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
//		intent.putExtra("duplicate", false); 
//		sendBroadcast(intent);
		
		
		Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		openAlbumIntent.setType("image/*");
		this.startActivityForResult(openAlbumIntent, 11);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
//		ChatImageManager cm = new ChatImageManager();
	}
	 
}
