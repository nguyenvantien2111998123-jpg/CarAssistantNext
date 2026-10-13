

package com.carassistant.next;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class LauncherShellView extends LinearLayout {

    private static final int BACKGROUND = Color.rgb(18, 24, 32);
    private static final int PANEL = Color.rgb(30, 39, 50);
    private static final int ACCENT = Color.rgb(45, 130, 230);
    private static final int TEXT = Color.WHITE;
    private static final int MUTED = Color.LTGRAY;

    private final FrameLayout contentFrame;
    private final TextView pageTitle;
    private final TextView pageStatus;
    private final LinearLayout appMenu;

    private String currentPage = "Trang chủ";

    public LauncherShellView(Context context) {
        super(context);

        setOrientation(HORIZONTAL);
        setBackgroundColor(BACKGROUND);

        LinearLayout leftBar = new LinearLayout(context);
        leftBar.setOrientation(VERTICAL);
        leftBar.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        leftBar.setPadding(dp(4), dp(8), dp(4), dp(8));
        leftBar.setBackgroundColor(PANEL);

        addView(leftBar, new LayoutParams(dp(68),
                LayoutParams.MATCH_PARENT));

        addTool(leftBar, "⌂", "Home", () -> showPage("Trang chủ"));
        addTool(leftBar, "▦", "Apps", () -> toggleAppMenu());
        addTool(leftBar, "●", "Voice", () -> showPage("Trợ lý"));
        addTool(leftBar, "⚙", "Settings",
                () -> showPage("Cài đặt"));

        LinearLayout rightArea = new LinearLayout(context);
        rightArea.setOrientation(VERTICAL);

        addView(rightArea, new LayoutParams(
                0, LayoutParams.MATCH_PARENT, 1f));

        contentFrame = new FrameLayout(context);
        contentFrame.setBackgroundColor(BACKGROUND);

        rightArea.addView(contentFrame, new LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 1f));

        LinearLayout center = new LinearLayout(context);
        center.setOrientation(VERTICAL);
        center.setGravity(Gravity.CENTER);
        center.setPadding(dp(16), dp(16), dp(16), dp(16));

        FrameLayout.LayoutParams centerParams =
                new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);

        contentFrame.addView(center, centerParams);

        pageTitle = new TextView(context);
        pageTitle.setTextColor(TEXT);
        pageTitle.setTextSize(25);
        pageTitle.setTypeface(null, Typeface.BOLD);
        pageTitle.setGravity(Gravity.CENTER);

        center.addView(pageTitle, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        pageStatus = new TextView(context);
        pageStatus.setTextColor(MUTED);
        pageStatus.setTextSize(15);
        pageStatus.setGravity(Gravity.CENTER);

        LayoutParams statusParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        statusParams.topMargin = dp(12);
        center.addView(pageStatus, statusParams);

        appMenu = new LinearLayout(context);
        appMenu.setOrientation(VERTICAL);
        appMenu.setPadding(dp(16), dp(16), dp(16), dp(16));
        appMenu.setBackground(makeBackground(PANEL, 16));

        FrameLayout.LayoutParams menuParams =
                new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);

        menuParams.setMargins(dp(20), dp(20), dp(20), dp(20));
        contentFrame.addView(appMenu, menuParams);
        appMenu.setVisibility(View.GONE);

        addMenuItem("Trang chủ", () -> showPage("Trang chủ"));
        addMenuItem("Ứng dụng", () -> showPage("Ứng dụng"));
        addMenuItem("Trợ lý giọng nói",
                () -> showPage("Trợ lý"));
        addMenuItem("Cài đặt", () -> showPage("Cài đặt"));

        LinearLayout bottomBar = new LinearLayout(context);
        bottomBar.setOrientation(HORIZONTAL);
        bottomBar.setGravity(Gravity.CENTER);
        bottomBar.setBackgroundColor(PANEL);
        bottomBar.setPadding(dp(4), dp(4), dp(4), dp(4));

        rightArea.addView(bottomBar, new LayoutParams(
                LayoutParams.MATCH_PARENT, dp(60)));

        addBottomTool(bottomBar, "Home",
                () -> showPage("Trang chủ"));
        addBottomTool(bottomBar, "Apps",
                () -> toggleAppMenu());
        addBottomTool(bottomBar, "Voice",
                () -> showPage("Trợ lý"));
        addBottomTool(bottomBar, "Back",
                () -> showPage("Trang chủ"));

        showPage("Trang chủ");
    }

    private void addTool(
            LinearLayout parent,
            String symbol,
            String label,
            Runnable action) {

        LinearLayout item = new LinearLayout(getContext());
        item.setOrientation(VERTICAL);
        item.setGravity(Gravity.CENTER);
        item.setBackground(makeBackground(PANEL, 12));
        item.setClickable(true);
        item.setFocusable(true);
        item.setOnClickListener(v -> action.run());

        TextView icon = new TextView(getContext());
        icon.setText(symbol);
        icon.setTextSize(24);
        icon.setTextColor(TEXT);
        icon.setGravity(Gravity.CENTER);

        item.addView(icon);

        TextView caption = new TextView(getContext());
        caption.setText(label);
        caption.setTextSize(10);
        caption.setTextColor(MUTED);
        caption.setGravity(Gravity.CENTER);

        item.addView(caption);

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, dp(58));
        params.bottomMargin = dp(6);
        parent.addView(item, params);
    }

    private void addBottomTool(
            LinearLayout parent,
            String label,
            Runnable action) {

        TextView item = new TextView(getContext());
        item.setText(label);
        item.setTextSize(12);
        item.setTextColor(TEXT);
        item.setGravity(Gravity.CENTER);
        item.setBackground(makeBackground(PANEL, 10));
        item.setClickable(true);
        item.setFocusable(true);
        item.setOnClickListener(v -> action.run());

        LayoutParams params = new LayoutParams(0,
                LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(dp(3), 0, dp(3), 0);
        parent.addView(item, params);
    }

    private void addMenuItem(String label, Runnable action) {
        TextView item = new TextView(getContext());
        item.setText(label);
        item.setTextColor(TEXT);
        item.setTextSize(16);
        item.setGravity(Gravity.CENTER_VERTICAL);
        item.setPadding(dp(12), dp(14), dp(12), dp(14));
        item.setClickable(true);
        item.setFocusable(true);
        item.setOnClickListener(v -> action.run());

        appMenu.addView(item, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void showPage(String page) {
        currentPage = page;
        appMenu.setVisibility(View.GONE);

        pageTitle.setText(page);

        if ("Trang chủ".equals(page)) {
            pageStatus.setText(
                    "CarAssistantNext\nLauncher Shell V1");
        } else if ("Ứng dụng".equals(page)) {
            pageStatus.setText(
                    "Vùng hiển thị ứng dụng\n"
                    + "Chưa tích hợp chiếu màn hình");
        } else if ("Trợ lý".equals(page)) {
            pageStatus.setText(
                    "Khu vực trợ lý giọng nói\n"
                    + "Sẽ phát triển ở giai đoạn sau");
        } else {
            pageStatus.setText(
                    "Cấu hình Launcher\n"
                    + "Chưa có thiết lập nâng cao");
        }
    }

    private void toggleAppMenu() {
        if (appMenu.getVisibility() == View.VISIBLE) {
            appMenu.setVisibility(View.GONE);
        } else {
            pageTitle.setText(currentPage);
            appMenu.setVisibility(View.VISIBLE);
            appMenu.bringToFront();
        }
    }

    private GradientDrawable makeBackground(
            int color, int radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        return drawable;
    }

    private int dp(float value) {
        return Math.round(value * getResources()
                .getDisplayMetrics().density);
    }
}
