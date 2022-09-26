package com.example.testchart.common;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testchart.data.ShotData;
import com.example.testchart.data.StatsTargetDay;
import com.example.testchart.data.StatsTargetInfo;
import com.example.testchart.databinding.ItemStatsByDateTargetBinding;
import com.example.testchart.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class StatsByTargetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public enum ORDER_TARGET {
        DISTANCE_ASC,
        DISTANCE_DESC,
        SCORE_ASC,
        SCORE_DESC
    }

    protected ItemClickListener listener;
    protected List<StatsTargetDay> datalist;
    protected ORDER_TARGET orderTarget;

    public StatsByTargetAdapter() {
        orderTarget = ORDER_TARGET.DISTANCE_ASC;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemStatsByDateTargetBinding iBinding = ItemStatsByDateTargetBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        return new ItemViewHolder(viewGroup.getContext(), iBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemViewHolder) {
            if (datalist != null) {
                ((ItemViewHolder) viewHolder).bind(datalist.get(i));
                ((ItemViewHolder) viewHolder).setOnItemClickListener(listener);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (datalist == null) return 0;
        return datalist.size();
    }

    public void setList(List<StatsTargetDay> list) {
        datalist = list;
        sortList();
        notifyDataSetChanged();
    }

    public void setOrderTarget(ORDER_TARGET order) {
        orderTarget = order;
        sortList();
        notifyDataSetChanged();
    }

    protected void sortList() {
        if (datalist == null) return;

        Collections.sort(datalist, new Comparator<StatsTargetDay>() {
            @Override
            public int compare(StatsTargetDay o1, StatsTargetDay o2) {
                switch (orderTarget) {
                    case DISTANCE_ASC:
                        if (o1.getTargetDistanceToMeter() > o2.getTargetDistanceToMeter()) return 1;
                        else if (o1.getTargetDistanceToMeter() == o2.getTargetDistanceToMeter())
                            return 0;
                        else return -1;
                    case DISTANCE_DESC:
                        if (o1.getTargetDistanceToMeter() > o2.getTargetDistanceToMeter())
                            return -1;
                        else if (o1.getTargetDistanceToMeter() == o2.getTargetDistanceToMeter())
                            return 0;
                        else return 1;
                    case SCORE_ASC:
                        if (o1.getAverageScore() > o2.getAverageScore()) return 1;
                        else if (o1.getAverageScore() == o2.getAverageScore()) return 0;
                        else return -1;
                    case SCORE_DESC:
                        if (o1.getAverageScore() > o2.getAverageScore()) return -1;
                        else if (o1.getAverageScore() == o2.getAverageScore()) return 0;
                        else return 1;
                }
                return 0;
            }
        });
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemStatsByDateTargetBinding binding;
        private Context context;
        private ItemClickListener listener;

        public ItemViewHolder(Context context, ItemStatsByDateTargetBinding binding) {
            super(binding.getRoot());
            this.context = context;
            this.binding = binding;
        }

        public void bind(StatsTargetDay stData) {

            binding.layoutBG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onClick(stData.getTargetDistance(), stData.getTargetUnit());
                }
            });

            binding.targetButton.setOnClickListener(view ->binding.layoutTargetChart.targetTest(100));

            setDistance(stData.getTargetDistance());
            setTextDistanceUnit(stData.getTargetUnit());
            binding.tvScore.setText(String.format("%.01f", Utils.Companion.floatRoundPosition(stData.getAverageScore(), 1)));
//            setShotData(stData.getStats(), stData.getTargetUnit());
//            Log.e("hjh", stData.toString());
            setTargetChart(stData);

        }


        //단위 거리 속도 높이
        // 0: yard / mph / ft
        // 1: yard / m/s / ft
        // 2: m / m/s / ft
        // 3: m / mph / ft
        // 4: m / m/s / m
        // 5: m / mph / m
        // 6: yard / mph / m
        // 7: yard / m/s / m

        protected void setTargetChart(StatsTargetDay stData) {
//            binding.layoutTargetChart.setTargetDistance((int) stData.getTargetDistance());

//            byte bUnit = Utils.Companion.getSettingUnit();
//
//            if (stData.getTargetUnit() != null && stData.getTargetUnit().compareTo("meter") == 0) {
//                if (bUnit == 0) {
//                    bUnit = 3;
//                } else if (bUnit == 1) {
//                    bUnit = 2;
//                } else if (bUnit == 6) {
//                    bUnit = 4;
//                } else if (bUnit == 7) {
//                    bUnit = 5;
//                }
//            } else {
//                if (bUnit == 2) {
//                    bUnit = 1;
//                } else if (bUnit == 3) {
//                    bUnit = 0;
//                } else if (bUnit == 4) {
//                    bUnit = 6;
//                } else if (bUnit == 5) {
//                    bUnit = 7;
//                }
//            }
//            binding.layoutTargetChart.setUnit(stData.getTargetUnit());

//            ShotData lastShotdata = null;
//            ArrayList<ShotData> listShotData = new ArrayList<>();
//
//            if (stData.getStats().size() > 0) {
//                int lastIndex = stData.getStats().size() - 1;
//                if (lastIndex > 2) lastIndex = 2;
//                StatsTargetInfo sti = stData.getStats().get(lastIndex);
//                lastShotdata = statsTargetInfo2ShotData(sti);
//
//                for (int i = 0; i < stData.getStats().size() && i < 3; i++) {
//                    sti = stData.getStats().get(i);
//                    listShotData.add(statsTargetInfo2ShotData(sti));
//                }
//            }
//
//            Log.e("hjh", "LastShotData : " + lastShotdata.toString() + "\n ListShotData : " + listShotData.toString());
//            binding.layoutTargetChart.drawStatsChart(lastShotdata, listShotData, MSCTargetChart.Mode.STATS, stData.getTargetUnit(), (int) stData.getTargetDistance());
            binding.layoutTargetChart.drawStatsChart(stData);
        }

        private ShotData statsTargetInfo2ShotData(StatsTargetInfo sti) {
            ShotData sd = new ShotData();
            sd.setClub_code(sti.getClubCode());
            sd.setClub_nickname("");
            sd.setApex(sti.getAverageApex());
            sd.setBall_speed(sti.getAverageBallSpeed());
            sd.setCarry(sti.getAverageCarry());
            sd.setClub_speed(sti.getAverageSwingSpeed());
            sd.setLaunch_angle(sti.getAverageLaunchAngle());
            sd.setTotal(sti.getAverageDrivingDistance());
//            sd.setClub_speed(sti.getAverageSwingSpeed());
            return sd;
        }

