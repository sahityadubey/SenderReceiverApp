package com.sahitya.sender.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sahitya.sender.R;
import com.sahitya.sender.SendersActivity;
import com.sahitya.sender.model.EmployeeDetail;
import com.sahitya.sender.model.EmployeeModel;
import com.sahitya.sender.network.NetworkHandler;
import com.sahitya.sender.network.PacketObj;
import com.sahitya.sender.network.ResponseListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class NewEmpFragment extends Fragment {
    View view;
    private EditText empName, empCompany, empExperience;
    private Button btnRegister;
    private CallBackListener callBackListener;
    NetworkHandler networkHandler;
    EmployeeModel employeeModelJson;
    String url = "https://EmpRegistration.azurewebsites.net/";

    public NewEmpFragment() {
    }

    public static NewEmpFragment newInstance() {
        NewEmpFragment fragment = new NewEmpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((SendersActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_emp, container, false);
        empName = view.findViewById(R.id.emp_input_name);
        empCompany = view.findViewById(R.id.emp_input_company);
        empExperience = view.findViewById(R.id.emp_input_exp);
        btnRegister = view.findViewById(R.id.btn_register);
        networkHandler = NetworkHandler.getInstance(getContext());
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(empName.getText().toString().equals("") &&
                        empCompany.getText().toString().equals("") &&
                        empExperience.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    RegisterEmployee();
                   //TriggerEmployeeData();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof CallBackListener)
            callBackListener = (CallBackListener) getActivity();
    }

    //Sending data to server
    private void TriggerEmployeeData()
    {
        networkHandler.PostReq(url, FormJsonData(employeeModelJson), new ResponseListener<PacketObj>() {
            @Override
            public void getResult(PacketObj object) {
                Toast.makeText(getContext(), "Data Saved Successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onErrorResponse(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //Forming data into jsonObject
    private JSONObject FormJsonData(EmployeeModel employeeModel)
    {
        Gson gson = new Gson();
        JSONObject req = new JSONObject();

        String jsonStr = gson.toJson(employeeModel);
        try {
            req.put("EmpData", jsonStr);
        } catch (Exception e) {
            return new JSONObject();
        }
        return req;
    }

    //Updating Employee Data
    private void RegisterEmployee() {
        Gson gson = new Gson();
        List<EmployeeDetail> employeeDetails = new ArrayList<EmployeeDetail>();
        EmployeeDetail empD = new EmployeeDetail(empName.getText().toString(),empCompany.getText().toString(),empExperience.getText().toString());
        employeeDetails.add(empD);
        employeeModelJson = new EmployeeModel(employeeDetails);
        String dataFromStorage = GetEmpFile(getString(R.string.file_name));
        //Saving and updating the time stamp
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatLocal.setTimeZone(TimeZone.getTimeZone("UTC"));

        if(dataFromStorage != "")
        {
            employeeModelJson = gson.fromJson(dataFromStorage, EmployeeModel.class);
        }
        else if(dataFromStorage == "") {
            UUID uuid = UUID.randomUUID();
            String tt = uuid.toString();
            employeeModelJson.setEmpDocId(tt);
        }
        employeeModelJson.setLastModifiedTimeUTC(dateFormatLocal.format(new Date()));
        employeeModelJson.setIsShouldSynced(true);
        employeeModelJson.getEmpDetails().add(empD);
        Gson json = new Gson();
        String jsonStr = json.toJson(employeeModelJson);

        SaveEmpFile(getString(R.string.file_name), jsonStr);

        FragmentManager fm= getActivity().getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        ft.commit();
        if(callBackListener != null)
            callBackListener.onCallBack();
    }

    //Retrieving into the storage
    private String GetEmpFile(String url) {
        String stringGotFromStorage = "";
        try
        {
            FileInputStream fileIn= getActivity().openFileInput(url);
            InputStreamReader outputStreamWriter = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[100];
            int charRead;

            while ((charRead= outputStreamWriter.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                stringGotFromStorage +=readstring;
            }
            outputStreamWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stringGotFromStorage;
    }

    //Saving into the storage
    private void SaveEmpFile(String url, String textToSave) {
        try {
            FileOutputStream fileout= getActivity().openFileOutput(url, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textToSave);
            outputWriter.close();
            Toast.makeText(getContext(), "Employer Successfully Registered!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
