package com.example.ponomarev.automobileguide.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ponomarev.automobileguide.R;

import java.util.ArrayList;
import java.util.List;


public class ExpandableRecyclerAdapter extends RecyclerView.Adapter<ExpandableRecyclerAdapter.ViewHolder> {

    private List<String> names;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context context;
    ArrayList<ArrayList<ArrayList<String>>> information;

    public ExpandableRecyclerAdapter(List<String> names, ArrayList<ArrayList<ArrayList<String>>> information) {
        this.names = names;
        this.information=information;
        for (int i = 0; i < names.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public ExpandableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.context=viewGroup.getContext();
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_expandable, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final ExpandableRecyclerAdapter.ViewHolder viewHolder, final  int i) {

            viewHolder.setIsRecyclable(false);
            viewHolder.tvName.setText(names.get(i));
            final boolean isExpanded = expandState.get(i);
            viewHolder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            TextView textViewHeader;
            TextView textViewDesc;

            int count=information.get(i).get(0).size();
            for(int j=0;j<count;j++) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewList = layoutInflater.inflate(R.layout.adapter_item_view, null);

                textViewHeader=(TextView)viewList.findViewById(R.id.itemHeader);
                textViewDesc=(TextView)viewList.findViewById(R.id.itemDescription);
                textViewHeader.setText(information.get(i).get(0).get(j));
                textViewDesc.setText(information.get(i).get(1).get(j));
                viewHolder.expandableLayout.addView( viewList);
            }

            viewHolder.buttonLayout.setRotation(expandState.get(i) ? 180f : 0f);
            viewHolder.expHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int position=i-1;
                    onClickButton(viewHolder.expandableLayout, viewHolder.buttonLayout, position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        public RelativeLayout buttonLayout;
        public LinearLayout expandableLayout;
        public RelativeLayout expHeader;
        public Spinner spinner;

        public ViewHolder(View view) {
            super(view);

                expHeader = (RelativeLayout) view.findViewById(R.id.exp_header);
                tvName = (TextView) view.findViewById(R.id.textView_name);
                buttonLayout = (RelativeLayout) view.findViewById(R.id.button);
                expandableLayout = (LinearLayout) view.findViewById(R.id.expandableLayout);
        }
    }

    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final  int i) {

        if (expandableLayout.getVisibility() == View.VISIBLE){
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        }else{
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(150);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}