package com.ramotion.showroom.examples.foldingcell;

import android.view.View;

import java.util.ArrayList;

/**
 * Simple POJO model for example
 */
public class ListItem {

    private String price;
    private String pledgePrice;
    private String fromAddress;
    private String toAddress;
    private int requestsCount;
    private String date;
    private String time;

    private View.OnClickListener requestBtnClickListener;

    public ListItem() {
    }

    public ListItem(String price, String pledgePrice, String fromAddress, String toAddress, int requestsCount, String date, String time) {
        this.price = price;
        this.pledgePrice = pledgePrice;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.requestsCount = requestsCount;
        this.date = date;
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPledgePrice() {
        return pledgePrice;
    }

    public void setPledgePrice(String pledgePrice) {
        this.pledgePrice = pledgePrice;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public int getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(int requestsCount) {
        this.requestsCount = requestsCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListItem listItem = (ListItem) o;

        if (requestsCount != listItem.requestsCount) return false;
        if (price != null ? !price.equals(listItem.price) : listItem.price != null) return false;
        if (pledgePrice != null ? !pledgePrice.equals(listItem.pledgePrice) : listItem.pledgePrice != null)
            return false;
        if (fromAddress != null ? !fromAddress.equals(listItem.fromAddress) : listItem.fromAddress != null)
            return false;
        if (toAddress != null ? !toAddress.equals(listItem.toAddress) : listItem.toAddress != null)
            return false;
        if (date != null ? !date.equals(listItem.date) : listItem.date != null) return false;
        return !(time != null ? !time.equals(listItem.time) : listItem.time != null);

    }

    @Override
    public int hashCode() {
        int result = price != null ? price.hashCode() : 0;
        result = 31 * result + (pledgePrice != null ? pledgePrice.hashCode() : 0);
        result = 31 * result + (fromAddress != null ? fromAddress.hashCode() : 0);
        result = 31 * result + (toAddress != null ? toAddress.hashCode() : 0);
        result = 31 * result + requestsCount;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    /**
     * @return List of elements prepared for tests
     */
    public static ArrayList<ListItem> getTestingList() {
        ArrayList<ListItem> listItems = new ArrayList<>();
        listItems.add(new ListItem("$14", "$270", "W 79th St, NY, 10024", "W 139th St, NY, 10030", 3, "TODAY", "05:10 PM"));
        listItems.add(new ListItem("$23", "$116", "W 36th St, NY, 10015", "W 114th St, NY, 10037", 10, "TODAY", "11:10 AM"));
        listItems.add(new ListItem("$63", "$350", "W 36th St, NY, 10029", "56th Ave, NY, 10041", 0, "TODAY", "07:11 PM"));
        listItems.add(new ListItem("$19", "$150", "12th Ave, NY, 10012", "W 57th St, NY, 10048", 8, "TODAY", "4:15 AM"));
        listItems.add(new ListItem("$5", "$300", "56th Ave, NY, 10041", "W 36th St, NY, 10029", 0, "TODAY", "06:15 PM"));
        return listItems;

    }

}
