package test.tao.harness;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

public class ExternalBindService extends Service {
//	// Binder given to clients
//	private final IBinder mBinder = new LocalBinder();
//	// Random number generator
//	private final Random mGenerator = new Random();
//
//	/**
//	 * Class used for the client Binder. Because we know this service always
//	 * runs in the same process as its clients, we don't need to deal with IPC.
//	 */
//	public class LocalBinder extends Binder {
//		ExternalBindService getService() {
//			// Return this instance of LocalService so clients can call public
//			// methods
//			return ExternalBindService.this;
//		}
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return mBinder;
//	}
//
//	/** method for clients */
//	public int getRandomNumber() {
//		return mGenerator.nextInt(100);
//	}
	
	 static final int MSG_SAY_HELLO = 1;

	    /**
	     * Handler of incoming messages from clients.
	     */
	    class IncomingHandler extends Handler {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case MSG_SAY_HELLO:
	                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
	                    break;
	                default:
	                    super.handleMessage(msg);
	            }
	        }
	    }

	    /**
	     * Target we publish for clients to send messages to IncomingHandler.
	     */
	    final Messenger mMessenger = new Messenger(new IncomingHandler());

	    /**
	     * When binding to the service, we return an interface to our messenger
	     * for sending messages to the service.
	     */
	    @Override
	    public IBinder onBind(Intent intent) {
	        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
	        return mMessenger.getBinder();
	    }
}
