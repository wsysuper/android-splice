package sjtu.android.splice;
import java.io.*;
import java.util.*;
import android.app.Activity;
import android.os.*;
import android.bluetooth.*;
import android.content.Intent;
import android.util.Log;

public class ServerSocketActivity extends Activity{
    /* 一些常量，代表服务器的名称 */
    public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
    public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";
    
    private static final String TAG = ServerSocketActivity.class.getSimpleName();
    private Handler _handler = new Handler();

    /* 取得默认的蓝牙适配器 */
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    /* 蓝牙服务器 */
    private BluetoothServerSocket _serverSocket;

    /* 线程-监听客户端的链接 */
    private Thread _serverWorker = new Thread() {
        public void run() {
            listen();
        };
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!_bluetooth.isEnabled()) {
            finish();
            return;
        }
        /* 开始监听 */
        _serverWorker.start();
	}

    protected void onDestroy() {
        super.onDestroy();
        shutdownServer();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        shutdownServer();
    }
    
    private void shutdownServer() {
        new Thread() {
            @Override
            public void run() {
                _serverWorker.interrupt();
                if (_serverSocket != null) {
                    try {
                        _serverSocket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "", e);
                    }
                    _serverSocket = null;
                }
            };
        }.start();
    }
    protected void listen() {
        try {
            /* 创建一个蓝牙服务器
             * 参数分别：服务器名称、UUID
             */
            _serverSocket = _bluetooth.listenUsingRfcommWithServiceRecord(
                    PROTOCOL_SCHEME_RFCOMM,
                    UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666")
                    );
            _handler.post(new Runnable() {
                public void run() {
                }
            });
            /* 接受客户端的连接请求 */
            BluetoothSocket socket = _serverSocket.accept();
            /* 处理请求内容 */
            
            if (socket != null) {
                InputStream inputStream = socket.getInputStream();
                final byte[] imageBytes = new byte[1024*768];
                inputStream.read(imageBytes);
                _handler.post(new Runnable() {
                    public void run() {
                    	Intent intent = new Intent(ServerSocketActivity.this, ServerImageActivity.class);
                    	intent.putExtra("image", imageBytes);
                    }
                });
            }
        } catch (IOException e) {}
    }
}
