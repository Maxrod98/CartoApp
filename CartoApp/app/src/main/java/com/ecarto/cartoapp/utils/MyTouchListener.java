package com.ecarto.cartoapp.utils;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class MyTouchListener implements View.OnTouchListener {
    //TESTING VARS
    float lastLoc = 0;
    boolean isOpen = false;
    float lastGlobalLoc = 0;
    Listener listener;

    View movedView;
    View handlerView;

    float maximumX;
    float maximumXWithOffset;

    public MyTouchListener(){

    }

    public MyTouchListener(View movedView, View handlerView, Listener listener){
        this.movedView = movedView;
        this.handlerView  = handlerView;
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        maximumX = this.movedView.getRootView().getWidth();
        maximumXWithOffset = maximumX - this.handlerView.getWidth() * 2;

        boolean flag = false;

        //Log.d("SLIDE", String.valueOf(event.getRawX()) + "  event = " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                lastLoc = event.getX();
                lastGlobalLoc = event.getRawX();
                flag = true;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float distance = (event.getRawX() - lastLoc);
                final float alpha = 1 - distance / maximumX;

                movedView.animate()
                        .setDuration(0)
                        .translationX(distance > maximumXWithOffset ? maximumXWithOffset : distance)
                        .alpha(distance > maximumXWithOffset ? 1 : alpha).start();

                flag = true;
                break;
            }

            case MotionEvent.ACTION_UP: {

                if (Math.abs(lastGlobalLoc - event.getRawX()) < 20){
                    openOrClose(!isOpen);
                } else {
                    boolean shouldOpen = event.getRawX() > maximumX / 2 && !isOpen;
                    openOrClose(shouldOpen);
                }


                break;
            }
        }
        Log.d("SLIDE", "CLICK " + event.getHistorySize());

        return flag;
    }

    private void openOrClose(boolean shouldOpen){
        float movement = 0;
        if (shouldOpen ) {
            movement = maximumXWithOffset;
            isOpen = true;
            listener.onOpen();
        } else {
            movement = 0;
            isOpen = false;
            listener.onClose();
        }
        movedView.animate().translationX(movement)
                .setDuration(100)
                .setInterpolator(new DecelerateInterpolator())
                .alpha(1)
                .start();
    }

    public interface Listener {
        void onOpen();
        void onClose();
    }
}