package test.tao.harness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CalendarAlarmReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println(this.getClass().getSimpleName()+":"+intent.getAction());
	}
}
