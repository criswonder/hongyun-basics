package test.tao.harness;

import android.content.Intent;
import android.net.Uri;

public class Test6June extends TestBaseActivity{
	public void testSendSmsBySysApp(){
		  Uri uri = Uri.parse("smsto:13800000000");
          Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
          intent.putExtra("sms_body", "The SMS text");
          startActivity(intent);
	}
}
