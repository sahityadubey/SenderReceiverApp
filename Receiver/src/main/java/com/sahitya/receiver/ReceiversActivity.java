package com.sahitya.receiver;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sahitya.receiver.adapters.ListAdapter;
import com.sahitya.receiver.model.EmployeeDetail;
import com.sahitya.receiver.model.EmployeeModel;
import com.sahitya.receiver.network.NetworkHandler;
import com.sahitya.receiver.network.PacketObj;
import com.sahitya.receiver.network.ResponseListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReceiversActivity extends AppCompatActivity {
    private List<EmployeeDetail> empList = new ArrayList<>();
    private ListAdapter mAdapter;
    private String dataReceivedFromServer;
    NetworkHandler networkHandler;
    String url = "https://EmpRegistration.azurewebsites.net/";
    private List<EmployeeDetail> employeeDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkHandler = NetworkHandler.getInstance(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ListAdapter(empList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Invoking data from storage
        //InvokeDataFromStorge();

        //Updating UI
        PrepareEmpData(employeeDetail);
    }

    //Need to recplace this method with "SyncFromServer" after backend deployed
    public void ToastMessageForSync(View view) {
        Toast.makeText(this, "Syncing From Server!", Toast.LENGTH_LONG).show();
    }

    //Get data from server
    public void SyncFromServer(View view) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()) {
                NetworkHandler.getInstance(this).GetReq(url, new ResponseListener<PacketObj>() {
                    @Override
                    public void getResult(PacketObj result) {
                        dataReceivedFromServer = result.getPacket().body.toString();
                        //Saving data to storage
                        SaveEmpFile(getString(R.string.file_name), dataReceivedFromServer);
                        //Updating UI
                        PrepareEmpData(employeeDetail);
                    }
                    @Override
                    public void onErrorResponse(Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: Something went wrong!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //Parsing response
    private EmployeeModel ParseData(String datafromServer){
        Gson gson = new Gson();
        EmployeeModel employeeModel;
        employeeModel = gson.fromJson(datafromServer, EmployeeModel.class);
        return employeeModel;
    }

    //Fetching data from internal storage
    private void InvokeDataFromStorge() {
        Gson gson = new Gson();
        String stringGotFromStorage = "";
        try {
            FileInputStream fileIn = openFileInput(getString(R.string.file_name));
            InputStreamReader outputStreamWriter = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[100];
            int charRead;

            while ((charRead = outputStreamWriter.read(inputBuffer)) > 0) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                stringGotFromStorage += readstring;
            }
            outputStreamWriter.close();
            EmployeeModel employeeModelJson = gson.fromJson(stringGotFromStorage, EmployeeModel.class);
            employeeDetail = employeeModelJson.getEmpDetails();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Hardcoded value to update the UI
    private List<EmployeeDetail> GetHardCodedValue()
    {
        EmployeeDetail emp1 = new EmployeeDetail("Sahitya", "Company", "2");
        EmployeeDetail emp2 = new EmployeeDetail("Shivansh", "infrrd", "3");
        EmployeeDetail emp3 = new EmployeeDetail("Shivam", "infrrd", "3");
        EmployeeDetail emp4 = new EmployeeDetail("Anubhav", "Ehidna", "2");
        List<EmployeeDetail> listEmp = Arrays.asList(emp1,emp2,emp3,emp4);
        return listEmp;
    }

    //Updating UI LIST with Employee data
    private void PrepareEmpData(List<EmployeeDetail> employeeDetail) {
        /*Below line is hardcoded due to backed is not deployed yet*/
        employeeDetail = GetHardCodedValue();
        if(employeeDetail != null) {
            empList.clear();
            for (EmployeeDetail item : employeeDetail) {
                //Adding items to the list
                empList.add(item);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    //Saving server data
    private void SaveEmpFile(String url, String textToSave) {
        try {
            FileOutputStream fileout= openFileOutput(url, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textToSave);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
