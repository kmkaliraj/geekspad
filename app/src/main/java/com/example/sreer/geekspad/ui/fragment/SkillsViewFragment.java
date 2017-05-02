package com.example.sreer.geekspad.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sreer.geekspad.R;
import com.example.sreer.geekspad.model.Skill;
import com.example.sreer.geekspad.ui.adapter.SkillSetRecyclerAdapter;
import com.example.sreer.geekspad.utils.ItemClickSupport;
import java.util.ArrayList;
import java.util.List;

public class SkillsViewFragment extends Fragment implements ItemClickSupport.OnItemClickListener{

    private RecyclerView skillsRecyclerView;
    private SkillSetRecyclerAdapter  skillSetRecyclerAdapter;
    private List<Skill> skillList;
    private String skill_name = null;
    private int selectedItemPosition = -1;

    public SkillsViewFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skills_view, container, false);
        skillList = new ArrayList<>();
        skillsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_skills_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        skillsRecyclerView.setLayoutManager(mLayoutManager);
        skillSetRecyclerAdapter = new SkillSetRecyclerAdapter(skillList);
        skillsRecyclerView.setAdapter(skillSetRecyclerAdapter);
        ItemClickSupport.addTo(skillsRecyclerView).setOnItemClickListener(this);
        return view;

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.skills_menu, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.skills_add:
                showAddSkillsPopup(getView());
                break;
            case R.id.skills_remove:
                if(selectedItemPosition!=-1) {
                    skillSetRecyclerAdapter.remove(selectedItemPosition);
                    selectedItemPosition=-1;
                }
                break;
        }

        return true;
    }


    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        selectedItemPosition = position;
    }


    public void showAddSkillsPopup(View view){

       try{

           Display display = getActivity().getWindowManager().getDefaultDisplay();
           Point size = new Point();
           display.getSize(size);
           int width = size.x;
           int height = size.y;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout
        View popupView = inflater.inflate(R.layout.skills_popup,
                null);
        // create a 300px width and 470px height PopupWindow
        final PopupWindow popupWindow = new PopupWindow(popupView,width-40,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // display the popup in the center
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

          final Button doneButton = (Button)popupView.findViewById(R.id.add_skills_button);
           final TextView seekbarText = (TextView) popupView.findViewById(R.id.proficiency);
           final SeekBar seekBar = (SeekBar) popupView.findViewById(R.id.input_proficiency);

           doneButton.setOnClickListener(new Button.OnClickListener(){
               String skill_level = null;
               @Override
               public void onClick(View v) {
                   // TODO Auto-generated method stub

                   if(skill_name !=null && !skill_name.contains("Select")){
                       skill_level = "Proficiency Level: "+ seekbarText.getText().toString();
                       Skill skill = new Skill(skill_name,skill_level);
                       skillSetRecyclerAdapter.add(skill);
                       skillsRecyclerView.smoothScrollToPosition(skillSetRecyclerAdapter.getItemCount()-1);
                   }
                   popupWindow.dismiss();
               }});


           ImageButton closeButton = (ImageButton)popupView.findViewById(R.id.popup_close);

           // Set a click listener for the popup window close button
           closeButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   // Dismiss the popup window
                   popupWindow.dismiss();
               }
           });



           seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

               @Override
               public void onProgressChanged(SeekBar seekBar, int progress,
                                             boolean fromUser) {
                   // TODO Auto-generated method stub
                   seekbarText.setText(String.valueOf(progress)+"%");
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {
                   // TODO Auto-generated method stub
               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {
                   // TODO Auto-generated method stub
               }
           });


           // initialize skills in the spinner
           final Spinner mSkills = (Spinner)popupView.findViewById(R.id.input_skills);
           ArrayAdapter countryAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                   R.array.skills, R.layout.spinner_item);
           countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           mSkills.setAdapter(countryAdapter);

          mSkills.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  skill_name = parent.getAdapter().getItem(position).toString();
                  seekBar.setProgress(0);
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
          });

    } catch (Exception e) {
        e.printStackTrace();
        }
    }


}