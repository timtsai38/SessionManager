package com.example.user.sessionmanager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.sessionmanager.database.MembersContract;

/**
 * Created by User on 2018/4/2.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {

    private static final String MALE_STRING = "\u2642";
    private static final String FEMALE_STRING = "\u2640";

    private final Drawable MALE_BACKGROUND;
    private final Drawable FEMALE_BACKGROUND;

    private Cursor mCursor;

    public MembersAdapter(Drawable maleBackground, Drawable femaleBackground){
        MALE_BACKGROUND = maleBackground;
        FEMALE_BACKGROUND = femaleBackground;
    }

    private Member getMember(int position){
        if(!mCursor.moveToPosition(position))
            return null;

        return new Member()
                .setId(mCursor.getInt(mCursor.getColumnIndex(MembersContract.MembersEntry._ID)))
                .setName(mCursor.getString(mCursor.getColumnIndex(MembersContract.MembersEntry.COLUMN_NAME)))
                .setAge(mCursor.getInt(mCursor.getColumnIndex(MembersContract.MembersEntry.COLUMN_AGE)))
                .setGender(Gender.valueOf(mCursor.getString(mCursor.getColumnIndex(MembersContract.MembersEntry.COLUMN_GENDER))));
    }

    public void swapCursor(Cursor currentCusor){
        mCursor = currentCusor;
        notifyDataSetChanged();
    }

    @Override
    public MembersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_member, viewGroup, false);

        return new MembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MembersViewHolder holder, int position) {
        Member member = getMember(position);

        holder.mIdTextView.setText(String.valueOf(member.getId()));
        holder.mMemberNameTextView.setText(member.getName());
        holder.mMemberAgeTextView.setText(String.valueOf(member.getAge()));

        if(member.getGender() == Gender.MALE){
            holder.mMemberGenderTextView.setText(MALE_STRING);
            holder.mMemberGenderTextView.setBackground(MALE_BACKGROUND);
        }else {
            holder.mMemberGenderTextView.setText(FEMALE_STRING);
            holder.mMemberGenderTextView.setBackground(FEMALE_BACKGROUND);
        }

        setIdTag(holder);
    }

    private void setIdTag(MembersViewHolder holder) {
        int idColumnIndex = mCursor.getColumnIndex(MembersContract.MembersEntry._ID);
        int id = mCursor.getInt(idColumnIndex);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }

        return mCursor.getCount();
    }

    class MembersViewHolder extends RecyclerView.ViewHolder{

        private TextView mIdTextView;
        private TextView mMemberNameTextView;
        private TextView mMemberAgeTextView;
        private TextView mMemberGenderTextView;

        MembersViewHolder(View itemView) {
            super(itemView);

            mIdTextView = (TextView) itemView.findViewById(R.id.tv_member_id);
            mMemberNameTextView = (TextView) itemView.findViewById(R.id.tv_member_name);
            mMemberAgeTextView = (TextView) itemView.findViewById(R.id.tv_member_age);
            mMemberGenderTextView = (TextView) itemView.findViewById(R.id.tv_member_gender);
        }
    }
}
