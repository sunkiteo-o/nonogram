package com.example.nonograms;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int life = 3; // 초기 생명 값
    private Cell[][] buttons = new Cell[5][5];
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TableLayout을 찾아서 참조
        tableLayout = findViewById(R.id.tableLayout);
        ToggleButton toggleButton = findViewById(R.id.toggleButton); // ToggleButton 추가

        // TextView 배열 선언
        TextView[][] rowTextViews = new TextView[8][3]; // 행 힌트용 TextView
        TextView[][] colTextViews = new TextView[5][3]; // 열 힌트용 TextView

        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            TableRow tableRow = new TableRow(this);
            tableLayout.addView(tableRow);

            for (int j = 0; j < 8; j++) {
                if (j < 3) {
                    // 행의 텍스트뷰 추가
                    TextView textView = new TextView(this);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText((i >= 3) ? "0" : "");

                    // 행 텍스트뷰 배열에 저장
                    if (i >= 3) {
                        rowTextViews[i - 3][j] = textView;
                    }

                    // 크기 및 레이아웃 설정
                    TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(100, 100);
                    textViewParams.setMargins(5, 5, 5, 5);
                    textView.setLayoutParams(textViewParams);

                    // TableRow에 추가
                    tableRow.addView(textView);

                } else {
                    if (i < 3) {
                        // 열의 텍스트뷰 추가
                        TextView textView = new TextView(this);
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("0"); // 초기 텍스트는 빈 값
                        colTextViews[j-3][i] = textView; // 열 텍스트뷰 배열에 저장

                        // 크기 및 레이아웃 설정
                        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(100, 100);
                        textViewParams.setMargins(5, 5, 5, 5);
                        textView.setLayoutParams(textViewParams);

                        // TableRow에 추가
                        tableRow.addView(textView);

                    } else {
                        // Cell 추가
                        Cell cell = new Cell(this, random.nextBoolean()); // 랜덤으로 검정 칸 여부 설정
                        buttons[i - 3][j - 3] = cell; // Cell 배열에 저장

                        // 크기 및 레이아웃 설정
                        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(100, 100);
                        cellParams.setMargins(5, 5, 5, 5);
                        cell.setLayoutParams(cellParams);

                        // TableRow에 추가
                        tableRow.addView(cell);

                        // 클릭 리스너 설정
                        cell.setOnClickListener(v -> {
                            if (toggleButton.isChecked()) {
                                cell.toggleX();
                            } else {
                                boolean correct = cell.markBlackSquare();
                                if (!correct) {
                                    life--;
                                    updateLifeDisplay();
                                    if (life == 0) {
                                        endGame(false);
                                    }
                                } else if (Cell.getNumBlackSquares() == 0) {
                                    endGame(true);
                                }
                            }
                        });
                    }
                }
            }
        }

        // 행, 열 힌트 계산
        countBInRowAndColumn(buttons, rowTextViews, colTextViews);
    }

    private void updateHintsForRow(StringBuilder hint, TextView[] rowHintViews) {
        if (rowHintViews == null) {
            Log.e("UpdateHints", "Error: rowHintViews is null");
            return; // null인 경우 더 이상 진행하지 않음
        }

        String[] hints = hint.toString().split(" ");
        int index = 0;
        for (String h : hints) {
            if (index < 3) {
                rowHintViews[index].setText(h);
                index++;
            }
        }
        while (index < 3) {
            rowHintViews[index].setText("0");
            index++;
        }
    }

    private void updateHintsForColumn(StringBuilder hint, TextView[] colHintViews) {
        if (colHintViews == null) {
            Log.e("UpdateHints", "Error: colHintViews is null");
            return; // null인 경우 더 이상 진행하지 않음
        }

        String[] hints = hint.toString().split(" ");
        int index = 0;
        for (String h : hints) {
            if (index < 3) {
                colHintViews[index].setText(h);
                index++;
            }
        }
        while (index < 3) {
            colHintViews[index].setText("0");
            index++;
        }
    }

    private void countBInRowAndColumn(Cell[][] cells, TextView[][] rowTextViews, TextView[][] colTextViews) {
        Log.e("countBInRowAndColumn", "countBInRowAndColumn!");
        // 행 힌트 계산
        for (int i = 0; i < 5; i++) {
            StringBuilder rowHint = new StringBuilder();
            int count = 0;

            for (int j = 0; j < 5; j++) {
                if (cells[i][j].isBlackSquare()) {
                    count++;
                } else if (count > 0) {
                    rowHint.append(count).append(" ");
                    count = 0;
                }
            }
            if (count > 0) rowHint.append(count); // 마지막 그룹 처리

            // 힌트를 rowTextViews에 업데이트
            if (rowTextViews[i] != null) { // null 체크 추가
                updateHintsForRow(rowHint, rowTextViews[i]);
            }
        }

        // 열 힌트 계산
        for (int j = 0; j < 5; j++) {
            StringBuilder colHint = new StringBuilder();
            int count = 0;

            for (int i = 0; i < 5; i++) {
                if (cells[i][j].isBlackSquare()) {
                    count++;
                } else if (count > 0) {
                    colHint.append(count).append(" ");
                    count = 0;
                }
            }
            if (count > 0) colHint.append(count); // 마지막 그룹 처리

            // 힌트를 colTextViews에 업데이트
            if (colTextViews[j] != null) { // null 체크 추가
                updateHintsForColumn(colHint, colTextViews[j]);
            }
        }
    }

    private void updateLifeDisplay() {
        // 생명 수를 표시할 TextView를 찾습니다.
        TextView lifeTextView = findViewById(R.id.lifeTextView);

        // TextView에 현재 생명 수를 업데이트
        lifeTextView.setText("Life: " + life);
    }


    private void endGame(boolean won) {
        String message = won ? "You Win!" : "Game Over!";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // 모든 버튼 클릭 비활성화
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                row.getChildAt(j).setClickable(false);
            }
        }
    }
}
