package com.gstar_info.lab.com.checkinclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gstar_info.lab.com.checkinclass.HomePageActivity;
import com.gstar_info.lab.com.checkinclass.R;
import com.gstar_info.lab.com.checkinclass.Regist1Activity;
import com.gstar_info.lab.com.checkinclass.model.MajorEntity.DataBean;
import com.gstar_info.lab.com.checkinclass.utils.ListViewUtil;

import java.util.ArrayList;
import java.util.List;

public class ListMajorGroupItemAdapter extends BaseAdapter {

    private List<DataBean> objects = new ArrayList<DataBean>();
    private LayoutInflater layoutInflater;
    private AppCompatActivity context;
    private ListAcademyListItemAdapter adapter;
    private boolean flag;


    public ListMajorGroupItemAdapter(Context context, List<DataBean> objects) {
        this.context = (AppCompatActivity) context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }


    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public DataBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_academy_group_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((DataBean) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(DataBean object, ViewHolder holder) {
        //TODO implement
        holder.tvAcademyGroup.setText(object.getKey());
        adapter = new ListAcademyListItemAdapter(context, object.getInfo());
        final List<DataBean.InfoBean> objects = object.getInfo();
        holder.academyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataBean.InfoBean bean = objects.get(position);
                int major_id = Integer.parseInt(bean.getId());
                String major_name = bean.getName();

                if (flag) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    intent.putExtra("major_id", major_id);
                    intent.putExtra("major_name", major_name);
                    context.setResult(HomePageActivity.Academy_res, intent);
                } else {
                    Intent intent = new Intent(context, Regist1Activity.class);
                    intent.putExtra("major_id", major_id);
                    intent.putExtra("major_name", major_name);
                    context.setResult(Regist1Activity.academy_res, intent);
                }

                context.finish();
            }
        });
        holder.academyList.setAdapter(adapter);
        ListViewUtil.setListViewHeightBasedOnChildren(holder.academyList);
    }

    protected class ViewHolder {
        private TextView tvAcademyGroup;
        private ListView academyList;

        public ViewHolder(View view) {
            tvAcademyGroup = (TextView) view.findViewById(R.id.tv_academy_group);
            academyList = (ListView) view.findViewById(R.id.academy_list);
        }
    }

    public class ListAcademyListItemAdapter extends BaseAdapter {

        private List<DataBean.InfoBean> objects = new ArrayList<DataBean.InfoBean>();

        private Context context;
        private LayoutInflater layoutInflater;

        public ListAcademyListItemAdapter(Context context, List<DataBean.InfoBean> objects) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public DataBean.InfoBean getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_academy_list_item, null);
                convertView.setTag(new ViewHolder(convertView));
            }
            initializeViews((DataBean.InfoBean) getItem(position), (ViewHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(DataBean.InfoBean object, ViewHolder holder) {
            //TODO implement
            holder.tvAcademyGroupItem.setText(object.getName());
        }

        protected class ViewHolder {
            private TextView tvAcademyGroupItem;

            public ViewHolder(View view) {
                tvAcademyGroupItem = (TextView) view.findViewById(R.id.tv_academy_group_item);
            }
        }
    }

}

