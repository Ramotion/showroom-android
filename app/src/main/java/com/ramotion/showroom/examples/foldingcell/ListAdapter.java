package com.ramotion.showroom.examples.foldingcell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.ramotion.foldingcell.FoldingCell;
import com.ramotion.showroom.R;

import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class ListAdapter extends ArrayAdapter<ListItem> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;


    public ListAdapter(Context context, List<ListItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        ListItem listItem = getItem(position);
        // if fc_cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.fc_cell, parent, false);
            // binding view parts to view holder
            viewHolder.price = (TextView) cell.findViewById(R.id.fc_title_price);
            viewHolder.time = (TextView) cell.findViewById(R.id.fc_title_time_label);
            viewHolder.date = (TextView) cell.findViewById(R.id.fc_title_date_label);
            viewHolder.fromAddress = (TextView) cell.findViewById(R.id.fc_title_from_address);
            viewHolder.toAddress = (TextView) cell.findViewById(R.id.fc_title_to_address);
            viewHolder.requestsCount = (TextView) cell.findViewById(R.id.fc_title_requests_count);
            viewHolder.pledgePrice = (TextView) cell.findViewById(R.id.fc_title_pledge);
            viewHolder.contentRequestBtn = (TextView) cell.findViewById(R.id.fc_content_request_btn);
            cell.setTag(viewHolder);
        } else {
            // for existing fc_cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        viewHolder.price.setText(listItem.getPrice());
        viewHolder.time.setText(listItem.getTime());
        viewHolder.date.setText(listItem.getDate());
        viewHolder.fromAddress.setText(listItem.getFromAddress());
        viewHolder.toAddress.setText(listItem.getToAddress());
        viewHolder.requestsCount.setText(String.valueOf(listItem.getRequestsCount()));
        viewHolder.pledgePrice.setText(listItem.getPledgePrice());

        if (listItem.getRequestBtnClickListener() != null) {
            viewHolder.contentRequestBtn.setOnClickListener(listItem.getRequestBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in listItem
            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
        }

        return cell;
    }

    // simple methods for register fc_cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView contentRequestBtn;
        TextView pledgePrice;
        TextView fromAddress;
        TextView toAddress;
        TextView requestsCount;
        TextView date;
        TextView time;
    }
}