//        protected void setShotData(List<StatsTargetInfo> listShot, String unit){
//            binding.ivInfo1.setImageDrawable(context.getDrawable(listShot.size()==1 ?  R.drawable.red_01_o : R.drawable.gray_01_o));
//            binding.ivInfo2.setImageDrawable(context.getDrawable(listShot.size()==2 ?  R.drawable.red_02_o : R.drawable.gray_02_o));
//            binding.ivInfo3.setImageDrawable(context.getDrawable(listShot.size()>=3 ?  R.drawable.red_03_o : R.drawable.gray_03_o));
//
//            String szUnit = "yd";
//            boolean isYardUnit = true;
//            if(unit != null && (unit.compareTo("meter")==0||unit.compareTo("m")==0)) {
//                szUnit = "m";
//                isYardUnit = false;
//            }
//
//            if(listShot.size() >= 1){
//                StatsTargetInfo si = listShot.get(0);
//                binding.tvInfo1.setText(String.format("%s-%.01f%s", getClubType(si), isYardUnit? Utils.floatRoundPosition(si.getAverageCarry()*App.UNIT_YARD, 1):Utils.floatRoundPosition(si.getAverageCarry(), 1), szUnit));
//            }else{
//                binding.tvInfo1.setText("----");
//            }
//
//            if(listShot.size() >= 2){
//                StatsTargetInfo si = listShot.get(1);
//                binding.tvInfo2.setText(String.format("%s-%.01f%s", getClubType(si), isYardUnit? Utils.floatRoundPosition(si.getAverageCarry()*App.UNIT_YARD, 1):Utils.floatRoundPosition(si.getAverageCarry(), 1), szUnit));
//            }else{
//                binding.tvInfo2.setText("----");
//            }
//
//            if(listShot.size() >= 3){
//                StatsTargetInfo si = listShot.get(2);
//                binding.tvInfo3.setText(String.format("%s-%.01f%s", getClubType(si), isYardUnit? Utils.floatRoundPosition(si.getAverageCarry()*App.UNIT_YARD, 1):Utils.floatRoundPosition(si.getAverageCarry(), 1), szUnit));
//            }else{
//                binding.tvInfo3.setText("----");
//            }
//        }

//        protected String getClubType(StatsTargetInfo si){
//            String ct = si.getClubType();
//            if(ct==null || ct.isEmpty() || ct.compareTo("null")==0){
//                UserClubInfo clubInfo = GlobalViewModel.getClubInfo(si.getClubCode());
//                if(clubInfo != null){
//                    ct = clubInfo.getClub_type();
//                }
//
//            }
//            return ct;
//        }

        protected void setTextDistanceUnit(String unit) {
            if (unit != null && (unit.compareTo("meter") == 0 || unit.compareTo("m") == 0)) {
                binding.tvDistanceUnit.setText("m");
            } else {
                binding.tvDistanceUnit.setText("yd");
            }
        }

        protected void setDistance(float distance) {
            binding.tvDistance.setText(Integer.toString((int) distance));

//            if(distance > 150f){
//                binding.layoutDistance.setBackground(context.getDrawable(R.drawable.bg_round_lipstick));
//            }else if(distance > 100f){
//                binding.layoutDistance.setBackground(context.getDrawable(R.drawable.bg_round_sky_blue));
//            }else if(distance > 50f){
//                binding.layoutDistance.setBackground(context.getDrawable(R.drawable.bg_round_greenish_teal));
//            }else{
//                binding.layoutDistance.setBackground(context.getDrawable(R.drawable.bg_round_tangerine));
//            }
        }

        public void setOnItemClickListener(ItemClickListener listener) {
            this.listener = listener;
        }
    }

    public interface ItemClickListener {
        void onClick(float targetDistance, String unit);
    }
}
