package com.ssit.realm.adapters;

import android.content.Context;

import com.ssit.realm.model.Users;

import io.realm.RealmResults;

public class RealmUserAdapter extends RealmModelAdapter<Users> {

    public RealmUserAdapter(Context context, RealmResults<Users> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}