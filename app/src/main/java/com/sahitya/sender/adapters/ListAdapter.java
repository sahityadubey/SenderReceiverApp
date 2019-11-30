package com.sahitya.sender.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sahitya.sender.R;
import com.sahitya.sender.model.EmployeeDetail;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<EmployeeDetail> empList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView empName,empCompany,empExp;
        public MyViewHolder(View view)
        {
            super(view);
            empName = (TextView) view.findViewById(R.id.emp_name);
            empCompany = (TextView) view.findViewById(R.id.emp_company);
            empExp = (TextView) view.findViewById(R.id.emp_exp);
        }
    }

    public ListAdapter(List<EmployeeDetail> empList) {
        this.empList = empList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EmployeeDetail employeeDetail = empList.get(position);
        holder.empName.setText(employeeDetail.getEmpName());
        holder.empCompany.setText(employeeDetail.getEmpComapany());
        holder.empExp.setText(employeeDetail.getEmpExperience());
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }
}
