package verificationCodeInput;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.dong.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class VerificationCodeInput extends LinearLayout implements TextWatcher, View.OnKeyListener {

    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";
    private int box = 4;
    private int boxWidth = 120;
    private int boxHeight = 120;
    private int childHPadding = 12;
    private int childVPadding = 12;
    private String inputType = TYPE_NUMBER;
    private Drawable boxBgFocus = null;
    private Drawable boxBgNormal = null;
    private afterTextChangedListener listener;

    public List<EditText> getmEditTextList() {
        return mEditTextList;
    }

    private List<EditText> mEditTextList = new ArrayList<>();
    private int currentPosition = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public VerificationCodeInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.verificationCodeInput);
        box = a.getInt(R.styleable.verificationCodeInput_box, box);

        childHPadding = (int) a.getDimension(R.styleable.verificationCodeInput_child_h_padding, childHPadding);
        childVPadding = (int) a.getDimension(R.styleable.verificationCodeInput_child_v_padding, childVPadding);
        boxBgFocus = a.getDrawable(R.styleable.verificationCodeInput_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.verificationCodeInput_box_bg_normal);
        inputType = a.getString(R.styleable.verificationCodeInput_inputType);
        boxWidth = (int) a.getDimension(R.styleable.verificationCodeInput_child_width, boxWidth);
        boxHeight = (int) a.getDimension(R.styleable.verificationCodeInput_child_height, boxHeight);
        initViews();
    }

    @SuppressLint("NewApi")
    private void initViews() {
        for (int i = 0; i < box; i++) {
            EditText editText = new EditText(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
            layoutParams.bottomMargin = childVPadding;
            layoutParams.topMargin = childVPadding;
            layoutParams.leftMargin = childHPadding;
            layoutParams.rightMargin = childHPadding;
            layoutParams.gravity = Gravity.CENTER;
            editText.setOnKeyListener(this);

            setBg(editText, false);

       /*     if (i == 0) {
                setBg(editText, true);
            } else {
                setBg(editText, false);
            }*/
            editText.setTextColor(Color.BLACK);
             editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

            if (TYPE_NUMBER.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (TYPE_PASSWORD.equals(inputType)) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (TYPE_TEXT.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (TYPE_PHONE.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }

            //设置自定义id
           // editText.setId(i);

            //设置textview的字符宽度
            editText.setEms(1);
            editText.addTextChangedListener(this);
            addView(editText, i);
            mEditTextList.add(editText);

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setBg(EditText editText, boolean focus) {
        if (boxBgNormal != null && !focus) {
            editText.setBackground(boxBgNormal);
        } else if (boxBgFocus != null && focus) {
            editText.setBackground(boxBgFocus);
        }
    }

    public void setOnCompleteListener(afterTextChangedListener listener) {
        this.listener = listener;
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < box; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        if (full) {
            if (listener != null) {
                listener.onComplete(stringBuilder.toString());
                setEnabled(false);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();

   /*     Log.e("DOAING","子布局的个数："+count);
        Log.e("DOAING","widthMeasureSpec："+widthMeasureSpec);
        Log.e("DOAING","heightMeasureSpec："+heightMeasureSpec);*/

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (count > 0) {
            View child = getChildAt(0);
            int cHeight = child.getMeasuredHeight();
            int cWidth = child.getMeasuredWidth();
  /*          Log.e("DOAING","cHeight："+cHeight);
            Log.e("DOAING","cWidth："+cWidth);*/

            int maxH = cHeight + 2 * childVPadding;
            int maxW = (cWidth + childHPadding) * box + childHPadding;
      /*      Log.e("DOAING","maxH："+cHeight);
            Log.e("DOAING","maxW："+cWidth);*/

            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec),
                    resolveSize(maxH, heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = (i) * (cWidth + childHPadding);
            int cr = cl + cWidth;
            int ct = childVPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && count >= 1 && currentPosition != mEditTextList.size() - 1) {
            currentPosition++;
            mEditTextList.get(currentPosition).requestFocus();
            setBg(mEditTextList.get(currentPosition), true);
            setBg(mEditTextList.get(currentPosition - 1), false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
        } else {
            focus();
            checkAndCommit();
        }
    }

    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        EditText editText = (EditText) v;
        if (keyCode == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0) {
            int action = event.getAction();
            if (currentPosition != 0 && action == KeyEvent.ACTION_DOWN) {
                currentPosition--;
                mEditTextList.get(currentPosition).requestFocus();
                setBg(mEditTextList.get(currentPosition), true);
                setBg(mEditTextList.get(currentPosition + 1), false);
                mEditTextList.get(currentPosition).setText("");
            }
        }
        return false;
    }

    public interface afterTextChangedListener {
        void onComplete(String content);
    }
}
