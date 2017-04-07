package com.gstar_info.lab.com.checkinclass.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gstar_info.lab.com.checkinclass.AcademySelectActivity;
import com.gstar_info.lab.com.checkinclass.FootTextInterFace;
import com.gstar_info.lab.com.checkinclass.HomePageActivity;
import com.gstar_info.lab.com.checkinclass.R;
import com.gstar_info.lab.com.checkinclass.RecyclerOnItemClickListener;
import com.gstar_info.lab.com.checkinclass.model.SignHistoryEntity.DataBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListSignHistoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DataBean> objects = new ArrayList<DataBean>();

    private AppCompatActivity context;
    private LayoutInflater layoutInflater;
    private static final int dataType = 45;
    private static final int footType = 908;
    public FootTextInterFace interFace;
    private RecyclerOnItemClickListener mItemClickListener;

    public ListSignHistoryItemAdapter(AppCompatActivity context, List<DataBean> objects) {
        this.objects = objects;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }


    public void setRecyclerOnItemClickListener(RecyclerOnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setData(List<DataBean> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }


    public void setInterFace(FootTextInterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == objects.size() + 1)
            return footType;
        if (position > 0)
            return dataType;
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == dataType) {
            RecyclerView.ViewHolder holder;
            holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_signhistory_item, parent, false));
            return holder;
        } else if (viewType == footType) {
            FootViewHolder holder;
            holder = new FootViewHolder(LayoutInflater.from(context).inflate(R.layout.footview, parent, false));
            return holder;
        } else
            return new EmptyHolder(LayoutInflater.from(context).inflate(R.layout.empty_item, null, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if ((position == 0) || (objects.size() == 0)) {

        } else if (position != objects.size() + 1) {
            DataBean object = objects.get(position - 1);
//            String id = object.getId();//课程ID
            String headUrl = null;
            if (object.getHeader() != null) {
                headUrl = object.getHeader().toString();

            }
            String stuname = object.getSname();
            int signstate = -1;
            if (object.getSignState() != null) {
                signstate = Integer.parseInt(object.getSignState());

            }

            if (holder instanceof ViewHolder) {
                if (headUrl == null || !headUrl.contains("http://") || headUrl.isEmpty()) {
                } else {
                    Uri imgUri = Uri.parse(headUrl);
                    ((ViewHolder) holder).imgStuhead.setImageURI(imgUri);
                }
                ((ViewHolder) holder).tvStuname.setText(stuname);
                switch (signstate) {
                    case 0:
                        ((ViewHolder) holder).tvSignstate.setTextColor(Color.parseColor("#ffffffff"));
                        ((ViewHolder) holder).tvSignstate.setText("未签到");
                        break;
                    case 1:
                        ((ViewHolder) holder).tvSignstate.setTextColor(Color.parseColor("#ff28780a"));
                        ((ViewHolder) holder).tvSignstate.setText("已签到");
                        break;
                    case 2:
                        ((ViewHolder) holder).tvSignstate.setTextColor(Color.parseColor("#FFFF4444"));
                        ((ViewHolder) holder).tvSignstate.setText("旷课");
                        break;
                }


            }


        }
        if (position == objects.size() + 1) {
            if (holder instanceof FootViewHolder) {
                interFace.setText(((FootViewHolder) holder).tv_foot);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return objects.size() + 1;
    }


    protected class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_foot;

        public FootViewHolder(View view) {
            super(view);
            tv_foot = (TextView) view.findViewById(R.id.text_foot);
        }
    }

    protected class EmptyHolder extends RecyclerView.ViewHolder {

        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerOnItemClickListener mOnItemClickListener;
        private LinearLayout itemView;
        @BindView(R.id.img_stuhead)
        SimpleDraweeView imgStuhead;
        @BindView(R.id.tv_stuname)
        TextView tvStuname;
        @BindView(R.id.tv_signstate)
        TextView tvSignstate;
        @BindView(R.id.itemView)
        LinearLayout itemmView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }


        public void setOnItemClickListener(RecyclerOnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }


    }
}
