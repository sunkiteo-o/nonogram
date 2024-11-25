package com.example.nonograms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.TableRow;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

@SuppressLint("ViewConstructor")
public class Cell extends AppCompatButton {
    private boolean blackSquare;  // 검정색 칸 여부
    private boolean checked;  // "X" 표시 여부
    private static int numBlackSquares = 0;

    public Cell(@NonNull Context context, boolean isBlackSquare) {
        super(context);
        setLayoutParams(new TableRow.LayoutParams(150, 150));  // 크기 설정
        setBackgroundResource(R.drawable.cell_selector);  // 셀 배경 설정

        this.blackSquare = isBlackSquare;  // 랜덤으로 검정색 칸 여부 설정
        if (blackSquare) {
            numBlackSquares++;  // 검정색 칸 수 증가
            setBackgroundColor(Color.BLACK);  // 검정색으로 설정
        }
    }

    public boolean isBlackSquare() {
        return blackSquare;
    }

    public static int getNumBlackSquares() {
        return numBlackSquares;
    }

    public boolean markBlackSquare() {
        if (checked) return true;

        if (blackSquare) {
            setBackgroundColor(Color.BLACK);  // 검정색으로 설정
            setClickable(false);  // 클릭 비활성화
            numBlackSquares--;  // 검정색 칸 수 감소
            return true;
        } else {
            setText("X");  // 빈 칸에 "X" 표시
            return false;  // 오답 클릭
        }
    }

    public boolean toggleX() {
        checked = !checked;  // X 표시 여부 토글
        setText(checked ? "X" : "");  // "X" 표시 또는 빈칸으로 설정
        return checked;
    }
}
