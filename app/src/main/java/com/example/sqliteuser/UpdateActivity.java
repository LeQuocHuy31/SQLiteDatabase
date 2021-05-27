package com.example.sqliteuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class UpdateActivity extends AppCompatActivity {
    private EditText edtUserName;
    private EditText edtAddress;
    private EditText edtYear;
    private Button btnUpdateUser;
    private User mUser;
    private  UserDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        edtUserName=findViewById(R.id.edt_username);
        edtAddress=findViewById(R.id.edt_address);
        edtYear=findViewById(R.id.edt_year);
        btnUpdateUser=findViewById(R.id.btn_update_user);
        Bundle bundle = getIntent().getExtras();
        mUser= (User) bundle.get("object_user");
        database = new UserDatabase(this);
        database.open();

        if(mUser!=null){
            edtUserName.setText(mUser.getName());
            edtAddress.setText(mUser.getAddress());
            edtYear.setText(mUser.getYear());
            btnUpdateUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUser();
                }
            });
        }
    }

    private void updateUser() {
        String strUsername=edtUserName.getText().toString().trim();
        String strAddress =edtAddress.getText().toString().trim();
        String strYear=edtYear.getText().toString().trim();
        //Kiểm tra dữ liệu nhâp vào có rổng hay không
        if(TextUtils.isEmpty(strUsername)|| TextUtils.isEmpty(strAddress)||TextUtils.isEmpty(strYear)){
            return;
        }
        mUser.setName(strUsername);
        mUser.setAddress(strAddress);
        mUser.setYear(strYear);
        database.update(mUser);
        Toast.makeText(this,"Update user successfull",Toast.LENGTH_LONG).show();
        Intent intentResult= new Intent();
        setResult(Activity.RESULT_OK,intentResult);
        finish();
    }

}