package com.sun.toy.fam.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sun.toy.fam.R;
import com.sun.toy.fam.ViewUtils;

import java.util.ArrayList;

/**
 * Created by hatti on 2017. 1. 16..
 */

public class FloatingActionMenuLayout extends RelativeLayout implements View.OnClickListener {
    private static final float FAB_PADDING = 16;
    private static final float MENU_HEIGHT = 72;
    final int DURATION = 200;

    private boolean isExpanded;private int fabRightMargin;

    private int fabBottomMargin;
    private FloatingActionButton fab;
    private OnClickListener mOnClickListener;
    private LinearLayout bottomMenu;
    private int fabMenuHeight;
    private ArrayList<Pair> mList = new ArrayList<>();
    private int background;

    public FloatingActionMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        initView();
    }

    private void init(AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        fabRightMargin = fabBottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FAB_PADDING, dm);
        fabMenuHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MENU_HEIGHT, dm);
        background = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        // get custom attrs
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.fabMenu);
        fabBottomMargin = a.getDimensionPixelSize(R.styleable.fabMenu_margin_bottom, fabBottomMargin);
        fabRightMargin = a.getDimensionPixelSize(R.styleable.fabMenu_margin_right, fabRightMargin);
        fabMenuHeight = a.getDimensionPixelSize(R.styleable.fabMenu_menu_height, fabMenuHeight);
