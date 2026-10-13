

package com.carassistant.next;

import android.hardware.display.DisplayManager;
import android.os.Build;
import android.view.Display;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public final class LauncherShellView extends LinearLayout {

    private static final int BG = Color.rgb(17, 23, 31);
    private static final int PANEL = Color.rgb(29, 39, 51);
    private static final int CARD = Color.rgb(37, 49, 64);
    private static final int ACCENT = Color.rgb(66, 145, 255);
    private static final int WHITE = Color.WHITE;
    private static final int MUTED = Color.rgb(175, 188, 202);

    private static final String PREFS = "carassistant_launcher";
    private static final String KEY_MAP_PACKAGE = "default_map_package";
    private static final String KEY_MAP_LABEL = "default_map_label";

    private final SharedPreferences prefs;
    private final FrameLayout contentFrame;

    private TextView selectedMapText;
    private LinearLayout homePage;

    public LauncherShellView(Context context) {
        super(context);

        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        setOrientation(HORIZONTAL);
        setBackgroundColor(BG);

        buildLeftBar();

        LinearLayout right = new LinearLayout(context);
        right.setOrientation(VERTICAL);
        addView(right, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f));

        contentFrame = new FrameLayout(context);
        right.addView(contentFrame,
                new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

        buildHomePage();
        showHome();

        LinearLayout bottom = new LinearLayout(context);
        bottom.setGravity(Gravity.CENTER);
        bottom.setBackgroundColor(PANEL);
        bottom.setPadding(dp(4), dp(4), dp(4), dp(4));

        right.addView(bottom,
                new LayoutParams(LayoutParams.MATCH_PARENT, dp(48)));

        addBottomItem(bottom, "Trang chủ", () -> showHome());
        addBottomItem(bottom, "Ứng dụng", () -> showAppsPage());
        addBottomItem(bottom, "Trợ lý", () -> showPlaceholder(
                "Trợ lý giọng nói",
                "Sẽ phát triển ở giai đoạn sau."));
    }

    private void buildLeftBar() {
        LinearLayout bar = new LinearLayout(getContext());
        bar.setOrientation(VERTICAL);
        bar.setGravity(Gravity.CENTER_HORIZONTAL);
        bar.setPadding(dp(4), dp(8), dp(4), dp(8));
        bar.setBackgroundColor(PANEL);

        addView(bar, new LayoutParams(dp(68), LayoutParams.MATCH_PARENT));

        addTool(bar, android.R.drawable.ic_menu_view,
                "Home", this::showHome);

        addTool(bar, android.R.drawable.ic_menu_sort_by_size,
                "Apps", this::showAppsPage);

        addTool(bar, android.R.drawable.ic_btn_speak_now,
                "Voice", () -> showPlaceholder(
                        "Trợ lý giọng nói",
                        "Chức năng trợ lý sẽ được phát triển sau."));

        addTool(bar, android.R.drawable.ic_media_previous,
                "Back", this::showHome);

        View spacer = new View(getContext());
        bar.addView(spacer,
                new LayoutParams(1, 0, 1f));

       
        addTool(bar, android.R.drawable.ic_menu_preferences,
                "Settings", this::showProjectionDiagnostics);

    }

    private void addTool(
            LinearLayout parent,
            int iconResource,
            String label,
            Runnable action) {

        LinearLayout item = new LinearLayout(getContext());
        item.setOrientation(VERTICAL);
        item.setGravity(Gravity.CENTER);
        item.setBackground(makeBackground(CARD, 12));
        item.setClickable(true);
        item.setFocusable(true);
        item.setContentDescription(label);
        item.setOnClickListener(v -> action.run());

        ImageView icon = new ImageView(getContext());
        icon.setImageResource(iconResource);
        icon.setColorFilter(WHITE);

        item.addView(icon, new LayoutParams(dp(25), dp(25)));

        TextView caption = new TextView(getContext());
        caption.setText(label);
        caption.setTextSize(10);
        caption.setTextColor(MUTED);
        caption.setGravity(Gravity.CENTER);

        item.addView(caption);

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, dp(58));
        params.bottomMargin = dp(7);
        parent.addView(item, params);
    }

    private void buildHomePage() {
        homePage = new LinearLayout(getContext());
        homePage.setOrientation(VERTICAL);
        homePage.setPadding(dp(12), dp(12), dp(12), dp(10));
        homePage.setBackgroundColor(BG);

        TextView heading = new TextView(getContext());
        heading.setText("CarAssistantNext");
        heading.setTextColor(WHITE);
        heading.setTextSize(20);
        heading.setTypeface(null, Typeface.BOLD);
        homePage.addView(heading);

        TextView mapHeading = new TextView(getContext());
        mapHeading.setText("BẢN ĐỒ");
        mapHeading.setTextColor(MUTED);
        mapHeading.setTextSize(11);

        LayoutParams headingParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        headingParams.topMargin = dp(8);
        homePage.addView(mapHeading, headingParams);

        FrameLayout mapPanel = new FrameLayout(getContext());
        mapPanel.setBackground(makeBackground(CARD, 18));

        LayoutParams mapParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, 0, 1f);
        mapParams.topMargin = dp(6);
        mapParams.bottomMargin = dp(10);
        homePage.addView(mapPanel, mapParams);

        LinearLayout mapCenter = new LinearLayout(getContext());
        mapCenter.setOrientation(VERTICAL);
        mapCenter.setGravity(Gravity.CENTER);
        mapCenter.setPadding(dp(12), dp(12), dp(12), dp(12));

        mapPanel.addView(mapCenter, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ImageView mapIcon = new ImageView(getContext());
        mapIcon.setImageResource(android.R.drawable.ic_dialog_map);
        mapIcon.setColorFilter(ACCENT);
        mapCenter.addView(mapIcon, new LayoutParams(dp(44), dp(44)));

        TextView mapMessage = new TextView(getContext());
        mapMessage.setText("Khu vực bản đồ");
        mapMessage.setTextColor(WHITE);
        mapMessage.setTextSize(18);
        mapMessage.setTypeface(null, Typeface.BOLD);
        mapMessage.setGravity(Gravity.CENTER);

        LayoutParams messageParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        messageParams.topMargin = dp(8);
        mapCenter.addView(mapMessage, messageParams);

        selectedMapText = new TextView(getContext());
        selectedMapText.setTextColor(MUTED);
        selectedMapText.setTextSize(13);
        selectedMapText.setGravity(Gravity.CENTER);

        LayoutParams selectedParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        selectedParams.topMargin = dp(5);
        mapCenter.addView(selectedMapText, selectedParams);

        Button chooseMap = makeButton("Chọn ứng dụng bản đồ");
        LayoutParams chooseParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, dp(44));
        chooseParams.topMargin = dp(14);
        mapCenter.addView(chooseMap, chooseParams);
        chooseMap.setOnClickListener(v -> showMapPicker());

        Button openMap = makeButton("Mở bản đồ");
        LayoutParams openParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, dp(42));
        openParams.topMargin = dp(7);
        mapCenter.addView(openMap, openParams);
        openMap.setOnClickListener(v -> openSelectedMap());

        LinearLayout utilityRow = new LinearLayout(getContext());
        utilityRow.setOrientation(LinearLayout.HORIZONTAL);
        utilityRow.setGravity(Gravity.CENTER_VERTICAL);

        homePage.addView(utilityRow,
                new LayoutParams(LayoutParams.MATCH_PARENT, dp(116)));

        buildTrafficCard(utilityRow);
        buildMediaCard(utilityRow);
    }

    private void buildTrafficCard(LinearLayout parent) {
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setBackground(makeBackground(PANEL, 16));
        card.setPadding(dp(5), dp(6), dp(5), dp(6));

        parent.addView(card, new LayoutParams(0,
                LayoutParams.MATCH_PARENT, 0.85f));

        ImageView icon = new ImageView(getContext());
        icon.setImageResource(android.R.drawable.ic_dialog_alert);
        icon.setColorFilter(0xffffc45c);
        card.addView(icon, new LayoutParams(dp(30), dp(30)));

        TextView title = new TextView(getContext());
        title.setText("Cảnh báo");
        title.setTextColor(WHITE);
        title.setTextSize(12);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        card.addView(title);

        TextView status = new TextView(getContext());
        status.setText("Chưa kết nối");
        status.setTextColor(MUTED);
        status.setTextSize(10);
        status.setGravity(Gravity.CENTER);
        card.addView(status);
    }

    private void buildMediaCard(LinearLayout parent) {
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(VERTICAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setBackground(makeBackground(PANEL, 16));
        card.setPadding(dp(10), dp(7), dp(10), dp(7));

        LayoutParams params = new LayoutParams(0,
                LayoutParams.MATCH_PARENT, 1.5f);
        params.leftMargin = dp(8);
        parent.addView(card, params);

        LinearLayout top = new LinearLayout(getContext());
        top.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(top);

        ImageView icon = new ImageView(getContext());
        icon.setImageResource(android.R.drawable.ic_media_play);
        icon.setColorFilter(ACCENT);
        top.addView(icon, new LayoutParams(dp(26), dp(26)));

        TextView title = new TextView(getContext());
        title.setText("Media");
        title.setTextColor(WHITE);
        title.setTextSize(14);
        title.setTypeface(null, Typeface.BOLD);
        LayoutParams titleParams = new LayoutParams(0,
                LayoutParams.WRAP_CONTENT, 1f);
        titleParams.leftMargin = dp(8);
        top.addView(title, titleParams);

        TextView info = new TextView(getContext());
        info.setText("Chưa kết nối phiên phát");
        info.setTextColor(MUTED);
        info.setTextSize(11);

        LayoutParams infoParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        infoParams.topMargin = dp(4);
        card.addView(info, infoParams);

        LinearLayout controls = new LinearLayout(getContext());
        controls.setGravity(Gravity.CENTER);
        LayoutParams controlParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, dp(34));
        controlParams.topMargin = dp(4);
        card.addView(controls, controlParams);

        addMediaIcon(controls, android.R.drawable.ic_media_previous);
        addMediaIcon(controls, android.R.drawable.ic_media_play);
        addMediaIcon(controls, android.R.drawable.ic_media_next);
    }

    private void addMediaIcon(LinearLayout parent, int resource) {
        ImageView icon = new ImageView(getContext());
        icon.setImageResource(resource);
        icon.setColorFilter(MUTED);
        icon.setAlpha(0.5f);
        icon.setEnabled(false);
        icon.setContentDescription("Điều khiển Media sẽ được tích hợp sau");

        LayoutParams params = new LayoutParams(dp(32), dp(30));
        params.setMargins(dp(5), 0, dp(5), 0);
        parent.addView(icon, params);
    }

    private void showHome() {
        contentFrame.removeAllViews();
        contentFrame.addView(homePage, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        refreshSelectedMapLabel();
    }
       
private void showProjectionDiagnostics() {
    PackageManager pm = getContext().getPackageManager();

    boolean supported = pm.hasSystemFeature(
            PackageManager.FEATURE_ACTIVITIES_ON_SECONDARY_DISPLAYS);

    DisplayManager dm = (DisplayManager) getContext()
            .getSystemService(Context.DISPLAY_SERVICE);

    Display[] displays = dm == null
            ? new Display[0]
            : dm.getDisplays();

    StringBuilder report = new StringBuilder();

    report.append("THIẾT BỊ\n");
    report.append(Build.MANUFACTURER)
            .append(" ")
            .append(Build.MODEL)
            .append("\n");

    report.append("Android: ")
            .append(Build.VERSION.RELEASE)
            .append(" (API ")
            .append(Build.VERSION.SDK_INT)
            .append(")\n\n");

    report.append("HỖ TRỢ ACTIVITY TRÊN MÀN HÌNH PHỤ\n");
    report.append(supported ? "CÓ" : "KHÔNG")
            .append("\n\n");

    report.append("MÀN HÌNH HỆ THỐNG NHẬN DIỆN\n");

    if (displays.length == 0) {
        report.append("Không tìm thấy màn hình.\n");
    } else {
        for (Display display : displays) {
            report.append("\nID: ")
                    .append(display.getDisplayId())
                    .append("\nTên: ")
                    .append(display.getName())
                    .append("\nTrạng thái: ")
                    .append(display.getState())
                    .append("\nFlags: ")
                    .append(display.getFlags())
                    .append("\n");
        }
    }

    report.append("\nLưu ý: kết quả này chưa xác nhận khả năng ");
    report.append("chiếu ứng dụng bản đồ vào vùng Home.");
    report.append("\nQuyền root chưa được kiểm tra bởi màn hình này.");

    TextView text = new TextView(getContext());
    text.setText(report.toString());
    text.setTextColor(WHITE);
    text.setTextSize(14);
    text.setPadding(dp(16), dp(12), dp(16), dp(12));

    ScrollView scroll = new ScrollView(getContext());
    scroll.addView(text);

    new AlertDialog.Builder(getContext())
            .setTitle("Chẩn đoán chiếu ứng dụng")
            .setView(scroll)
            .setPositiveButton("Đóng", null)
            .show();
}

    private void showAppsPage() {
        showPlaceholder("Ứng dụng",
                "Khung quản lý ứng dụng sẽ được phát triển sau.");
    }

    private void showPlaceholder(String title, String message) {
        contentFrame.removeAllViews();

        LinearLayout page = new LinearLayout(getContext());
        page.setOrientation(VERTICAL);
        page.setGravity(Gravity.CENTER);
        page.setPadding(dp(20), dp(20), dp(20), dp(20));

        TextView titleView = new TextView(getContext());
        titleView.setText(title);
        titleView.setTextColor(WHITE);
        titleView.setTextSize(22);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setGravity(Gravity.CENTER);
        page.addView(titleView);

        TextView messageView = new TextView(getContext());
        messageView.setText(message);
        messageView.setTextColor(MUTED);
        messageView.setTextSize(14);
        messageView.setGravity(Gravity.CENTER);

        LayoutParams messageParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        messageParams.topMargin = dp(12);
        page.addView(messageView, messageParams);

        contentFrame.addView(page, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void showMapPicker() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="));

        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> handlers =
                pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        ArrayList<ResolveInfo> maps = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        String currentPackage = prefs.getString(KEY_MAP_PACKAGE, "");

        for (ResolveInfo info : handlers) {
            String packageName = info.activityInfo.packageName;

            if (packageName.equals(getContext().getPackageName())) {
                continue;
            }

            if (packageName.equals(currentPackage)) {
                // Keep the currently selected app available if it is installed.
            }

            maps.add(info);
            labels.add(info.loadLabel(pm).toString());
        }

        if (maps.isEmpty()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Ứng dụng bản đồ")
                    .setMessage("Không tìm thấy ứng dụng xử lý bản đồ. "
                            + "Hãy cài ứng dụng bản đồ rồi thử lại.")
                    .setPositiveButton("Đóng", null)
                    .show();
            return;
        }

        int checked = -1;
        String[] items = labels.toArray(new String[0]);

        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).activityInfo.packageName.equals(currentPackage)) {
                checked = i;
                break;
            }
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Chọn bản đồ mặc định")
                .setSingleChoiceItems(items, checked, (dialog, which) -> {
                    ResolveInfo selected = maps.get(which);
                    String label = labels.get(which);
                    String packageName = selected.activityInfo.packageName;

                    prefs.edit()
                            .putString(KEY_MAP_PACKAGE, packageName)
                            .putString(KEY_MAP_LABEL, label)
                            .apply();

                    refreshSelectedMapLabel();
                    dialog.dismiss();
                    Toast.makeText(getContext(),
                            "Đã chọn " + label,
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void refreshSelectedMapLabel() {
        if (selectedMapText == null) {
            return;
        }

        String label = prefs.getString(KEY_MAP_LABEL, "");
        if (label.isEmpty()) {
            selectedMapText.setText(
                    "Chưa chọn ứng dụng bản đồ mặc định");
        } else {
            selectedMapText.setText("Mặc định: " + label);
        }
    }

    private void openSelectedMap() {
        String packageName = prefs.getString(KEY_MAP_PACKAGE, "");

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="));

        if (!packageName.isEmpty()) {
            intent.setPackage(packageName);
        }

        try {
            getContext().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "Không mở được ứng dụng bản đồ. Hãy chọn lại.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void addBottomItem(
            LinearLayout parent, String label, Runnable action) {

        TextView item = new TextView(getContext());
        item.setText(label);
        item.setTextColor(WHITE);
        item.setTextSize(12);
        item.setGravity(Gravity.CENTER);
        item.setClickable(true);
        item.setFocusable(true);
        item.setOnClickListener(v -> action.run());

        parent.addView(item, new LayoutParams(0,
                LayoutParams.MATCH_PARENT, 1f));
    }

    private Button makeButton(String text) {
        Button button = new Button(getContext());
        button.setText(text);
        button.setTextColor(WHITE);
        button.setTextSize(12);
        button.setAllCaps(false);
        button.setBackground(makeBackground(ACCENT, 12));
        return button;
    }

    private GradientDrawable makeBackground(int color, int radiusDp) {
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
