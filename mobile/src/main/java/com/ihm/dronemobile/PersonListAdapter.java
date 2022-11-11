package com.ihm.dronemobile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ihm.dronemobile.databinding.PersonItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.PersonViewHolder> {

    private final List<PersonData> mPersons;

    public PersonListAdapter(List<PersonData> persons) {
        this.mPersons = persons;
    }

    @NonNull
    @Override
    public PersonListAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PersonItemBinding binding = PersonItemBinding.inflate(layoutInflater, parent, false);
        return new PersonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonListAdapter.PersonViewHolder holder, int position) {
        PersonData curItem = mPersons.get(position);

        holder.binding.nameTv.setText(curItem.getName());
        holder.binding.popularityTv.setText(String.valueOf(curItem.getPopularity()));

        Picasso.get()
                .load(ApiClient.IMAGE_BASE_URL + curItem.getProfilePath())
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.ic_menu_help)
                .into(holder.binding.photoIv);
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        PersonItemBinding binding;

        public PersonViewHolder(@NonNull PersonItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /* ---------
     * REMINDER: The "old" way to proceed using hand made view holdler
     * ---------

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        {
            PersonData curItem = mPersons.get(position);
            holder.nameTv.setText(curItem.getName());
            holder.popularityTv.setText(String.valueOf(curItem.getPopularity()));

            Picasso.get()
                    .load(ApiClient.IMAGE_BASE_URL + curItem.getProfilePath())
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(holder.photoIv);
        }
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView photoIv;
        TextView nameTv;
        TextView popularityTv;

        public PersonViewHolder(View v) {
            super(v);
            photoIv = v.findViewById(R.id.photo_iv);
            nameTv = v.findViewById(R.id.name_tv);
            popularityTv = v.findViewById(R.id.popularity_tv);
        }
    }
    */

}

