package com.gg.app.mobilesafe2.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.gg.app.mobilesafe2.R;

public class AnimationUtils {

    // ViewGroup添加动画
    public static LayoutAnimationController getController(AnimationSet as) {
        LayoutAnimationController lac = new LayoutAnimationController(as);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lac.setDelay(0.2f);
        return lac;
    }

    // AnimationSet相当于animator里的<set/>,多种动画效果叠加
    public static AnimationSet getSet(Animation... animations) {
        //true相当于<set/>里的andrroid:shareInterpolator的参数
        AnimationSet Set = new AnimationSet(true);
        for (Animation param : animations) {
            Set.addAnimation(param);
        }
        //如果上面的参数为false，这里的效果就要animation自己设置了
        Set.setStartOffset(0);
        Set.setDuration(500);
        Set.setRepeatCount(1);
        Set.setFillAfter(true);
        Set.setInterpolator(new DecelerateInterpolator());
//        Set.setInterpolator(new LinearInterpolator());匀速
//        Set.setInterpolator(new AccelerateInterpolator());加速
//        Set.setInterpolator(new AccelerateDecelerateInterpolator());加减加
//        Set.setInterpolator(new Interpolator() {
//            @Override
//            public float getInterpolation(float input) {
//                return (float)(Math.sin(2*));
//            }
//        });
        return Set;
    }

    public static TranslateAnimation getTranslate() {
        return new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
    }

    public static AlphaAnimation getAlpha() {
        return new AlphaAnimation(0, 1);
    }

    public static ScaleAnimation getScale() {
        return new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    }

    public static RotateAnimation getRotate() {
        return new RotateAnimation(0, 360, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
    }


}
