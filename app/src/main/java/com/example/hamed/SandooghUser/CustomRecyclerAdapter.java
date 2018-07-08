package com.example.hamed.SandooghUser;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>{

    private Context context;
    private OzvFragment listUserActivity;
    private int row_index=-1;
    private List<PersonUtils> personUtils;
    //Typeface typeface;
    DecimalFormat formatter=new DecimalFormat("#,###,###");

    public CustomRecyclerAdapter(Context context, List personUtils) {
        this.context =context;
        this.personUtils = personUtils;
       // this.typeface=Typeface.createFromAsset(context.getAssets(),"IRANSansMobile.ttf");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(personUtils.get(position));

        final PersonUtils pu = personUtils.get(position);


        if(row_index==position){
            holder.row_cardView.setCardBackgroundColor(Color.RED);
            holder.userid.setTextColor(Color.WHITE);
            holder.username.setTextColor(Color.WHITE);
            holder.mojodi.setTextColor(Color.WHITE);
            row_index=-1;

        }
        else
        {

            if(pu.getwam()==1) {
                holder.userid.setBackgroundResource(R.drawable.border);
                holder.userid.setTextColor(Color.WHITE);
            }
            else {
                holder.userid.setBackgroundColor(Color.WHITE);
                holder.userid.setTextColor(Color.BLACK);

            }

            holder.username.setTextColor(Color.BLACK);
            holder.mojodi.setTextColor(Color.BLACK);
        }

        holder.userid.setText(Integer.toString(pu.getid()));
        holder.username.setText(pu.getusername());
        holder.mojodi.setText(formatter.format(pu.getmojodi())+"ریال");



    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userid;
        public TextView username;
        public TextView mojodi;
        public TextView textName,textMojodi;
        public LinearLayout row_linearLayout;
        public RecyclerView rv2;
        public CardView row_cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            userid=(TextView)itemView.findViewById(R.id.userId);
            username = (TextView) itemView.findViewById(R.id.username);
            mojodi = (TextView) itemView.findViewById(R.id.mojodi);
            row_linearLayout=(LinearLayout)itemView.findViewById(R.id.row_linearLayout);
            rv2=(RecyclerView)itemView.findViewById(R.id.recycleViewOzv);
            row_cardView=(CardView)itemView.findViewById(R.id.row_CardView);
            textName=(TextView)itemView.findViewById(R.id.texviewName);
            textMojodi=(TextView)itemView.findViewById(R.id.textviewMojodi);
        }


    }


}