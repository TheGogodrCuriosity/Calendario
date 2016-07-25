package com.curiosity.calendario;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Gogodr on 22/07/2016.
 */
public class CalendarAdapter extends PagerAdapter{
    public interface OnReload{
        void reload();
    }

    LayoutInflater mInflater;
    Context mContext;
    List<Evento> eventos = new ArrayList<>();
    Calendar hoy;
    List<Calendar> dias = new ArrayList<>();
    HashMap<Calendar,List<List<Evento>>> eventosContenedoresPorDia = new HashMap<>();
    Evento wannaEvento;
    OnReload onReload;



    public CalendarAdapter(Context context, LayoutInflater inflater,List<Evento> eventos, OnReload onReload){
        mContext = context;
        mInflater = inflater;
        this.eventos = eventos;
        this.onReload = onReload;
        hoy = Calendar.getInstance();
        Calendar aux = (Calendar)hoy.clone();
        for(int i = 0;i<30;i++){
            Calendar dia = (Calendar)aux.clone();
            dias.add(dia);
            List<Evento> eventosDelDia = new ArrayList<>();
            for(Evento evento:eventos){
                if(Utils.isSameDay(evento.getStartTime(),dia)){
                    eventosDelDia.add(evento);
                }
            }
            eventosContenedoresPorDia.put(dia, new ArrayList<List<Evento>>());
            eventosContenedoresPorDia.get(dia).add(new ArrayList<Evento>());
            Iterator<Evento> iterator = eventosDelDia.iterator();
            Evento preEvento = null;
            if(iterator.hasNext()){
                preEvento = iterator.next();
                eventosContenedoresPorDia.get(dia).get(0).add(preEvento);
                Log.d("Calendar", "ContainerEvento " + aux.get(Calendar.DAY_OF_MONTH));
                Log.d("Calendar", aux.get(Calendar.DAY_OF_MONTH)+ " " + preEvento.getStartTime().get(Calendar.HOUR_OF_DAY));
            }
            while(iterator.hasNext()){
                Evento nextEvento = iterator.next();
                if(nextEvento.getStartTime().getTimeInMillis() >= preEvento.getEndTime().getTimeInMillis()){
                    Log.d("Calendar","ContainerEvento " + aux.get(Calendar.DAY_OF_MONTH));
                    eventosContenedoresPorDia.get(dia).add(new ArrayList<Evento>());
                }
                eventosContenedoresPorDia.get(dia).get(eventosContenedoresPorDia.get(dia).size()-1).add(nextEvento);
                Log.d("Calendar", aux.get(Calendar.DAY_OF_MONTH) + " " + nextEvento.getStartTime().get(Calendar.HOUR_OF_DAY));
                preEvento = nextEvento;
            }
            aux.add(Calendar.DAY_OF_MONTH,1);
        }
    }
    public View createSpacer(float size){
        View eventoView = new View(mContext);
        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        lp.weight = size;
        eventoView.setLayoutParams(lp);
        return eventoView;
    }
    public View createEvento(float size,int color,String foto){
        RelativeLayout eventoView = new RelativeLayout(mContext);
        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        lp.weight = size;
        eventoView.setLayoutParams(lp);
        eventoView.setBackgroundColor(color);

        ImageView picture = new ImageView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4,4,4,4);
        picture.setLayoutParams(params);
        eventoView.addView(picture);
        Picasso.with(mContext)
                .load(foto)
                .resize(55, 55)
                .placeholder(R.drawable.wanna_p)
                .centerCrop()
                .transform(new CircleTransform())
                .into(picture);

