package com.example.stickeynote;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private EditText noteEdt;
    private Button increaseSizeBtn, decreaseSizeBtn, boldBtn, underlineBtn, italicBtn;
    private TextView sizeTV;
    float currentSize;
    StickyNote note = new StickyNote();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEdt = findViewById(R.id.idEdt);
        increaseSizeBtn = findViewById(R.id.idBtnincreaseSize);
        decreaseSizeBtn = findViewById(R.id.idBtnReduceSize);
        boldBtn = findViewById(R.id.idBtnBold);
        underlineBtn = findViewById(R.id.idBtnUnderLine);
        italicBtn = findViewById(R.id.idBtnIntalic);
        sizeTV = findViewById(R.id.idTVSize);

        // Set the initial text size
        currentSize = 14f;
        noteEdt.setTextSize(currentSize);
        sizeTV.setText(String.valueOf((int) currentSize));

        increaseSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyTextSize(1.1f); // Increase size by 10%
            }
        });

        decreaseSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyTextSize(0.9f); // Decrease size by 10%
            }
        });

        boldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyStyleToSelection(Typeface.BOLD);
            }
        });

        underlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyUnderlineToSelection();
            }
        });

        italicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyStyleToSelection(Typeface.ITALIC);
            }
        });
    }

    private void modifyTextSize(float factor) {
        int start = noteEdt.getSelectionStart();
        int end = noteEdt.getSelectionEnd();
        if (start < end) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(noteEdt.getText());
            spannable.setSpan(new RelativeSizeSpan(factor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            noteEdt.setText(spannable);
            Selection.setSelection(noteEdt.getText(), start, end);
        } else {
            Toast.makeText(MainActivity.this, "Please select some text to modify the size", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyStyleToSelection(int style) {
        int start = noteEdt.getSelectionStart();
        int end = noteEdt.getSelectionEnd();
        if (start < end) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(noteEdt.getText());
            StyleSpan[] spans = spannable.getSpans(start, end, StyleSpan.class);
            boolean hasStyle = false;

            for (StyleSpan span : spans) {
                if (span.getStyle() == style) {
                    spannable.removeSpan(span);
                    hasStyle = true;
                }
            }

            if (!hasStyle) {
                spannable.setSpan(new StyleSpan(style), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            noteEdt.setText(spannable);
            Selection.setSelection(noteEdt.getText(), start, end);
        } else {
            Toast.makeText(MainActivity.this, "Please select some text to apply the style", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyUnderlineToSelection() {
        int start = noteEdt.getSelectionStart();
        int end = noteEdt.getSelectionEnd();
        if (start < end) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(noteEdt.getText());
            UnderlineSpan[] spans = spannable.getSpans(start, end, UnderlineSpan.class);
            boolean hasUnderline = false;

            for (UnderlineSpan span : spans) {
                spannable.removeSpan(span);
                hasUnderline = true;
            }

            if (!hasUnderline) {
                spannable.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            noteEdt.setText(spannable);
            Selection.setSelection(noteEdt.getText(), start, end);
        } else {
            Toast.makeText(MainActivity.this, "Please select some text to apply underline", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveButton(View view) {
        note.setStick(noteEdt.getText().toString(), this);
        updateWidget();
        Toast.makeText(this, "Note has been updated....", Toast.LENGTH_SHORT).show();
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.widget_layout);
        ComponentName thisWidget = new ComponentName(this, AppWidget.class);
        remoteViews.setTextViewText(R.id.idTVWidget, noteEdt.getText().toString());
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
}
