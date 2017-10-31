package com.ramotion.showroom.examples.garlandview.profile;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ramotion.showroom.R;
import com.ramotion.showroom.databinding.ProfileItemBinding;
import com.ramotion.showroom.examples.garlandview.details.DetailsData;

import java.util.List;


class ProfileAdapter extends RecyclerView.Adapter<ProfileItem> {

    private final List<DetailsData> mData;

    public ProfileAdapter(final List<DetailsData> data) {
        super();
        mData = data;
    }

    @Override
    public ProfileItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ProfileItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.profile_item, parent, false);
        return new ProfileItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ProfileItem holder, int position) {
        holder.setContent(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
