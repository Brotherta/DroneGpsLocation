package com.ihm.dronegpsmobileapp

import android.R
import androidx.recyclerview.widget.RecyclerView
import com.ihm.dronegpsmobileapp.PersonListAdapter.PersonViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.squareup.picasso.Picasso
import com.ihm.dronegpsmobileapp.databinding.PersonItemBinding

class PersonListAdapter(private val mPersons: List<PersonData>) : RecyclerView.Adapter<PersonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PersonItemBinding.inflate(layoutInflater, parent, false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val curItem = mPersons[position]
        holder.binding.nameTv.text = curItem.name
        holder.binding.popularityTv.text = curItem.popularity.toString()
        Picasso.get()
                .load(ApiClient.IMAGE_BASE_URL + curItem.profilePath)
                .placeholder(R.drawable.sym_def_app_icon)
                .error(R.drawable.ic_menu_help)
                .into(holder.binding.photoIv)
    }

    override fun getItemCount(): Int {
        return mPersons.size
    }

    class PersonViewHolder(var binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root) /* ---------
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