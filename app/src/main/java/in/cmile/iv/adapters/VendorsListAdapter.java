package in.cmile.iv.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import in.cmile.iv.R;
import in.cmile.iv.activity.VendorDetailsActivity;
import in.cmile.iv.models.VendorInfo;

/**
 * Created by pintu on 2020-03-27
 */
public class VendorsListAdapter extends RecyclerView.Adapter<VendorsListAdapter.ViewHolder> {
    private Context context;
    private List<VendorInfo> vendorInfoList;
    private int flag = 0;

    public VendorsListAdapter(Context context, List<VendorInfo> vendorInfoList) {
        this.context = context;
        this.vendorInfoList = vendorInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_info_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VendorsListAdapter.ViewHolder holder, int position) {
        final VendorInfo vendorInfo = vendorInfoList.get(position);
        holder.tvShopName.setText(vendorInfo.getName());
        holder.tvShopAddress.setText(vendorInfo.getAddress());
        if (vendorInfo.isShopOpen()) {
            holder.tvIsOpenClose.setText(R.string.open);
        } else {
            holder.tvIsOpenClose.setText(R.string.close);
        }

        Glide.with(context)
                .load(R.drawable.ic_launcher_background)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivShopLogo);

        holder.llFavourite.setOnClickListener(view -> {
            if (flag == 0) {
                holder.ivFavouriteBorder.setVisibility(View.VISIBLE);
                holder.ivFavouriteFill.setVisibility(View.GONE);
                holder.ivFavouriteBorder.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show();
                flag = 1;
            } else if (flag == 1) {
                holder.ivFavouriteFill.setVisibility(View.VISIBLE);
                holder.ivFavouriteBorder.setVisibility(View.GONE);
                holder.ivFavouriteFill.setImageResource(R.drawable.ic_favorite_border);
                Toast.makeText(context, "Removed from favourite", Toast.LENGTH_SHORT).show();
                flag = 0;
            }
        });

        holder.llVendorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VendorDetailsActivity.class);
//                intent.putExtra("name", vendorInfo.getName());
                Bundle b = new Bundle();
                b.putSerializable("vendorInfo", vendorInfo);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName, tvShopAddress, tvIsOpenClose;
        LinearLayout llVendorItem, llFavourite;
        ImageView ivFavouriteBorder, ivFavouriteFill, ivShopLogo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvShopName = (TextView) itemView.findViewById(R.id.tv_shop_name);
            tvShopAddress = (TextView) itemView.findViewById(R.id.tv_shop_address);
            tvIsOpenClose = (TextView) itemView.findViewById(R.id.tv_shop_open_close);
            llVendorItem = (LinearLayout) itemView.findViewById(R.id.ll_vendor_item);
            llFavourite = (LinearLayout) itemView.findViewById(R.id.ll_favourite);
            ivFavouriteBorder = itemView.findViewById(R.id.iv_fav_border);
            ivFavouriteFill = itemView.findViewById(R.id.iv_fav_fill);
            ivShopLogo = itemView.findViewById(R.id.iv_shop_logo);
        }
    }
}