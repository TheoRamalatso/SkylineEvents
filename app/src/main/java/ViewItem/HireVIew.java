package ViewItem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylinestockservice.R;

import Interface.itemClickListener;
//package interface
//...  //for view class access which sets the onclick
public class HireVIew extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemNameOfTextView,itemPriceOfTextView,itemQuantityOfTextView;
    private itemClickListener itemClickListener;

    public HireVIew(@NonNull View itemView) {
        super(itemView);
        itemNameOfTextView = itemView.findViewById(R.id.hireItemName);
        itemPriceOfTextView = itemView.findViewById(R.id.hireItemPrice);
        itemQuantityOfTextView = itemView.findViewById(R.id.hireItemQuantity);
    }

    @Override
    public void onClick(View v) {
        ///..MULT_onclick:
        //view, position , long onclick :
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
    public void setItemClickListener(itemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}
