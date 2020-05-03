package com.example.weddingplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weddingplanner.Models.Checklist;
import com.example.weddingplanner.Models.Products;
import com.example.weddingplanner.ViewHolder.ChecklistViewHolder;
import com.example.weddingplanner.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;


public class ChecklistFragment extends Fragment {
    private ImageButton addtoList;
    private EditText string_toadd;
    private String s;
    private RecyclerView RecyclerViewChecklist_pending;
    private DatabaseReference ChecklistRef_pending;

    private RecyclerView RecyclerViewChecklist_completed;
    private DatabaseReference ChecklistRef_completed;

    private String email_user;
    private boolean status_current;
    private Activity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_checklist, container, false );
        addtoList = (ImageButton) view.findViewById( R.id.add_checkactivity );
        string_toadd = (EditText) view.findViewById( R.id.string_toadd );
        RecyclerViewChecklist_pending = (RecyclerView)view.findViewById(R.id.recycler_menu_checklist);
        RecyclerViewChecklist_completed = (RecyclerView)view.findViewById(R.id.recycler_menu_checklist_completed);

        email_user = LoginActivity.currentuser.getEmail();
        ChecklistRef_pending = FirebaseDatabase.getInstance().getReference().child("checklist").child(email_user).child("pending");
        ChecklistRef_completed = FirebaseDatabase.getInstance().getReference().child("checklist").child(email_user).child("completed");


        RecyclerViewChecklist_pending.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerViewChecklist_pending.setLayoutManager(layoutManager);

        RecyclerViewChecklist_completed.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager_completed = new LinearLayoutManager(getActivity());
        RecyclerViewChecklist_completed.setLayoutManager(layoutManager_completed);

        addtoList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = string_toadd.getText().toString();
                if(s.isEmpty())
                {
                    Toast.makeText( getActivity(), "Please enter something to add to your checklist", Toast.LENGTH_LONG ).show();
                }
                else
                {
                    add_to_list(s);
                }
            }
        } );


        final FirebaseRecyclerOptions<Checklist> options =
                new FirebaseRecyclerOptions.Builder<Checklist>()
                        .setQuery(ChecklistRef_pending, Checklist.class)
                        .build();


        final FirebaseRecyclerAdapter<Checklist, ChecklistViewHolder> adapter =
                new FirebaseRecyclerAdapter<Checklist, ChecklistViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChecklistViewHolder holder, int position, @NonNull final Checklist model)
                    {

                        holder.description.setText(model.getTask());
                        status_current = holder.stat.isChecked();
                        holder.stat.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                status_current = isChecked;
                                ChecklistRef_pending.addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot data: dataSnapshot.getChildren())
                                        {
                                            final String keys = data.getKey();
                                            ChecklistRef_pending.child(keys).addListenerForSingleValueEvent( new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Checklist cl = dataSnapshot.getValue(Checklist.class);
                                                    if(cl.getTask().equals(model.getTask()))
                                                    {
                                                        ChecklistRef_pending.child(keys).removeValue();
                                                        Toast.makeText(getActivity()," Task completed! Congratulations.", Toast.LENGTH_LONG).show();
                                                        HashMap<String, Object> checkMap = new HashMap<>();
                                                        checkMap.put("task", model.getTask());//According to class Checklist
                                                        checkMap.put("status", "1");//According to class Checklist
                                                        ChecklistRef_pending.getParent().child("completed").push().setValue(checkMap);
//                                                        getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new ChecklistFragment()).commit();
                                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                        intent.putExtra("fragment", "checklist");
                                                        startActivity(intent);
                                                    }
//                                                    getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new ChecklistFragment()).commit();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            } );
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }
                        } );
                        /*holder.stat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {

                                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });*/
                    }

                    @NonNull
                    @Override
                    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_items_layout, parent, false);
                        ChecklistViewHolder holder = new ChecklistViewHolder(view);
                        return holder;
                    }
                };
        RecyclerViewChecklist_pending.setAdapter(adapter);
        adapter.startListening();



        final FirebaseRecyclerOptions<Checklist> options_completed =
                new FirebaseRecyclerOptions.Builder<Checklist>()
                        .setQuery(ChecklistRef_completed, Checklist.class)
                        .build();
        final FirebaseRecyclerAdapter<Checklist, ChecklistViewHolder> adapter_completed =
                new FirebaseRecyclerAdapter<Checklist, ChecklistViewHolder>(options_completed) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChecklistViewHolder holder, int position, @NonNull final Checklist model_completed)
                    {

                        holder.description.setText(model_completed.getTask());
                        holder.stat.setChecked(true);
                        holder.stat.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                ChecklistRef_completed.addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot data: dataSnapshot.getChildren())
                                        {
                                            final String keys = data.getKey();
                                            ChecklistRef_completed.child(keys).addListenerForSingleValueEvent( new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Checklist cl = dataSnapshot.getValue(Checklist.class);
                                                    if(cl.getTask().equals(model_completed.getTask()))
                                                    {
                                                        ChecklistRef_completed.child(keys).removeValue();
                                                        HashMap<String, Object> checkMap = new HashMap<>();
                                                        checkMap.put("task", model_completed.getTask());//According to class Checklist
                                                        checkMap.put("status", "0");//According to class Checklist
                                                        ChecklistRef_completed.getParent().child("pending").push().setValue(checkMap);
                                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                        intent.putExtra("fragment", "checklist");
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            } );
                                        }
//                                        getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new ChecklistFragment()).commit();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }
                        } );
                    }

                    @NonNull
                    @Override
                    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_items_layout, parent, false);
                        ChecklistViewHolder holder = new ChecklistViewHolder(view);
                        return holder;
                    }
                };

        RecyclerViewChecklist_completed.setAdapter(adapter_completed);
        adapter_completed.startListening();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach( context );
        activity = getActivity();

    }

    private void add_to_list(String s) {
        HashMap<String, Object> checkMap = new HashMap<>();
        checkMap.put("task", s);//According to class Checklist
        checkMap.put("status", "0");//According to class Checklist
        ChecklistRef_pending.push().setValue(checkMap);
//        getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new ChecklistFragment()).commit();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("fragment", "checklist");
        startActivity(intent);
    }
}
