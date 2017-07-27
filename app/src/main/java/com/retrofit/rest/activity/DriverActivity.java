package com.retrofit.rest.activity;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofit.rest.R;
import com.retrofit.rest.adapter.DriverAdapter;
import com.retrofit.rest.adapter.listener.RecyclerItemListener;
import com.retrofit.rest.data.model.Driver;
import com.retrofit.rest.data.remote.ApiFactory;
import com.retrofit.rest.data.remote.DriverService;
import com.retrofit.rest.database.table.DriversTable;
import com.retrofit.rest.loader.DriversLoader;
import com.retrofit.rest.util.Util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;

/**
 * Created by TechnoA on 24.07.2017.
 */

public class DriverActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerViewDrivers;
    private EditText etFirstName;
    private EditText etLastName;

    private ArrayList<Driver> drivers = new ArrayList<>();
    private Paint p = new Paint();

    private AlertDialog.Builder alertDialog;
    private DriverAdapter driverAdapter;

    private int edit_position;
    private boolean add = false;

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new DriversLoader(this);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        if (id == 1) {
            fillDriversFromDB(cursor);
            if (drivers.size() != 0) {
                updateRecyclerView();
            } else {
                Toast.makeText(this, "No Driver Records", Toast.LENGTH_LONG).show();
            }
        }
        getLoaderManager().destroyLoader(id);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_list_fragment);

        initViews();
        initDialog();

        //sync DB with Server
        fillDriversFromServer();
    }

    private void initViews() {

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerViewDrivers = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerViewDrivers.setLayoutManager(mLayoutManager);
        recyclerViewDrivers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDrivers.setHasFixedSize(true);
        recyclerViewDrivers.addOnItemTouchListener(new RecyclerItemListener(this, recyclerViewDrivers,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Util.logInfo("onClickItem");
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {
                        Util.logInfo("onLongClickItem");
                    }

                }));
        driverAdapter = new DriverAdapter(drivers, this);
        driverAdapter.notifyDataSetChanged();
        initSwipe();

    }

    private void initSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    Driver driver = drivers.get(position);
                    final long id = driver.getId();
                    DriverService service = ApiFactory.getDriverService();
                    Call<Void> call = service.deleteDriver(id);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                DriversTable.deleteById(getApplicationContext(), id);
                                Cursor cursor = getContentResolver().query(DriversTable.URI, null, null, null, null);
                                fillDriversFromDB(cursor);
                                updateRecyclerView();
                                Toast.makeText(getApplicationContext(), "Driver is deleted", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    removeView();
                    edit_position = position;
                    alertDialog.setTitle("Edit Driver");
                    etFirstName.setText(drivers.get(position).getFirstName());
                    etLastName.setText(drivers.get(position).getLastName());
                    alertDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewDrivers);
    }

    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.driver_dialog_layout, null);
        alertDialog.setView(view);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                updateRecyclerView();

                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add) {

                    add = false;
                    Driver driver = new Driver();
                    driver.setFirstName(etFirstName.getText().toString());
                    driver.setLastName(etLastName.getText().toString());

                    postToServer(driver);
                    updateRecyclerView();

                    dialog.dismiss();

                } else {

                    Driver driver = drivers.get(edit_position);
                    driver.setFirstName(etFirstName.getText().toString());
                    driver.setLastName(etLastName.getText().toString());
                    drivers.set(edit_position, driver);
                    putToServer(driver);
                    driverAdapter.notifyDataSetChanged();

                    dialog.dismiss();

                }

            }
        });
        etFirstName = (EditText) view.findViewById(R.id.et_f_name);
        etLastName = (EditText) view.findViewById(R.id.et_l_name);
    }

    private void postToServer(Driver driver) {
        DriverService service = ApiFactory.getDriverService();
        Call<Driver> call = service.createDriver(driver);
        call.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                Driver addedDriver = response.body();
                DriversTable.save(getApplicationContext(), addedDriver);
                drivers.add(addedDriver);
                driverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {

            }
        });

    }

    private void putToServer(Driver driver) {
        DriverService service = ApiFactory.getDriverService();
        Call<Driver> call = service.updateDriver(driver, driver.getId());
        call.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (response.isSuccessful()) {
                    Driver addedDriver = response.body();
                    DriversTable.updateDriver(getApplicationContext(), addedDriver);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                removeView();
                add = true;
                alertDialog.setTitle("Add Driver");
                etFirstName.setText("");
                etLastName.setText("");
                alertDialog.show();

                break;
        }
    }

    private void fillDriversFromServer() {
        getLoaderManager().initLoader(1, Bundle.EMPTY, this);
    }


    private void fillDriversFromDB(Cursor cursor) {
        drivers = DriversTable.listFromCursor(cursor);
    }

    private void updateRecyclerView() {
        driverAdapter = new DriverAdapter(drivers, this);
        recyclerViewDrivers.setAdapter(driverAdapter);
        recyclerViewDrivers.setVisibility(View.VISIBLE);
        driverAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        fillDriversFromServer();
    }
}
