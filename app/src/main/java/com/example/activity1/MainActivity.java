package com.example.activity1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton[] buttons = new ImageButton[9];
    Button restartBtn;
    TextView timeTv;

    private final int ROWS = 3;
    private final int COLS = 3;
    private final int TOTAL = ROWS * COLS;

    private int[] imageResIds = {
            R.mipmap.img_00x00, R.mipmap.img_00x01, R.mipmap.img_00x02,
            R.mipmap.img_01x00, R.mipmap.img_01x01, R.mipmap.img_01x02,
            R.mipmap.img_02x00, R.mipmap.img_02x01, R.mipmap.img_02x02
    };

    private int[] imageIndex = new int[TOTAL];
    private int blankIndex = TOTAL - 1;
    private int time = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                time++;
                timeTv.setText("时间: " + time + " 秒");
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initView();
        shuffleImages();
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    private void initView() {
        buttons[0] = findViewById(R.id.pt_ib_00x00);
        buttons[1] = findViewById(R.id.pt_ib_00x01);
        buttons[2] = findViewById(R.id.pt_ib_00x02);
        buttons[3] = findViewById(R.id.pt_ib_01x00);
        buttons[4] = findViewById(R.id.pt_ib_01x01);
        buttons[5] = findViewById(R.id.pt_ib_01x02);
        buttons[6] = findViewById(R.id.pt_ib_02x00);
        buttons[7] = findViewById(R.id.pt_ib_02x01);
        buttons[8] = findViewById(R.id.pt_ib_02x02);

        for (int i = 0; i < TOTAL; i++) {
            final int index = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    move(index);
                }
            });
        }

        timeTv = findViewById(R.id.pt_tv_time);
        restartBtn = findViewById(R.id.pt_btn_restart);
    }

    private void move(int site) {
        int sitex = site / COLS;
        int sitey = site % COLS;
        int blankx = blankIndex / COLS;
        int blanky = blankIndex % COLS;

        int dx = Math.abs(sitex - blankx);
        int dy = Math.abs(sitey - blanky);

        if ((dx == 0 && dy == 1) || (dx == 1 && dy == 0)) {
            buttons[blankIndex].setImageResource(imageResIds[imageIndex[site]]);
            buttons[blankIndex].setVisibility(View.VISIBLE);
            buttons[site].setVisibility(View.INVISIBLE);

            swap(site, blankIndex);
            blankIndex = site;

            if (isPuzzleSolved()) {
                handler.removeMessages(1);
                for (ImageButton btn : buttons) {
                    btn.setVisibility(View.VISIBLE);
                    btn.setEnabled(false);
                }
                timeTv.setText("完成! 总用时: " + time + " 秒");
            }
        }
    }

    private boolean isPuzzleSolved() {
        for (int i = 0; i < TOTAL; i++) {
            if (imageIndex[i] != i) return false;
        }
        return true;
    }

    private void swap(int i, int j) {
        int temp = imageIndex[i];
        imageIndex[i] = imageIndex[j];
        imageIndex[j] = temp;
    }

    public void restart(View view) {
        handler.removeMessages(1);
        time = 0;
        timeTv.setText("时间: " + time + " 秒");
        shuffleImages();
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    private void shuffleImages() {
        for (int i = 0; i < TOTAL; i++) {
            imageIndex[i] = i;
        }

        for (int i = TOTAL - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            swap(i, j);
        }

        for (int i = 0; i < TOTAL; i++) {
            buttons[i].setImageResource(imageResIds[imageIndex[i]]);
            buttons[i].setVisibility(View.VISIBLE);
            buttons[i].setEnabled(true);
        }

        blankIndex = TOTAL - 1;
        buttons[blankIndex].setVisibility(View.INVISIBLE);
    }
}
