/**
 * Copyright (C) futuretek AG 2016
 * All Rights Reserved
 *
 * @author Artan Veliju
 */
package survey.android.futuretek.ch.ft_survey;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SkillsActivity extends BaseActivity {
    private Button btn_add;
    private ListView listview;
    public List<String> _productlist = new ArrayList<String>();
    private ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        listview = (ListView) findViewById(R.id.listView);
        View mainTextView = findViewById(R.id.textLayout);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        btn_add = (Button) findViewById(R.id.addBtn);
        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openInputSkillDialog("");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ViewGroup)findViewById(R.id.textLayout)).removeAllViews();
        List<String> textArray = new ArrayList<>(1);
        textArray.add("Please add a developer skill");
        animateText(textArray);
        _productlist.clear();
        _productlist = getDatabase().getAllSkills();
        adapter = new ListAdapter(this);
        listview.setAdapter(adapter);
    }

    private class ListAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ViewHolder viewHolder;

        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return _productlist.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_row, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView1);
                viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewGroup row = ((ViewGroup)v.getParent());
                        String skill = ((TextView)row.findViewById(R.id.textView1)).getText().toString();
                        openInputSkillDialog(skill);
                    }
                });

                viewHolder.delBtn = (Button) convertView.findViewById(R.id.deleteBtn);
                viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViewGroup row = ((ViewGroup)v.getParent());
                        String id = ((TextView)row.findViewById(R.id.textView1)).getText().toString();
                        getDatabase().deleteSkill(id);
                        _productlist.remove(id);
                        adapter.notifyDataSetChanged();
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(_productlist.get(position));
            return convertView;
        }
    }

    private class ViewHolder {
        TextView textView;
        Button delBtn;

    }

    protected void openInputSkillDialog(final String skill) {
        final Dialog dlg = new Dialog(this);
        dlg.setContentView(R.layout.skillsdialog);

        try{
            final EditText skillsInput = ((EditText) dlg.findViewById(R.id.skillsInput));
            skillsInput.setText(skill);

            ((Button) dlg.findViewById(R.id.add)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if(skill != null && !skill.isEmpty())
                    {
                        updateSkill(skill, skillsInput.getText().toString());
                    }
                    else
                    {
                        insertSkill(skillsInput.getText().toString());
                    }

                    dlg.dismiss();
                }
            });
            dlg.setOnCancelListener(
                    new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            dlg.dismiss();
                        }
                    });
            dlg.show();
        }catch (Exception e){
        }
    }

    private void updateSkill(String key, String value){
        try {
            getDatabase().updateSkill(key, value);
            _productlist = getDatabase().getAllSkills();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertSkill(String skill){
        try {
            getDatabase().putSkill(skill);
            _productlist = getDatabase().getAllSkills();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}