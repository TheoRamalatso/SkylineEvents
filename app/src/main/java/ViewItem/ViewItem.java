package ViewItem;

import android.view.View;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylinestockservice.R;

import Interface.itemClickListener;
//HomepageView
public class ViewItem extends RecyclerView.ViewHolder implements View.OnClickListener  {
    public TextView textItemName,textItemDescription,textItemPrice;//will be linked to the item info'
    public ImageView itemImageView;
    //for quick access
    public itemClickListener listner; //gotmal




    //cons
    public ViewItem(@NonNull View itemView) {
        super(itemView);
        //access Relocations
        itemImageView = (ImageView) itemView.findViewById(R.id.imageView);
        textItemName = (TextView) itemView.findViewById(R.id.itemName);
        textItemDescription = (TextView) itemView.findViewById(R.id.descriptionView);
        textItemPrice = (TextView) itemView.findViewById(R.id.price);
    }

    //set item listener from a package interface class
    //initialize on start in HomeActivity
    public void setItemClickListner(itemClickListener listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }

}
