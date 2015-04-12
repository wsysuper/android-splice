/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sjtu.android.splice;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import android.app.Activity;
import android.os.*;
import android.bluetooth.*;
import android.content.Intent;
import android.widget.Toast;

public class ClientSocketActivity extends Activity{
    private static final String TAG = ClientSocketActivity.class.getSimpleName();
    private static final int REQUEST_DISCOVERY = 0x1;;
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    BluetoothSocket socket = null;
    private byte[] imageArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent enabler = getIntent();
        imageArray = enabler.getByteArrayExtra("image");
        setContentView(R.layout.screen);
        
        if (!_bluetooth.isEnabled()) {
            finish();
            return;
        }
        
        Intent intent = new Intent(this, DiscoveryActivity.class);
        /* 提示选择一个要连接的服务器 */
        
        Toast.makeText
                (this, "select device to connect", Toast.LENGTH_SHORT).show();
        
        /* 跳转到搜索的蓝牙设备列表区，进行选择 */
        startActivityForResult(intent, REQUEST_DISCOVERY);
    }

    /* 选择了服务器之后进行连接 */
    protected void onActivityResult
            (int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_DISCOVERY) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        final BluetoothDevice device
                = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        try {
            socket = device.createRfcommSocketToServiceRecord
                    (UUID.fromString
                    ("a60f35f0-b93a-11de-8a39-08002009c666"));
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //send data
        try {
        	OutputStream out = socket.getOutputStream();
			out.write(imageArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
