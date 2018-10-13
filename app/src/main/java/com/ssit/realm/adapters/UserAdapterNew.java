package com.ssit.realm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ssit.realm.R;
import com.ssit.realm.activity.UserDetails;
import com.ssit.realm.app.Prefs;
import com.ssit.realm.model.Users;
import com.ssit.realm.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserAdapterNew extends RealmRecyclerViewAdapter<Users> {

    final Context context;
    private Realm realm;
    public UserAdapterNew(Context context) {

        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usernew, parent, false);
        return new CardViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmController.getInstance().getRealm();

        // get the article
        final Users user = getItem(position);
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        // set the title and the snippet
        holder.name.setText(user.getName());
        holder.phoneno.setText(user.getPhone());
        holder.companyname.setText(user.getCompany());

        // load the background image
        if (user.getUserimage() != null) {
            Glide.with(context)
                    .load(user.getUserimage().replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(holder.userimage);
        }

        //remove single match from realm
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                RealmResults<Users> results = realm.where(Users.class).findAll();

                // Get the user name to show it in toast message
                Users b = results.get(position);
                String name = b.getName();

                // All changes to data must happen in a transaction
                realm.beginTransaction();

                // remove single match
                results.remove(position);
                realm.commitTransaction();

                if (results.size() == 0) {
                    Prefs.with(context).setPreLoad(false);
                }

                notifyDataSetChanged();

                Toast.makeText(context, name + " is removed from Realm", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //update single match from realm
        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserDetails.class);
                intent.putExtra("position", user.getId());
                context.startActivity(intent);
            }
        });
    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView name;
        public TextView phoneno;
        public TextView companyname;
        public ImageView userimage;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_books);
            name = (TextView) itemView.findViewById(R.id.name);
            phoneno = (TextView) itemView.findViewById(R.id.phoneno);
            companyname = (TextView) itemView.findViewById(R.id.companyname);
            userimage = (ImageView) itemView.findViewById(R.id.userimage);
        }
    }
}
