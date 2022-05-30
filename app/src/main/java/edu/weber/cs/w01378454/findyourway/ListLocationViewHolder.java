package edu.weber.cs.w01378454.findyourway;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListLocationViewHolder extends RecyclerView.ViewHolder
{
    TextView textViewLocationName;

    public ListLocationViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewLocationName = itemView.findViewById(R.id.textViewLocationName);
    }


}