//        background = a.getColor(R.styleable.fabMenu_menu_background, background);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initView() {

        // init fab
        fab = new FloatingActionButton(getContext());
        fab.setId(View.generateViewId());
        fab.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorPrimary));
        fab.setRippleColor(ContextCompat.getColor(getContext(), R.color.white40));
        fab.setImageResource(R.drawable.ic_add_white_24dp);
        addView(fab);
        RelativeLayout.LayoutParams fabParams = (LayoutParams) fab.getLayoutParams();
        fabParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        fabParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        fabParams.rightMargin = fabRightMargin;
        fabParams.bottomMargin = fabBottomMargin;

        // init bottom menu
        bottomMenu = new LinearLayout(getContext());
        bottomMenu.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        bottomMenu.setOrientation(LinearLayout.HORIZONTAL);
        bottomMenu.setVisibility(View.INVISIBLE);
        addView(bottomMenu);
        RelativeLayout.LayoutParams params = (LayoutParams) bottomMenu.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.height = fabMenuHeight;
        params.width = LayoutParams.MATCH_PARENT;

        fab.setOnClickListener(this);
    }

    /**
     * when scroll start, must collapse fabMenu and then appear {@link #fab}
     *
     * @param view
     */
    public void addScrollView(RecyclerView view) {
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                Log.i("hatti.view.scroll", newState + "");
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && isExpanded) {
                    handleActionButton(true, fabRightMargin, fabBottomMargin, fab.getRight(), fab.getLeft() + 3 * fab.getWidth(), fabBottomMargin, DURATION);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                Log.d("hatti.view.scroll", dy + "");
                super.onScrolled(recyclerView, dx, dy);
                if (dy == -0 || dy == 0) {
                    return;
                }

            }
        });
    }

    /**
     *
     * @param list list of {@link Pair} that contains menu's name and menu's image resource
     */
    public void addMenuList(ArrayList<Pair<String, Integer>> list) {
        this.mList.clear();
        this.mList.addAll(list);

        bottomMenu.removeAllViews();


        for (Pair menu : list) {
            int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};

            // Obtain the styled attributes. 'themedContext' is a context with a
            // theme, typically the current Activity (i.e. 'this')
            TypedArray ta = getContext().obtainStyledAttributes(attrs);

            // Now get the value of the 'listItemBackground' attribute that was
            // set in the theme used in 'themedContext'. The parameter is the index
            // of the attribute in the 'attrs' array. The returned Drawable
            // is what you are after
            Drawable drawableFromTheme = ta.getDrawable(0 /* index */);

            // Finally free resources used by TypedArray
            ta.recycle();

            AppCompatImageButton btn = (AppCompatImageButton) LayoutInflater.from(getContext()).inflate(R.layout.item_menu, null);

            bottomMenu.addView(btn);
//            btn.setId(View.generateViewId());
            btn.setTag(menu.first);
            Log.d("hatti.view.id", "@generated.. " + menu.first);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btn.getLayoutParams();
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            params.weight = 1;
            btn.setBackground(drawableFromTheme);
            btn.setImageResource((Integer) menu.second);
            btn.setOnClickListener(this);
        }
    }

    public void setOnClickListener(OnClickListener l) {
        this.mOnClickListener = l;
    }

    private void handleActionButton(final boolean expanded, final int marginLeft, final int marginTop, final int fromX, final int toX, final int dist, int durationMilli) {

        if (!expanded) {
            expandMenu(marginLeft, marginTop, fromX, toX, dist, durationMilli);
        } else {
            collapseMenu(marginLeft, marginTop, fromX, toX, dist, durationMilli);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == fab.getId()) {
            if (isExpanded) {
                handleActionButton(true, fabRightMargin, fabBottomMargin, ViewUtils.getScreenWidth(getContext()) /2 - fab.getWidth(), ViewUtils.getScreenWidth(getContext()) - ViewUtils.dp2px(getContext(), 16), fabBottomMargin, DURATION);
            } else {
                handleActionButton(false, fabRightMargin, fabBottomMargin, fab.getLeft(), fab.getLeft() - 2 * fab.getHeight(), fabBottomMargin, DURATION);
            }
            return;
        } else {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
        }
    }

    private void expandMenu(final int marginRight, final int marginBottom, final int fromX, final int toX, final int dist, int durationMilli) {
        Log.i("hatti.animator", "fromX : " + fromX + ", toX : " + toX);
        final float yMax = (float) logB(Math.abs(toX - fromX), 0.5), yDistMax = dist;
        float ratio = yDistMax / (float) yMax;

        Log.i("hatti.animator", "yMax : " + yMax + " yDistMax : " + dist);

        ValueAnimator anim = ValueAnimator.ofInt(fromX, toX);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                Log.i("hatti.animator", "val : " + val);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();
                int delta = fromX - val;
                int bottomMargin = 0, rightMargin = 0;

                if (delta == 0) {
                    bottomMargin = marginBottom;
                    rightMargin = marginRight;
                } else {
                    rightMargin = delta;
                    float tmpLog = logB(delta, 0.5) == Integer.MAX_VALUE ? 0f : (float) logB(delta, 0.5);
                    bottomMargin = (int) ((int) ((tmpLog * yDistMax) / yMax) + yMax);
                    bottomMargin = bottomMargin < 0 ? marginBottom : bottomMargin + marginBottom;
                    bottomMargin = bottomMargin == Integer.MAX_VALUE ? marginBottom : bottomMargin;
                    rightMargin = rightMargin < 0 ? marginRight : rightMargin + marginRight;
                }
                Log.d("FloatingActionButton", "@anim.. bottomMargin : " + bottomMargin);
                layoutParams.bottomMargin = 104 - bottomMargin;
                layoutParams.rightMargin = ViewUtils.dp2px(getContext(), 16) + delta;
                fab.setLayoutParams(layoutParams);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isExpanded = !isExpanded;
                int cx = bottomMenu.getMeasuredWidth() / 2;
                int cy = bottomMenu.getMeasuredHeight() / 2;

                // get the initial radius for the clipping circle
                int initialRadius = bottomMenu.getWidth() / 2;
                int finalRadius = Math.max(bottomMenu.getWidth(), bottomMenu.getHeight()) / 2;

//                Animator anim = ViewAnimationUtils.createCircularReveal(bottomMenu, (fab.getLeft() + fab.getRight()) / 2, cy, 0, finalRadius);
                Animator anim = ViewAnimationUtils.createCircularReveal(bottomMenu, ViewUtils.getScreenWidth(getContext()) / 2, cy, 0, finalRadius);

                fab.setVisibility(View.INVISIBLE);
                bottomMenu.setVisibility(View.VISIBLE);
                anim.setDuration(150);
                anim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(durationMilli);
        anim.start();
        ValueAnimator a = ValueAnimator.ofInt(fab.getLeft(), fab.getLeft() - fab.getWidth());

        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
                params.rightMargin = ViewUtils.dp2px(getContext(), 16) + (fromX - value);
                params.bottomMargin *= value / (float) (fromX - toX);

                fab.setLayoutParams(params);
            }
        });
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isExpanded = !isExpanded;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        a.setDuration(durationMilli);
//        a.start();
    }

    private void collapseMenu(final int marginRight, final int marginBottom, final int fromX, final int toX, final int dist, final int durationMilli) {

        Log.i("hatti.animator", "fromX : " + fromX + ", toX : " + toX);
        final float yMax = (float) logB(Math.abs(toX - fromX), 0.5), yDistMax = dist;
        float ratio = yDistMax / (float) yMax;

        Log.i("hatti.animator", "yMax : " + yMax + " yDistMax : " + dist);

        isExpanded = !isExpanded;
        int cx = bottomMenu.getMeasuredWidth() / 2;
        int cy = bottomMenu.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = bottomMenu.getWidth() / 2;
        int finalRadius = Math.max(bottomMenu.getWidth(), bottomMenu.getHeight()) / 2;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
        final int curMargin = params.rightMargin;

//        Animator animReveal = ViewAnimationUtils.createCircularReveal(bottomMenu, (fab.getLeft() + (fab.getWidth() / 2)), cy, finalRadius, 0);
        Animator animReveal = ViewAnimationUtils.createCircularReveal(bottomMenu, (ViewUtils.getScreenWidth(getContext()) / 2), cy, finalRadius, 0);
        animReveal.setDuration(150);
        animReveal.start();
        animReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bottomMenu.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ValueAnimator anim = ValueAnimator.ofInt(fromX, toX);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                Log.i("hatti.animator", "val : " + val);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();
                int delta = toX - val;
                int bottomMargin = 0, rightMargin = 0;

                if (delta == 0) {
                    bottomMargin = marginBottom;
                    rightMargin = marginRight;
                } else {
                    bottomMargin = (int) (Math.log(delta) / 10f * dist);
                    rightMargin = delta;
                    float tmpLog = logB(delta, 0.5) == Integer.MAX_VALUE ? 0f : (float) logB(delta, 0.5);
                    bottomMargin = (int) ((int) ((tmpLog * yDistMax) / yMax) + yMax);

                    // 보정
                    bottomMargin = bottomMargin < 0 ? marginBottom : bottomMargin + marginBottom;
                    bottomMargin = bottomMargin == Integer.MAX_VALUE ? marginBottom : bottomMargin;
                    rightMargin = rightMargin < 0 ? marginRight : rightMargin + marginRight;
                }
                layoutParams.bottomMargin = 104 - bottomMargin;
                layoutParams.rightMargin = delta + ViewUtils.dp2px(getContext(), 16);
                fab.setLayoutParams(layoutParams);
                Log.d("hatti.animator", "val : " + val + " delta : " + delta + " y : " + layoutParams.bottomMargin + " , x : " + layoutParams.rightMargin + " , " + ((int) Math.pow(Math.E, delta)));
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(durationMilli);
        anim.setStartDelay(80);
        anim.start();
    }

    public static double logB(double x, double base) {
        return Math.log(x) / Math.log(base);
    }

    public int dip2px(Context context, int dip) {
        int px = 0;
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return px;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public void collapse() {
        handleActionButton(true, fabRightMargin, fabBottomMargin, fab.getRight(), fab.getLeft() + 3 * fab.getWidth(), fabBottomMargin, DURATION);
    }
}