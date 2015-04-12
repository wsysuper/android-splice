package sjtu.android.splice;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Splice extends Activity {
    /* ȡ��Ĭ�ϵ����������� */
    private BluetoothAdapter _bluetooth
            = BluetoothAdapter.getDefaultAdapter();
    /* ��������� */
    private static final int REQUEST_ENABLE = 0x1;
    /* �����ܹ������� */
    private static final int REQUEST_DISCOVERABLE = 0x2;
    private Bitmap bitmap;
    private byte[] imageBytes;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
    }
    
    public void ShowImage(View view){
    	setContentView(R.layout.screen);

    	Intent intent = new Intent();
    	intent.setType("image/*");
    	intent.setAction(Intent.ACTION_GET_CONTENT);
    	startActivityForResult(intent, 1);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (resultCode==RESULT_OK){
    		Uri uri = data.getData();
    		Log.e("uri", uri.toString());
    		ContentResolver cr = this.getContentResolver();
    		try{
    			bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
    			ByteArrayOutputStream stream = new ByteArrayOutputStream();
    			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
    			imageBytes = stream.toByteArray();
    			
				Intent enabler = new Intent(this, ClientSocketActivity.class);
			    enabler.putExtra("image", imageBytes);
			    startActivity(enabler);
    			
    		}catch(FileNotFoundException e){
    			e.printStackTrace();
    		}
    	}
    }
    
    /*��������*/
    public void CreateScreen(View view) {
        Intent enabler = new Intent(this, ServerSocketActivity.class);
        startActivity(enabler);
    }

    /*��������*/
    public void JoinNetwork(View view){
        Intent enabler = new Intent(this, ClientSocketActivity.class);
        enabler.putExtra("image", imageBytes);
        startActivity(enabler);
    }

    /*��ʾ����ѡ��*/
    public void NetOption(View view){
        setContentView(R.layout.netoption);
    }

    /*������*/
    public void OpenBluetooth(View view){
        // �û����������
        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enabler, REQUEST_ENABLE);
        //������
        _bluetooth.enable();
    }

    /*�ر�����*/
    public void CloseBluetooth(View view){
        _bluetooth.disable();
    }

    /*��������*/
    public void EnableSearchable(View view){
        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(enabler, REQUEST_DISCOVERABLE);
    }

    public void Goback(View view){
        setContentView(R.layout.main);
    }
}
