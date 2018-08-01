package com.example.user.sessionmanager;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.sessionmanager.database.MembersContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.toString();
    private static final int MEMBERS_LOADER_ID = 87;

    private static final String KEY_ORDER = "order";
    private static final String VALUE_ORDER_BY_NAME = MembersContract.MembersEntry.COLUMN_NAME + " ASC";
    private static final String VALUE_ORDER_BY_AGE = MembersContract.MembersEntry.COLUMN_AGE + " ASC";
    private static final String VALUE_ORDER_BY_GENDER = MembersContract.MembersEntry.COLUMN_GENDER + " ASC";

    private Drawable mMaleBackground;
    private Drawable mFemaleBackground;
    private RecyclerView mMemberRecyclerView;
    private Toast mMessageToast;

    private MembersAdapter mMembersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getDrawbleResource();
        setRecyclerView();
        swipeDeleteMember();

        getLoaderManager().initLoader(MEMBERS_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getLoaderManager().restartLoader(MEMBERS_LOADER_ID, null, this);
    }

    private void getDrawbleResource(){
        Drawable maleBackground, femaleBackground;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            maleBackground = ContextCompat.getDrawable(this, R.drawable.male_background);
            femaleBackground = ContextCompat.getDrawable(this, R.drawable.female_background);
        }else{
            maleBackground = getResources().getDrawable(R.drawable.male_background);
            femaleBackground = getResources().getDrawable(R.drawable.female_background);
        }

        mMaleBackground = maleBackground;
        mFemaleBackground = femaleBackground;
    }

    private void setRecyclerView(){
        mMemberRecyclerView = (RecyclerView) findViewById(R.id.rv_members);
        mMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMembersAdapter = new MembersAdapter(mMaleBackground, mFemaleBackground);
        mMemberRecyclerView.setAdapter(mMembersAdapter);
    }

    private void swipeDeleteMember(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                String idString = Integer.toString(id);

                Uri uri = MembersContract.CONTENT_URI
                        .buildUpon()
                        .appendPath(idString)
                        .build();

                Log.d(TAG,"uri: \t" + uri);

                getContentResolver().delete(uri, null, null);

                getLoaderManager().restartLoader(MEMBERS_LOADER_ID, null, MainActivity.this);
            }
        }).attachToRecyclerView(mMemberRecyclerView);
    }

    public void onAddButtonClicked(View view) {
        createDialog().show();
    }

    private AlertDialog createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(R.layout.alert_add_member, mMemberRecyclerView, false);

        builder.setMessage(R.string.message_add_member)
                .setView(contentView);

        final EditText nameEditText = (EditText) contentView.findViewById(R.id.edit_text_name);
        final EditText ageEditText = (EditText) contentView.findViewById(R.id.edit_text_age);
        final RadioGroup genderRadioGroup = (RadioGroup)contentView.findViewById(R.id.radio_group_gender);

        return builder.setPositiveButton(R.string.message_ok, new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                String age = ageEditText.getText().toString();

                int checkedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
                String gender = (checkedRadioButtonId == R.id.radio_button_male) ? Gender.MALE.name() : Gender.FEMALE.name();

                ContentValues values = new ContentValues();
                values.put(MembersContract.MembersEntry.COLUMN_NAME, name);
                values.put(MembersContract.MembersEntry.COLUMN_AGE, age);
                values.put(MembersContract.MembersEntry.COLUMN_GENDER, gender);
                getContentResolver().insert(MembersContract.CONTENT_URI, values);

                getLoaderManager().restartLoader(MEMBERS_LOADER_ID, null, MainActivity.this);
                displayMessage(name + "\t" +  getString(R.string.message_joined));
            }
        }).setNegativeButton(R.string.message_cancel, null).create();
    }

    private void displayMessage(String message){
        if(mMessageToast != null) mMessageToast.cancel();
        mMessageToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mMessageToast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bundle bundle = new Bundle();

        switch (id){
            case R.id.item_sort_by_id:
                break;
            case R.id.item_sort_by_name:
                bundle.putString(KEY_ORDER,VALUE_ORDER_BY_NAME);
                break;
            case R.id.item_sort_by_age:
                bundle.putString(KEY_ORDER,VALUE_ORDER_BY_AGE);
                break;
            case R.id.item_sort_by_gender:
                bundle.putString(KEY_ORDER,VALUE_ORDER_BY_GENDER);
                break;
        }

        getLoaderManager().restartLoader(MEMBERS_LOADER_ID, bundle, this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == MEMBERS_LOADER_ID){
            return new CursorLoader(this, MembersContract.CONTENT_URI, null, null, null, (args != null) ? args.getString(KEY_ORDER) : null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        if(id == MEMBERS_LOADER_ID){
            mMembersAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        if(id == MEMBERS_LOADER_ID){
            mMembersAdapter.swapCursor(null);
        }
    }
}
