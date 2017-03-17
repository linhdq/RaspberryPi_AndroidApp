package vn.com.ldz.raspberrypi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //view
    private ImageView btnControlLed1;
    private ImageView btnControlLed2;

    //
    private boolean isLed1On;
    private boolean isLed2On;
    private int statusLed1;
    private int statusLed2;

    //
    private DatabaseReference fbControlLed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        init();
        addListener();
    }

    private void init() {
        //view
        btnControlLed1 = (ImageView) findViewById(R.id.btn_control_led_01);
        btnControlLed2 = (ImageView) findViewById(R.id.btn_control_led_02);
        //
        isLed1On = isLed2On = false;
        //
        fbControlLed = FirebaseDatabase.getInstance().getReference("pi");
        //
        statusLed1 = statusLed2 = 0;
    }

    private void addListener() {
        btnControlLed1.setOnClickListener(this);
        btnControlLed2.setOnClickListener(this);
        //
        fbControlLed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    statusLed1 = Integer.parseInt(dataSnapshot.child("led1").getValue().toString());
                    statusLed2 = Integer.parseInt(dataSnapshot.child("led2").getValue().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (statusLed1 == 1) {
                    btnControlLed1.setImageResource(R.drawable.btn_switch_on);
                } else {
                    btnControlLed1.setImageResource(R.drawable.btn_switch_off);
                }

                if (statusLed2 == 1) {
                    btnControlLed2.setImageResource(R.drawable.btn_switch_on);
                } else {
                    btnControlLed2.setImageResource(R.drawable.btn_switch_off);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_control_led_01:
                Map<String, Object> map1 = new HashMap<>();
                if (isLed1On) {
                    btnControlLed1.setImageResource(R.drawable.btn_switch_off);
                    isLed1On = false;
                    map1.put("led1", "0");
                } else {
                    btnControlLed1.setImageResource(R.drawable.btn_switch_on);
                    isLed1On = true;
                    map1.put("led1", "1");
                }
                fbControlLed.updateChildren(map1);
                break;
            case R.id.btn_control_led_02:
                Map<String, Object> map2 = new HashMap<>();
                if (isLed2On) {
                    btnControlLed2.setImageResource(R.drawable.btn_switch_off);
                    isLed2On = false;
                    map2.put("led2", "0");
                } else {
                    btnControlLed2.setImageResource(R.drawable.btn_switch_on);
                    isLed2On = true;
                    map2.put("led2", "1");
                }
                fbControlLed.updateChildren(map2);
                break;
            default:
                break;
        }
    }
}
