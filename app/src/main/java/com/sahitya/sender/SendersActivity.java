package com.sahitya.sender;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.sahitya.sender.adapters.ListAdapter;
import com.sahitya.sender.fragments.CallBackListener;
import com.sahitya.sender.fragments.NewEmpFragment;
import com.sahitya.sender.model.EmployeeDetail;
import com.sahitya.sender.model.EmployeeModel;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SendersActivity extends AppCompatActivity implements CallBackListener {
    private List<EmployeeDetail> empList = new ArrayList<>();
    private ListAdapter mAdapter;
    private FloatingActionButton floatBtn;
    private List<EmployeeDetail> employeeDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        floatBtn = findViewById(R.id.fab);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        setSupportActionBar(toolbar);
        mAdapter = new ListAdapter(empList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton floatingActionButton = this.getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
        //Invoking data from storage
        InvokeDataFromStorge();
        //Updating UI
        PrepareEmpData(employeeDetail);
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatBtn;
    }

    //Adding new employee, adding fragment
    public void AddEmployee(View view) {
        Fragment fragment = NewEmpFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.new_emp_fragment, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    //Fetching data from internal storage
    private void InvokeDataFromStorge() {
        Gson gson = new Gson();
        String stringGotFromStorage = "";
        try {
            int charRead;
            char[] inputBuffer = new char[100];
            FileInputStream fileIn = openFileInput(getString(R.string.file_name));
            InputStreamReader outputStreamWriter = new InputStreamReader(fileIn);

            while ((charRead = outputStreamWriter.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                stringGotFromStorage += readString;
            }
            outputStreamWriter.close();
            //Converting string to json
            EmployeeModel employeeModelJson = gson.fromJson(stringGotFromStorage, EmployeeModel.class);
            employeeDetail = employeeModelJson.getEmpDetails();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        FloatingActionButton floatingActionButton = this.getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
    }

    //Updating UI LIST with Employee data
    private void PrepareEmpData(List<EmployeeDetail> employeeDetail) {
        if (employeeDetail != null) {
            empList.clear();
            for (EmployeeDetail item : employeeDetail) {
                //Adding items to the list
                empList.add(item);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCallBack() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        FloatingActionButton floatingActionButton = this.getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
        //Invoking data from storage
        InvokeDataFromStorge();
        //Updating UI
        PrepareEmpData(employeeDetail);
    }
}
