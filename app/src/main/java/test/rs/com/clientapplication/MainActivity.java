package test.rs.com.clientapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Messenger myService;
    boolean isBound = false;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);

        Intent intent = new Intent("test.rs.com.serviceapplication.MyService");
        intent.setPackage("test.rs.com.serviceapplication");   // This Line will be work for abobe 5.0 version
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        String data = "Data";
    }

    public void sendMessage(View view)
    {
        if (!isBound) return;

        Message msg = Message.obtain();
        msg.replyTo = new Messenger(new ResponseHandler());
        Bundle bundle = new Bundle();
        String clentMessage=  editText.getText().toString();
        bundle.putString("MyString", clentMessage);
        msg.setData(bundle);
        try {
            myService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }




    private ServiceConnection myConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            myService = new Messenger(service);
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            myService = null;
            isBound = false;
        }
    };



    class  ResponseHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            String dataString = data.getString("ResponseString");
            Toast.makeText(getApplicationContext(),
                    dataString, Toast.LENGTH_SHORT).show();
        }
    }
}
