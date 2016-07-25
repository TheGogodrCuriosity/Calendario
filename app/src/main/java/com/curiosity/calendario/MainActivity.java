package com.curiosity.calendario;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.curiosity.calendario.Utils.getCalendarFromISO;

public class MainActivity extends AppCompatActivity {
    CalendarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager pager=(ViewPager)findViewById(R.id.diasPager);

        List<Evento> eventos = new ArrayList<>();
        eventos.add(new Evento(getCalendarFromISO("2016-07-26T15:00:00.000Z"),getCalendarFromISO("2016-07-26T17:00:00.000Z"),"https://randomuser.me/api/portraits/women/21.jpg", Color.argb(50,150,0,0)));
        eventos.add(new Evento(getCalendarFromISO("2016-07-26T16:00:00.000Z"),getCalendarFromISO("2016-07-26T18:00:00.000Z"),"https://randomuser.me/api/portraits/women/22.jpg", Color.argb(50,0,150,0)));
        eventos.add(new Evento(getCalendarFromISO("2016-07-26T17:00:00.000Z"),getCalendarFromISO("2016-07-26T19:00:00.000Z"),"https://randomuser.me/api/portraits/women/23.jpg", Color.argb(50,0,0,150)));
        eventos.add(new Evento(getCalendarFromISO("2016-07-26T19:00:00.000Z"),getCalendarFromISO("2016-07-26T22:00:00.000Z"),"https://randomuser.me/api/portraits/women/24.jpg", Color.argb(50,75,0,150)));
        eventos.add(new Evento(getCalendarFromISO("2016-07-26T10:00:00.000Z"),getCalendarFromISO("2016-07-26T14:00:00.000Z"),"https://randomuser.me/api/portraits/women/25.jpg", Color.argb(50,15,80,75)));
        eventos.add(new Evento(getCalendarFromISO("2016-07-29T15:00:00.000Z"),getCalendarFromISO("2016-07-29T17:00:00.000Z"),"https://randomuser.me/api/portraits/women/26.jpg", Color.argb(50,0,75,75)));
        eventos.add(new Evento(getCalendarFromISO("2016-07-29T15:00:00.000Z"), getCalendarFromISO("2016-07-29T17:00:00.000Z"), "https://randomuser.me/api/portraits/women/27.jpg", Color.argb(50, 75, 75, 75)));
        Collections.sort(eventos);
        adapter = new CalendarAdapter(this, getLayoutInflater(), eventos, new CalendarAdapter.OnReload() {
            @Override
            public void reload() {
                int aux = pager.getCurrentItem();
                pager.setAdapter(adapter);
                pager.setCurrentItem(aux);
            }
        });
        pager.setOffscreenPageLimit(7);
        pager.setAdapter(adapter);


    }

}
