package com.ak6l.MorseTrainer;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PracticeLettersActivity extends ListActivity {
	private MorseAudioService mMAudioSvc;
	private boolean mIsBound = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_letters);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        String[] letterGroups = getResources().getStringArray(R.array.letter_groups);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.letter_groups_list_item, letterGroups));
        
        ListView letterGroupsListView = getListView();
        letterGroupsListView.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		String viewText = ((TextView) view).getText().toString();
        		Toast.makeText(getApplicationContext(), viewText, Toast.LENGTH_SHORT).show();
                mMAudioSvc.playMorseString(viewText);
        	}
        });
        
        doBindService();
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mMAudioSvc = ((MorseAudioService.MorseAudioBinder)service).getService();

            // Tell the user about this for our demo.
            Toast.makeText(PracticeLettersActivity.this, R.string.morse_audio_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mMAudioSvc = null;
            Toast.makeText(PracticeLettersActivity.this, R.string.morse_audio_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(PracticeLettersActivity.this, 
                MorseAudioService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}