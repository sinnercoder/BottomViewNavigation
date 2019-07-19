package com.pankaj.BottomView;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    VectorMasterView hourglassView;
    BottomNavigationView bottomNavigationMenu;
    float start[] = {0.08f,0.27f,0.45f, 0.63f,0.80f};
    float end[] = {0.17f,0.36f,0.54f,0.72f,0.90f};
    int prevSelected = 0;
    int currentSelected = 0;
    int TIMER_DELAY = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hourglassView = findViewById(R.id.heart_vector);
        bottomNavigationMenu = findViewById(R.id.bottom_nav_view);


        openFragmente(new FragmentOne());
        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                prevSelected = currentSelected;
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        openFragmente(new FragmentOne());
                        currentSelected = 0;
                        animate();

                        break;
                    case R.id.products:
                        openFragmente(new FragmentOne());
                        currentSelected = 1;
                        animate();

                        break;
                    case R.id.collections:
                        openFragmente(new FragmentOne());
                        currentSelected = 2;
                        animate();
                        break;

                    case R.id.cart:
                        openFragmente(new FragmentOne());
                        currentSelected = 3;
                        animate();
                        break;

                    case R.id.profile:
                        openFragmente(new FragmentOne());
                        currentSelected = 4;
                        animate();

                        break;
                }
                return true;
            }
        });

        animate();
    }

    void animate() {

        final PathModel outline = hourglassView.getPathModelByName("outline");
        final float[] startTemp = {start[currentSelected]};
        final float[] endTemp = {end[currentSelected]};
        if(currentSelected == prevSelected) {
            outline.setTrimPathStart(startTemp[0]);
            outline.setTrimPathEnd(endTemp[0]);
            hourglassView.update();
        } else if (currentSelected > prevSelected) {
            startTemp[0] = start[prevSelected];
            endTemp[0] = end[currentSelected];
            outline.setTrimPathStart(startTemp[0]);
            outline.setTrimPathEnd(endTemp[0]);
            hourglassView.update();
            animateLeftToRightImpl();
        } else {
            startTemp[0] = start[currentSelected];
            endTemp[0] = end[prevSelected];
            outline.setTrimPathStart(startTemp[0]);
            outline.setTrimPathEnd(endTemp[0]);
            hourglassView.update();
            animateRightToLeftImpl();
        }
    }

    private void animateLeftToRightImpl() {

        System.out.println(start[currentSelected]+":"+start[prevSelected]);
        final float[] previousStartTemp = {start[prevSelected]};
        final PathModel outline = hourglassView.getPathModelByName("outline");
        final Timer timer = new Timer();
        float timeDiff = start[currentSelected] - previousStartTemp[0];
        if(timeDiff > 0.2f)
            TIMER_DELAY = 3;
        else if(timeDiff >0.4f)
            TIMER_DELAY = 1;
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if((start[currentSelected] - previousStartTemp[0])>0.01) {
                    previousStartTemp[0] += 0.01;
                    System.out.println(previousStartTemp[0]);
                    outline.setTrimPathStart(previousStartTemp[0]);
                } else {
                    outline.setTrimPathStart(start[currentSelected]);
                    timer.cancel();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hourglassView.update();
                    }
                });
            }
        },10,TIMER_DELAY);
    }

    private void animateRightToLeftImpl() {

        System.out.println(end[currentSelected]+":"+end[prevSelected]);
        final float[] previousEndTemp = {end[prevSelected]};
        final PathModel outline = hourglassView.getPathModelByName("outline");
        final Timer timer = new Timer();
        float timeDiff = previousEndTemp[0] - end[currentSelected];
        if(timeDiff > 0.2f)
            TIMER_DELAY = 3;
        else if(timeDiff >0.4f)
            TIMER_DELAY = 1;
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if((previousEndTemp[0] - end[currentSelected])>0.01) {
                    previousEndTemp[0] -= 0.01;
                    System.out.println(previousEndTemp[0]+":"+end[currentSelected]);
                    outline.setTrimPathEnd(previousEndTemp[0]);
                } else {
                    outline.setTrimPathEnd(end[currentSelected]);
                    timer.cancel();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hourglassView.update();
                    }
                });
            }
        },10,TIMER_DELAY);
    }

    private void openFragmente(Fragment fragment) {
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragmen_open, R.anim.fragmen_open);
        fragmentTransaction.replace(R.id.homeFrame,fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
