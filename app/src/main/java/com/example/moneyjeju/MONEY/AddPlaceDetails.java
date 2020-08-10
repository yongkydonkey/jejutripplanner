package com.example.moneyjeju.MONEY;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moneyjeju.Login.LoginActivity;
import com.example.moneyjeju.Login.RegisterActivity;
import com.example.moneyjeju.Login.RegisterRequest;
import com.example.moneyjeju.MAP.StartActivity;
import com.example.moneyjeju.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class AddPlaceDetails extends AppCompatActivity {

    private static Button btn;
    private static Button b1;
    private static Button btnadd;
    final String COL_2 = "place_name";
    final String COL_3 = "date_go";
    final String COL_4 = "friend_no";
    final String table = "Trip_details";
    public DatePickerDialog datepick = null;
    SQLiteDatabase db1 = null;
    EditText e1, e2;
    Editable d1, d2 = null;
    TextView edtDob = null;
    TextView edtxt;
    EditText f_add;
    EditText num;
    String user_id;
    int check = 0;
    ArrayList<String> fname = new ArrayList<String>();
    int size = 0;
    public static final String URL_SAVE_NAME = "http://192.168.0.8/sqltomysql.php";
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_details);
        db = new DatabaseHelper(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn = (Button) findViewById(R.id.button_SUBMIT);
        edtxt = (TextView) findViewById(R.id.daye);
        btnadd = (Button) findViewById(R.id.add_btn);
        b1 = (Button) findViewById(R.id.daypickbut);
        f_add = (EditText) findViewById(R.id.friend_name);
        num = (EditText) findViewById(R.id.editno);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        user_id = intent.getStringExtra("userID");
        OnClickpickDate();
        OnClickButtonSubmit();
        setTitle("New Trip");
        OnClickButtonAddFriend();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddPlaceDetails.this, AddPlace.class));
        finish();
    }

    public void OnClickpickDate() {
        try {
            b1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    datepick = new DatePickerDialog(v.getContext(), (DatePickerDialog.OnDateSetListener) new DatePickHandler(), Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                    datepick.show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "불가능한 날짜", Toast.LENGTH_SHORT).show();
        }
    }

    public class DatePickHandler implements DatePickerDialog.OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int months = month + 1;
            if ((months < 10) && (day < 10))
                edtxt.setText(year + "-0" + (months) + "-0" + day);
            else if ((months < 10) && (day > 10))
                edtxt.setText(year + "-0" + (months) + "-" + day);
            else if ((months > 10) && (day < 10))
                edtxt.setText(year + "-" + (months) + "-0" + day);
            else
                edtxt.setText(year + "-" + (months) + "-" + day);
            datepick.hide();
        }
    }

    void OnClickButtonSubmit() {
        e1 = (EditText) findViewById(R.id.Textadd);
        e2 = (EditText) findViewById(R.id.editno);
        edtDob = (TextView) findViewById(R.id.daye);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db1 = openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
                        d1 = e1.getText();
                        d2 = e2.getText();
                        String dd = edtDob.getText().toString();
                        String s1 = d1.toString().toLowerCase();
                        String s2 = d2.toString();
                        s1 = s1.trim();
                        s2 = s2.trim();

                        try {
                            db1.execSQL("CREATE TABLE IF NOT EXISTS Trip_details (id INTEGER PRIMARY KEY AUTOINCREMENT ,place_name TEXT NOT NULL, date_go DATE NOT NULL," + " friend_no INTEGER NOT NULL DEFAULT 0);");
                            //check if any of the fields is not empty or friend no is not equal to zero
                            if (s1.matches("") || s2.matches("") || dd.matches("") || Integer.parseInt(s2) == 0)
                                throw new ArithmeticException("Inadequate details..\nEnter Again");
                            final ContentValues contentValues = new ContentValues();
                            contentValues.put(COL_2, d1.toString().toLowerCase());
                            contentValues.put(COL_2, d1.toString().toLowerCase());
                            contentValues.put(COL_3, dd);
                            contentValues.put(COL_4, Integer.parseInt(num.getText().toString()));
                            int x = Integer.parseInt(num.getText().toString());

                            try {
                                Cursor c = db1.rawQuery("SELECT * FROM Trip_details ORDER BY date_go DESC;", null);
                                if (c != null) {
                                    int i = 0;
                                    if (c.moveToFirst()) {
                                        do {
                                            String compare = c.getString(c.getColumnIndex("place_name"));
                                            if (compare.matches(e1.getText().toString().toLowerCase()))
                                                throw new ArithmeticException("HELLO");

                                        } while (c.moveToNext());
                                    }
                                }

                                //EXCEPTION
                                try {
                                    if (check == Integer.parseInt(num.getText().toString())) {
                                        long result = db1.insert(table, null, contentValues);


                                        if (result != -1) {
                                            Toast.makeText(getApplicationContext(), "여행 추가", Toast.LENGTH_SHORT).show();
                                            Intent lis = new Intent(getApplicationContext(), AddPlace.class);
                                            startActivity(lis);
                                            finish();
                                        } else
                                            throw new ArithmeticException("Inadequate details..\nEnter Again");

                                    } else {
                                        Toast.makeText(getApplicationContext(), "더 많은 친구 추가", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Inadequate details..\n" +
                                            "Enter Again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "여행 이름이 이미 등록됨", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Inadequate details..\n" +
                                    "Enter Again", Toast.LENGTH_LONG).show();
                        }
                    }

                }
        );
    }


    public void OnClickButtonAddFriend() {
        btnadd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String z = num.getText().toString().trim();
                            if (z.matches(""))
                                throw new ArithmeticException("hello");

                            int x = Integer.parseInt(num.getText().toString());

                            if (check < x) {
                                db1 = openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
                                String TABLE_NAME;
                                e1 = (EditText) findViewById(R.id.Textadd);
                                TABLE_NAME = "f" + e1.getText().toString().toLowerCase();
                                try {
                                    Cursor c = db1.rawQuery("SELECT * FROM Trip_details ORDER BY date_go DESC;", null);
                                    if (c != null) {
                                        int i = 0;
                                        if (c.moveToFirst()) {
                                            do {
                                                String compare = c.getString(c.getColumnIndex("place_name"));
                                                if (compare.matches(e1.getText().toString().toLowerCase()))
                                                    throw new ArithmeticException("HELLO");

                                            } while (c.moveToNext());
                                        }
                                    }
                                    db1.execSQL("create table if not exists " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,friend TEXT,amount INTEGER NOT NULL DEFAULT 0)");
                                    ContentValues contentValues = new ContentValues();
                                    String COL_2;
                                    COL_2 = "friend";
                                    //exception
                                    try {
                                        String y = f_add.getText().toString();
                                        y = y.toLowerCase();
                                        int flag1 = 0;
                                        if (y.matches(""))
                                            throw new ArithmeticException("Inadequate details..\nEnter Again");

                                        //check if friend name is alrealy included in the list
                                        for (int i = 0; i < fname.size(); i++) {
                                            if (y.matches(fname.get(i))) {
                                                Toast.makeText(getApplicationContext(), y + " 이미 포함되어 있음", Toast.LENGTH_SHORT).show();
                                                flag1 = 1;
                                                break;
                                            }
                                        }
                                        if (flag1 == 0) {
                                            contentValues.put(COL_2, y);
                                            fname.add(y);
                                            size++;
                                            long result = db1.insert(TABLE_NAME, null, contentValues);
                                            check++;
                                        }
                                        f_add.setText("");
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "이름을 입력하시오", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "이미 등록되어 있음", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "등록 인원 초과", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Inadequate details..\n" +
                                    "Enter Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Response.Listener<String> responseListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                Trip_detailsRequest registerRequest = new Trip_detailsRequest(user_id, COL_2, COL_3, COL_4, responseListener);
                RequestQueue queue = Volley.newRequestQueue(AddPlaceDetails.this);
                queue.add(registerRequest);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

    };
}
