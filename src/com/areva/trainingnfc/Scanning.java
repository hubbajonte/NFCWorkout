package com.areva.trainingnfc;
/*
 * Klass som hanterar skanningen utav NFC-taggarna/datan, specifikt en NDEF tag
 * Delar utav denna klass är hämtad från Googles tutorials
 * 
 * Jonathan Arevalo Garay
 * 
 * */
import com.areva.trainingnfc.Models.Taggie;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Scanning extends Activity {

	//Attribut, attribut för NFC och attribut för vyer och databas
	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String TAG = "NfcDemo";
	private TextView mTextView;
	private TextView txtScanned;
	private NfcAdapter mNfcAdapter;
	private MyDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanning);
		//Hämtar våra vyer
		mTextView = (TextView) findViewById(R.id.txtNFC);
		txtScanned = (TextView) findViewById(R.id.txtScanned);
		//Hämtar vår NFC-adapter, alltså hårdvaran
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		//Skapar en anslutning/instans utav vår databas
		db = new MyDatabase(this);
		//Om våran enhet inte stödjer NFC
		if (mNfcAdapter == null) {
			// Stoppa har om enheten inte stödjer NFC
			Toast.makeText(this, "Den här enheten stödjer tyvärr inte NFC.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		//Om NFC inte är aktiverat, på en enhet som har NFC
		if (!mNfcAdapter.isEnabled()) {
			mTextView.setText("NFC är inte aktiverat.");
		} else {
			//Om NFC är aktiverat
			mTextView.setText("NFC är redo att skanna.");
		}
		
		handleIntent(getIntent());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scanning, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_feed) {
			Intent feedActivity = new Intent (this, Feed.class);
			startActivity(feedActivity);
			return true;
		}
			if (id == R.id.action_about) {
				Intent aboutActivity = new Intent (this, About.class);
				startActivity(aboutActivity);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
		
	
	@Override
	protected void onResume() {
		super.onResume();
		
		/*
		 * It's important, that the activity is in the foreground (resumed). Otherwise
		 * an IllegalStateException is thrown. 
		 */
		setupForegroundDispatch(this, mNfcAdapter);
	}
	
	@Override
	protected void onPause() {
		/*
		 * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
		 */
		stopForegroundDispatch(this, mNfcAdapter);
		
		super.onPause();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		/*
		 * This method gets called, when a new Intent gets associated with the current activity instance.
		 * Instead of creating a new activity, onNewIntent will be called. For more information have a look
		 * at the documentation.
		 * 
		 * In our case this method gets called, when the user attaches a Tag to the device.
		 */
		handleIntent(intent);
	}
	
	//Hantera vår intent om appen körs i bakgrunden/är installerad
	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			
			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				new NdefReaderTask().execute(tag);
				
			} else {
				Log.d(TAG, "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			
			// In case we would still use the Tech Discovered Intent
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();
			
			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					new NdefReaderTask().execute(tag);
					break;
				}
			}
		}
	}
	
	/**
	 * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
	 * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][]{};

		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}
		
		adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
	}

	/**
	 * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
	 * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}
	
	/**
	 * Denna klass läser av taggen i bakgrunden
	 *
	 */
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		@Override
		protected String doInBackground(Tag... params) {
			Tag tag = params[0];
			//Om NDEF inte stöds
			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				return null;
			}

			NdefMessage ndefMessage = ndef.getCachedNdefMessage();
			//Om vår enkodning inte stöds
			NdefRecord[] records = ndefMessage.getRecords();
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
					try {
						return readText(ndefRecord);
					} catch (UnsupportedEncodingException e) {
						Log.e(TAG, "Unsupported Encoding", e);
					}
				}
			}

			return null;
		}
		//För att läsa av vår text/data i vår NFC-tag
		private String readText(NdefRecord record) throws UnsupportedEncodingException {
			/*
			 * See NFC forum specification for "Text Record Type Definition" at 3.2.1 
			 * 
			 * http://www.nfc-forum.org/specs/
			 * 
			 * bit_7 defines encoding
			 * bit_6 reserved for future use, must be 0
			 * bit_5..0 length of IANA language code
			 */

			byte[] payload = record.getPayload();

			// Hämta enkodningen
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

			// Hämta språkkoden
			int languageCodeLength = payload[0] & 0063;
			
			// Hämta den lagrade texten, returnera den 
			return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
		}
		
		@Override
		//Skriver ut resultatet och lagrar detta i vår databas
		protected void onPostExecute(String result) {
			if (result != null) {
				//Sätter resultaten i vår vy
				txtScanned.setText("Avläsning: " + result);
				//Lagra i databas
				db.addTag(new Taggie (result));
			}
		}
	}
}
