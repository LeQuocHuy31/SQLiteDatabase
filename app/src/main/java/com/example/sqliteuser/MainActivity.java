package com.example.sqliteuser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private EditText edtUsername, edtAddress, edtSearch;
    private Button btnAddUser;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    private TextView tvDeleteAll;
    private TextView edtYear;

    private  UserDatabase datasource;
    List<User> mListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        datasource = new UserDatabase(this);
        datasource.open();
        userAdapter = new UserAdapter(new UserAdapter.IClickItemUser() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
            }
        });
        mListUser = new ArrayList<>();
        userAdapter.setData(mListUser);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(userAdapter);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUser();
            }
        });

        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDeleteAllUser();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId ==  EditorInfo.IME_ACTION_SEARCH){
                    handleSearchUser();
                }
                return false;
            }
        });
        loadData();
    }


    private void initUi(){
        edtUsername= (EditText)findViewById(R.id.edt_username);
        edtAddress= (EditText) findViewById(R.id.edt_address);
        edtYear= (EditText) findViewById(R.id.edt_year);
        btnAddUser=findViewById(R.id.btn_add_user);
        rcvUser= findViewById(R.id.rcv_user);
        tvDeleteAll=findViewById(R.id.tv_deleteALL);
        edtSearch=findViewById(R.id.edt_search);
    }

    private void addUser() {
        String strUsername=edtUsername.getText().toString().trim();
        String strAddress =edtAddress.getText().toString().trim();
        String strYear =edtYear.getText().toString().trim();
        //Kiểm tra dữ liệu nhâp vào có rổng hay không
        if(TextUtils.isEmpty(strUsername)|| TextUtils.isEmpty(strAddress)|| TextUtils.isEmpty(strYear)){
            return;
        }
        User user = datasource.createUser(strUsername,strAddress,strYear);
        Toast.makeText(this,"Add user successful",Toast.LENGTH_SHORT).show();
        edtAddress.setText("");
        edtUsername.setText("");
        edtYear.setText("");
        hideSoftKeyboard();
        loadData();
    }

    public  void hideSoftKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void loadData(){
        mListUser = datasource.getListUser();
        userAdapter.setData(mListUser);
    }



    private void clickUpdateUser(User user){
        Intent intent =new Intent(this,UpdateActivity.class);
         Bundle bundle= new Bundle();
         bundle.putSerializable("object_user",user);
         intent.putExtras(bundle);
          // startActivity(intent);
         startActivityForResult(intent,MY_REQUEST_CODE);
    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(requestCode==MY_REQUEST_CODE && resultCode==Activity.RESULT_OK){
             loadData();
         }
     }

    private  void clickDeleteUser(User user){
        new AlertDialog.Builder(this)
                .setTitle("Comfirm delete user")
                .setMessage("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datasource.delete(user);
                        Toast.makeText(MainActivity.this,"Delete user successfull",Toast.LENGTH_LONG).show();
                        loadData();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    private void clickDeleteAllUser(){
         new AlertDialog.Builder(this)
                 .setTitle("Comfirm delete all user")
                 .setMessage("Are you sure")
                 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         datasource.deleteAllUser();
                         Toast.makeText(MainActivity.this,"Delete all user successfull",Toast.LENGTH_LONG).show();
                         loadData();
                     }
                 })
                 .setNegativeButton("No",null)
                 .show();

    }

    private  void handleSearchUser(){
        String strKey= edtSearch.getText().toString().trim();
        mListUser= new ArrayList<>();
        mListUser=datasource.searchListUser(strKey);
        userAdapter.setData(mListUser);
        hideSoftKeyboard();
    }

}