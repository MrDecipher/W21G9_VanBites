package com.example.vanbites.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vanbites.CartActivity;
import com.example.vanbites.FoodItemActivity;
import com.example.vanbites.Interface.ItemClickListener;
import com.example.vanbites.Model.Menu_model;
import com.example.vanbites.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MenuItems extends AppCompatActivity {

    DatabaseReference menu;

    FirebaseRecyclerOptions<Menu_model> options;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recyclerViewMenu;
    String cat;
    Button btnGoBack4;
    FloatingActionButton cartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        // make activity fullscreen
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );


        cat = getIntent().getExtras().getString("CAT", "");


        //Floating cart button
        cartButton = findViewById(R.id.floatingActionButton);
        cartButton.setColorFilter(Color.WHITE);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuItems.this, CartActivity.class));
            }
        });

        // Go back button
        btnGoBack4 = findViewById(R.id.btnGoBack4);
        btnGoBack4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Firebase initiation
        menu = FirebaseDatabase.getInstance().getReference().child(cat);
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        loadMenu();
    }


    // https://firebaseopensource.com/projects/firebase/firebaseui-android/database/readme/
    // FirebaseRecyclerAdapter — binds a Query to a RecyclerView and responds to all real-time events
    // included items being added, removed, moved, or changed.
    // Best used with small result sets since all results are loaded at once.


    private void loadMenu() {
        options = new FirebaseRecyclerOptions.Builder<Menu_model>().setQuery(menu, Menu_model.class).build();
        adapter = new FirebaseRecyclerAdapter<Menu_model, MenuViewHolder>(options) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the MenuViewHolder, in this case we are using a custom
                // layout called R.layout.menu_item for each item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false);
                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Menu_model model) {
                // Bind the Category object to the MenuViewHolder
                holder.txtViewMenuItemName.setText(model.getName());

                // https://square.github.io/picasso/
                Picasso.get().load(model.getImage()).into(holder.imgViewMenuImage);

                // Creating bundle with data of item being clicked and passing it to the Food Item Activity
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Bundle bundle = new Bundle();
                        bundle.putString("CAT", cat);
                        bundle.putString("ITEM_ID", model.getId());
                        bundle.putString("ITEM_IMG", model.getImage());
                        bundle.putString("ITEM_NAME", model.getName());
                        Intent item = new Intent(MenuItems.this, FoodItemActivity.class);
                        item.putExtras(bundle);
                        startActivity(item);
                    }
                });
            }
        };

        adapter.startListening();
        recyclerViewMenu.setAdapter(adapter);
    }
}