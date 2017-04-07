package com.gstar_info.lab.com.checkinclass.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gstar_info.lab.com.checkinclass.AcademySelectActivity;
import com.gstar_info.lab.com.checkinclass.FootTextInterFace;
import com.gstar_info.lab.com.checkinclass.HomePageActivity;
import com.gstar_info.lab.com.checkinclass.R;
import com.gstar_info.lab.com.checkinclass.RecyclerOnItemClickListener;
import com.gstar_info.lab.com.checkinclass.model.courseShowEntity.DataBean;

import java.util.ArrayList;
import java.util.List;

public class ListCourseItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DataBean> objects = new ArrayList<DataBean>();

    private AppCompatActivity context;
    private LayoutInflater layoutInflater;
    private int aid;
    private String aname;
    private static final int dataType = 45;
    private static final int footType = 908;
    private static final int headType = 167;
    public FootTextInterFace interFace;
    private RecyclerOnItemClickListener mItemClickListener;

    public ListCourseItemAdapter(AppCompatActivity context, List<DataBean> objects, int aid, String aname) {
        this.objects = objects;
        this.context = context;
        this.aid = aid;
        this.aname = aname;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public ListCourseItemAdapter(AppCompatActivity context, List<DataBean> objects) {
        this.objects = objects;
        this.context = context;
    }


    public void setRecyclerOnItemClickListener(RecyclerOnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setData(List<DataBean> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public void setAacademy(int aid, String aname) {
        this.aid = aid;
        this.aname = aname;
        notifyDataSetChanged();
    }

    public void setInterFace(FootTextInterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return headType;
        else if (position == objects.size() + 1)
            return footType;
        return dataType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == headType) {
            HeadHolder holder;
            holder = new HeadHolder(LayoutInflater.from(context).inflate(R.layout.headview, parent, false));
            return holder;
        } else if (viewType == dataType) {
            RecyclerView.ViewHolder holder;
            holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_course_item, parent, false));
            return holder;
        } else {
            FootViewHolder holder;
            holder = new FootViewHolder(LayoutInflater.from(context).inflate(R.layout.footview, parent, false));
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            if (holder instanceof HeadHolder) {
                ((HeadHolder) holder).tv_title.setText(aname);
                ((HeadHolder) holder).tv_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof HomePageActivity) {
                            Intent intent = new Intent(context, AcademySelectActivity.class);
                            intent.putExtra("flag", 2);
                            context.startActivityForResult(intent, ((HomePageActivity) context).Academy_req);
                        }
                    }
                });
            }
        }
        if ((position != objects.size() + 1) && (position != 0)) {
            DataBean object = objects.get(position - 1);
//            String id = object.getId();//课程ID
            String courseName = object.getC_name();
            String headUrl = object.getHeader();
            String teacherName = object.getTeacher();
            String place = object.getPlace();
            String time = object.getTime();
            String grade = object.getGpa();
            String number = object.getNumber();
            if (holder instanceof ViewHolder) {
                if (headUrl == null || !headUrl.contains("http://") || headUrl.isEmpty()) {
                    ((ViewHolder) holder).courseIcon.setImageResource(R.mipmap.logo);
                } else {
                    Uri imgUri = Uri.parse(headUrl);
                    ((ViewHolder) holder).courseIcon.setImageURI(imgUri);
                }
                if (courseName.length() > 8) {
                    courseName = courseName.substring(0, 8);
                    courseName += "...";
                }
                ((ViewHolder) holder).tvCoursename.setText(courseName);
                ((ViewHolder) holder).tvTeacher.setText(teacherName);
                ((ViewHolder) holder).tvGrade.setText(grade);
                ((ViewHolder) holder).tvPlace.setText(place);
                ((ViewHolder) holder).tvTime.setText(time);
                ((ViewHolder) holder).tvStudentnumber.setText(number);
            }

            if (mItemClickListener != null) {
                ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(v, position);
                    }
                });
                ((ViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mItemClickListener.onItemLongClick(v, position);
                        return true;
                    }
                });
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
        return objects.size() + 2;
    }


    protected class HeadHolder extends RecyclerView.ViewHolder {
        private Toolbar toolbar;
        private TextView tv_title;

        public HeadHolder(View view) {
            super(view);
            toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
        }
    }

    protected class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_foot;

        public FootViewHolder(View view) {
            super(view);
            tv_foot = (TextView) view.findViewById(R.id.text_foot);
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCoursename;
        private SimpleDraweeView courseIcon;
        private TextView tvTeacher;
        private TextView tvPlace;
        private TextView tvTime;
        private TextView tvGrade;
        private TextView tvStudentnumber;
        private SimpleDraweeView courseState;
        private RecyclerOnItemClickListener mOnItemClickListener;
        private RelativeLayout itemView;


        public ViewHolder(View view) {
            super(view);
            itemView = (RelativeLayout) view.findViewById(R.id.itemView);
            tvCoursename = (TextView) view.findViewById(R.id.tv_coursename);
            courseIcon = (SimpleDraweeView) view.findViewById(R.id.course_icon);
            tvTeacher = (TextView) view.findViewById(R.id.tv_teacher);
            tvPlace = (TextView) view.findViewById(R.id.tv_place);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvGrade = (TextView) view.findViewById(R.id.tv_grade);
            tvStudentnumber = (TextView) view.findViewById(R.id.tv_studentnumber);
            courseState = (SimpleDraweeView) view.findViewById(R.id.course_state);

        }

        public void setOnItemClickListener(RecyclerOnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }


    }
}
