package com.example.cooorem.tec2com;

import android.app.Activity;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private Spinner spR,spS,spCon,spTo;
    private List<String > rList=new ArrayList<String>(),sList=new ArrayList<String>(),conList=new ArrayList<String>(),toList=new ArrayList<String>();
    private ArrayAdapter<String > rAdapter,sAdapter,conAdapter,toAdapter;
    private CheckBox cbBreak,cbPlus;
    private Button btnSub;
    private boolean isSub=false;
    private TextView tvAns;
    private ControlNum controlNum=new ControlNum();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSub= (Button) findViewById(R.id.btnSub);
        cbBreak= (CheckBox) findViewById(R.id.cbBreak);
        cbPlus= (CheckBox) findViewById(R.id.cbPlus);
        tvAns= (TextView) findViewById(R.id.tvAns);
        spR= (Spinner) findViewById(R.id.spinnerR);
        spS= (Spinner) findViewById(R.id.spinnerS);
        spCon= (Spinner) findViewById(R.id.spinnerCao);
        spTo= (Spinner) findViewById(R.id.spinnerTo);
        rList.add("0");
        rList.add("SR");
        rList.add("MEM");
        rList.add("PC");
        conList.add("+");
        conList.add("-");
        conList.add("and");
        conList.add("or");
        conList.add("re-");
        sList.add("Q");
        sList.add("DR");
        sList.add("PC");
        toList.add("AR");
        toList.add("Q");
        toList.add("MEM");
        initR();
        initCon();
        initS();
        initTo();
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Button pressed");
                tvAns.setText(controlNum.toString());
            }
        });
        cbPlus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && isSub) {
                    Toast.makeText(MainActivity.this, "减法时为减一", Toast.LENGTH_SHORT).show();
                    controlNum.plus1(true);
                    return;
                } else if (!controlNum.plus1(b)) Log.e("MainActivityPlus1", "Error");
            }
        });
        cbBreak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                controlNum.breakOff(b);
            }
        });
    }

    private void initCon() {
        conAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, conList);
        conAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCon.setAdapter(conAdapter);
        spCon.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("MainActivity", "con select " + i);
                if(i==1){
                    cbPlus.setChecked(false);
                    isSub=true;
                }
                else isSub=false;
                controlNum.setCao(i);
                adapterView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void initR(){
        rAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, rList);
        rAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spR.setAdapter(rAdapter);
        spR.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("MainActivity","r select "+i);
                setR(i);
                adapterView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    void initS(){
        sAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sList);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spS.setAdapter(sAdapter);
        spS.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("MainActivity", "s select " + i);
                if(!setS(sAdapter.getItem(i))) {
                    Toast.makeText(MainActivity.this, "set S failed", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity","init S failed");
                }
                adapterView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    void initTo(){
        toAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, toList);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTo.setAdapter(toAdapter);
        spTo.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("MainActivity","to select "+i);
                if(!setTo(toAdapter.getItem(i))) {
                    Toast.makeText(MainActivity.this, "set To failed", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity","init To failed");
                    return;
                }
                tvAns.setText(controlNum.toString());
                adapterView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private boolean setS(String m) {
        int mode=0;
        switch (m.charAt(0)){
            case 'Q':
                mode=0;
                break;
            case 'D':
                mode=1;
                break;
            case 'P':
                mode=2;
                break;
            case 'S':
                mode=3;
                break;
            case '0':
                mode=4;
                break;
        }
        if(mode==1||mode==0||mode==4){
            toList.remove("DR");
            toList.add("DR");
        }
        if(mode==2||mode==0||mode==4){
            toList.remove("PC");
            toList.add("PC");
        }
        return controlNum.setS(mode);
    }

    private void setR(int mode){
        toList.remove("MEM");
        sList.clear();
        sList.add("Q");
        sList.add("DR");
        sList.add("PC");
        if(mode==0){
            sList.add("SR");
        }else if(mode==2){
            sList.remove("DR");
            sList.remove("PC");
            sList.add("SR");
            sList.add("0");
        }
        if(mode!=2)toList.add("MEM");
        controlNum.setR(mode);
    }
    private boolean setTo(String s){
        switch (s.charAt(0)){
            case 'A':
                return controlNum.setTo(0);
            case 'P':
                return controlNum.setTo(1);
            case 'M':
                return controlNum.setTo(2);
            case 'D':
                return controlNum.setTo(3);
            case 'Q':
                return controlNum.setTo(4);
        }
        return false;
    }
}
