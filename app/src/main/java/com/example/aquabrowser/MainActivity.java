package com.example.aquabrowser;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public TextView searchText;

    private ImageButton homeBtn;
    private ImageButton nextBtn;
    private ImageButton prevBtn;
    private ImageButton closeBtn;
    private ImageButton reloadBtn;

    private TabLayout tabLayout;

    private LinearLayout layout;

    private ArrayList<WebView> webViews;

    private final String HOME_URL = "https://google.com";

    private int currentWebView;

    @SuppressLint({"SetJavaScriptEnabled", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabLayout = findViewById(R.id.tabLayout);

        searchText = findViewById(R.id.editText);

        homeBtn = findViewById(R.id.homeButton);
        nextBtn = findViewById(R.id.nextButton);
        prevBtn = findViewById(R.id.previousButton);
        closeBtn = findViewById(R.id.closeButton);
        reloadBtn = findViewById(R.id.reloadButton);

        layout = findViewById(R.id.webViewLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Необязательно, но можно добавить здесь дополнительную логику
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Необязательно, но можно добавить здесь дополнительную логику
            }
        });

        webViews = new ArrayList<>();

        addTab(HOME_URL);
        showHideButtons(homeBtn);
    }

    private void showTab(int id) {
        currentWebView = id;
        updatePages();
    }

    private void addTab(String url) {
        WebView newWeb = new WebView(this);

        layout.addView(newWeb);

        newWeb.getSettings().setJavaScriptEnabled(true);
        newWeb.getSettings().setDomStorageEnabled(true);
        newWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                updateTabName(view);
            }
        });

        newWeb.loadUrl(url);

        webViews.add(newWeb);
        tabLayout.addTab(tabLayout.newTab().setText("Page " + webViews.indexOf(newWeb)));

        showTab(webViews.indexOf(newWeb));
    }

    private void updatePages() {
        for (WebView x : webViews)
            x.setVisibility(x == getView() ? View.VISIBLE : View.GONE);

        tabLayout.selectTab(getTab());
    }

    private void updateTabName(WebView view) {
        TabLayout.Tab currentTab = tabLayout.getTabAt(webViews.indexOf(view));
        currentTab.setText(view.getTitle());
    }

    private WebView getView() {
        return webViews.get(currentWebView);
    }

    private TabLayout.Tab getTab() {
        return tabLayout.getTabAt(currentWebView);
    }

    private static String prepareForUrl(String text) {
        String regex = "[^\\p{L}\\s]";
        text = text.replaceAll(regex, "");

        return text.replaceAll(" ", "+");
    }

    public void search(View view) {
        search(searchText.getText().toString());
    }

    private void search(String url) {
        if (!url.contains("http"))
            url = HOME_URL + "/search?q=" + prepareForUrl(url);

        addTab(url);
    }

    public void showHideButtons(View view) {
        int hidden = homeBtn.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;

        homeBtn.setVisibility(hidden);
        nextBtn.setVisibility(hidden);
        prevBtn.setVisibility(hidden);
        closeBtn.setVisibility(hidden);
        reloadBtn.setVisibility(hidden);
    }

    public void reloadPage(View view) {
        getView().reload();
    }

    public void closePage(View view) {
        WebView webView = getView();
        int currentPage = webViews.indexOf(webView);

//        if (currentPage == 0) return;

        if (webViews.size() <= 1) {
            goToHome(webView);
        } else {
            webViews.remove(webView);
            tabLayout.removeTab(getTab());

            webView.destroy();

            webView.setVisibility(View.GONE);

            if (currentPage >= webViews.size())
                currentPage = webViews.size() - 1;

            showTab(currentPage);
        }
    }

    public void goToHome(View view) {
        getView().loadUrl(HOME_URL);
    }

    public void goBack(View view) {
        getView().goBack();
    }

    public void goNext(View view) {
        getView().goForward();
    }
}