        return eventoView;
    }
    public LinearLayout createEventoContainer(int size){
        LinearLayout ll = new LinearLayout(mContext);
        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        ll.setWeightSum(size);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lp);
        return ll;
    }

    public LinearLayout createEventosContainer(float size,int events){
        LinearLayout ll = new LinearLayout(mContext);
        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        lp.setMargins(15,0,15,0);
        lp.weight = size;
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setWeightSum(events);
        ll.setLayoutParams(lp);
        return ll;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View page = mInflater.inflate(R.layout.calendar_dia_item, container, false);

        LinearLayout eventosLL = (LinearLayout)page.findViewById(R.id.eventosLL);
        eventosLL.addView(createSpacer(1.5f));

        List<List<Evento>> contenedores = eventosContenedoresPorDia.get(dias.get(position));

        int preEndHourContainer = 0;
        for(List<Evento> eventos:contenedores){
            if(eventos.size()>0){
                Log.d("Calendar","-Eventos: "+eventos.size()+"- Dia:"+position);
                int startHourContainer = eventos.get(0).getStartTime().get(Calendar.HOUR_OF_DAY);
                int endHourContainer = eventos.get(eventos.size()-1).getEndTime().get(Calendar.HOUR_OF_DAY);
                Log.d("Calendar","Spacer: "+(startHourContainer - preEndHourContainer));
                eventosLL.addView(createSpacer(startHourContainer - preEndHourContainer));
                LinearLayout eventosContainer = createEventosContainer(endHourContainer-startHourContainer,eventos.size());
                for(Evento evento:eventos){
                    Log.d("Calendar","Hora: "+evento.getStartTime().get(Calendar.HOUR_OF_DAY));
                    int startHour = evento.getStartTime().get(Calendar.HOUR_OF_DAY);
                    int endHour = evento.getEndTime().get(Calendar.HOUR_OF_DAY);
                    LinearLayout eventoContainer = createEventoContainer(endHourContainer - startHourContainer);
                    eventoContainer.addView(createSpacer(startHour-startHourContainer));
                    eventoContainer.addView(createEvento(endHour - startHour,evento.getColor(),evento.getPhotoUser()));
                    eventosContainer.addView(eventoContainer);
                }
                eventosLL.addView(eventosContainer);
                preEndHourContainer = endHourContainer;
            }

        }

        final View preWanna = page.findViewById(R.id.preWanna);
        final RelativeLayout wanna = (RelativeLayout)page.findViewById(R.id.wanna);
        LinearLayout diaLL = (LinearLayout)page.findViewById(R.id.diaLL);
        int childcount = diaLL.getChildCount();
        for (int i=0; i < childcount; i++){
            View v = diaLL.getChildAt(i);
            if(i==0){
                ((TextView)v).setText(Utils.calenderToMonthDay(dias.get(position)));
            }else if(i>1){
                final int hora = (i-2);
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Calendar seleccionado = (Calendar)dias.get(position).clone();
                        seleccionado.set(Calendar.HOUR_OF_DAY,hora);
                        Toast.makeText(mContext,"Dia: " + seleccionado.get(Calendar.DAY_OF_MONTH) + " Hora: "+seleccionado.get(Calendar.HOUR_OF_DAY),Toast.LENGTH_SHORT).show();
                        //TODO
                        //Aca la logica para poner la nueva actividad y mostrar el nuevo evento
                        Calendar startTime = (Calendar)seleccionado.clone();
                        Calendar endTime = (Calendar)seleccionado.clone();
                        endTime.add(Calendar.HOUR_OF_DAY,2);
                        wannaEvento = new Evento(startTime,endTime,null,0);
                        onReload.reload();

                        return true;
                    }
                });
            }
        }


        if(wannaEvento!=null) {
            if (Utils.isSameDay(wannaEvento.getStartTime(), dias.get(position))) {
                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                lps.weight = wannaEvento.getStartTime().get(Calendar.HOUR_OF_DAY);
                preWanna.setLayoutParams(lps);
                LinearLayout.LayoutParams lpw = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                lpw.weight = (wannaEvento.getEndTime().get(Calendar.HOUR_OF_DAY) - wannaEvento.getStartTime().get(Calendar.HOUR_OF_DAY));
                lpw.setMargins(15,0,15,0);
                wanna.setLayoutParams(lpw);
                wanna.setVisibility(View.VISIBLE);
            } else {
                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                lps.weight = 0;
                preWanna.setLayoutParams(lps);
                LinearLayout.LayoutParams lpw = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                lpw.weight = 0;
                wanna.setLayoutParams(lpw);
                wanna.setVisibility(View.GONE);
            }
        }

        container.addView(page);
        return (page);
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView((View) object);
    }
    @Override
    public float getPageWidth(int position) {
        return(0.3333333333f);
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }


}

