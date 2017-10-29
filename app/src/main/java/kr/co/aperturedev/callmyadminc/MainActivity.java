package kr.co.aperturedev.callmyadminc;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permisionRequest = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE);

            if (permisionRequest == PackageManager.PERMISSION_DENIED)
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NETWORK_STATE)||shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NETWORK_STATE)) {
                    AlertDialog.Builder permissioCheck = new AlertDialog.Builder(MainActivity.this);
                    permissioCheck.setTitle("권한요청")
                            .setMessage("권한요청")
                            .setCancelable(false)
                            .setPositiveButton("동의", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "앱 사용이 불가합니다", Toast.LENGTH_SHORT).show();
                                    start();
                                }
                            })
                            .create()
                            .show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                } else {
                start();
            }
        } else {
            start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start();
                } else {
                    Toast.makeText(MainActivity.this, "앱을 사용할수 없습니다", Toast.LENGTH_SHORT).show();
                    start();
                }
                return;
        }
    }

    private void start() {
        String str =
                "[{'name':'jjj','age':43,'address':'ajjja'},"+
                        "{'name':'aaa','age':36,'address':'kaaak'},"+
                        "{'name':'bbb','age':25,'address':'nbbbn'}]";
        try {
            JSONObject a = new JSONObject(str);
            HttpRequester httpRequester = new HttpRequester(a, "http://");
            httpRequester.run();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
