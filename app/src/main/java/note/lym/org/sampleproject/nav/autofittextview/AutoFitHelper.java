package note.lym.org.sampleproject.nav.autofittextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import note.lym.org.sampleproject.R;

/**
 * A helper class to enable automatically resizing {@link TextView}`s {@code textSize} to fit
 * within its bounds.
 *
 * @attr ref R.styleable.AutofitTextView_sizeToFit
 * @attr ref R.styleable.AutofitTextView_minTextSize
 * @attr ref R.styleable.AutofitTextView_precision
 */
public class AutoFitHelper {

    private static final String TAG = "AutoFitTextHelper";
    private static final boolean SPEW = false;
    // Minimum size of the text in pixels
    private static final int DEFAULT_MIN_TEXT_SIZE = 8; //sp
    // How precise we want to be when reaching the target textWidth size
    private static final float DEFAULT_PRECISION = 1.0f;

    /**
     * Creates a new instance of {@code AutoFitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoFitHelper create(TextView view) {
        return create(view, null, 0);
    }

    /**
     * Creates a new instance of {@code AutoFitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoFitHelper create(TextView view, AttributeSet attrs) {
        return create(view, attrs, 0);
    }

    /**
     * Creates a new instance of {@code AutoFitHelper} that wraps a {@link TextView} and enables
     * automatically sizing the text to fit.
     */
    public static AutoFitHelper create(TextView view, AttributeSet attrs, int defStyle) {
        AutoFitHelper helper = new AutoFitHelper(view);
        //是否开始适配监听
        boolean sizeToFit = true;
        if (attrs != null) {
            Context context = view.getContext();
            //适配的最小文字
            int minTextSize = (int) helper.getMinTextSize();
            //文字适配相差像素
            float precision = helper.getPrecision();
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoFitTextView, defStyle, 0);
            sizeToFit = ta.getBoolean(R.styleable.AutoFitTextView_sizeToFit, sizeToFit);
            minTextSize = ta.getDimensionPixelSize(R.styleable.AutoFitTextView_minTextSize, minTextSize);
            precision = ta.getFloat(R.styleable.AutoFitTextView_precision, precision);
            ta.recycle();
            helper.setMinTextSize(TypedValue.COMPLEX_UNIT_PX, minTextSize).setPrecision(precision);
        }
        helper.setEnabled(sizeToFit);
        return helper;
    }

    /**
     * Re-sizes the textSize of the TextView so that the text fits within the bounds of the View.
     */
    private static void autoFit(TextView view, TextPaint paint, float minTextSize, float maxTextSize,
                                int maxLines, float precision, int height) {
        //获取TextView的实际宽度
        int targetWidth = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        if (targetWidth <= 0) {
            return;
        }
        CharSequence text = view.getText();
        TransformationMethod method = view.getTransformationMethod();
        if (method != null) {
            text = method.getTransformation(text, view);
        }
        Context context = view.getContext();
        Resources r = Resources.getSystem();
        DisplayMetrics displayMetrics;
        float size = maxTextSize;
        float high = size;
        float low = 0;
        if (context != null) {
            r = context.getResources();
        }
        displayMetrics = r.getDisplayMetrics();
        paint.set(view.getPaint());
        paint.setTextSize(size);
        /**
         * 这里有两种情况需要进行文字适配
         * 1.当 TextView的测量宽度大于文字的实际宽度
         * 2.当TextView的测量高度大于指定的高度
         */
        if (getStaticLayoutHeight(text, paint, size, targetWidth, displayMetrics) > height) {
            size = getAutoFitTextSize(text, paint, targetWidth, low, high, precision,
                    displayMetrics, height);
        }
        //如果适配后的文字小于指定的最小文字，则采用指定的最小文字
        if (size < minTextSize) {
            size = minTextSize;
        }
        //设置适配过后的文字大小
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    //获取TextView的测量高度
    private static int getStaticLayoutHeight(CharSequence text, TextPaint paint, float size, float width, DisplayMetrics displayMetrics) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size,
                displayMetrics));
        StaticLayout layout = new StaticLayout(text, paint, (int) width,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        return layout.getHeight();
    }

    /**
     * Recursive binary search to find the best size for the text.
     */
    private static float getAutoFitTextSize(CharSequence text, TextPaint paint,
                                            float targetWidth, float low, float high, float precision,
                                            DisplayMetrics displayMetrics, int height) {
        float mid = (low + high) / 2.0f;
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mid, displayMetrics));
        StaticLayout layout = new StaticLayout(text, paint, (int) targetWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        int lineCount = layout.getLineCount();
        int lineHeight = layout.getHeight();
        if (SPEW)
            Log.d(TAG, "low=" + low + " high=" + high + " mid=" + mid + " target=" + targetWidth + " maxLines=" + " lineCount=" + lineCount);

        if (lineHeight > height) {
            Log.d(TAG, "1______low=" + low + " high=" + high + " mid=" + mid+"lineHeight" + lineHeight);
            // For the case that `text` has more newline characters than `maxLines`.
            if ((high - low) < precision) {
                return low;
            }
            return getAutoFitTextSize(text, paint, targetWidth, low, mid, precision,
                    displayMetrics, height);
        } else if (lineHeight < height) {
            Log.d(TAG, "2______low=" + low + " high=" + high + " mid=" + mid+"lineHeight" + lineHeight);
            if ((high - low) < precision) {
                return low;
            }
            return getAutoFitTextSize(text, paint, targetWidth, mid, high, precision, displayMetrics, height);
        } else {
            Log.d(TAG, "2______low=" + low + " high=" + high + " mid=" + mid+"lineHeight" + lineHeight);
            return mid;
        }
    }

    private static int getLineCount(CharSequence text, TextPaint paint, float size, float width,
                                    DisplayMetrics displayMetrics) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size,
                displayMetrics));
        StaticLayout layout = new StaticLayout(text, paint, (int) width,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        return layout.getLineCount();
    }

    private static int getMaxLines(TextView view) {
        int maxLines = -1; // No limit (Integer.MAX_VALUE also means no limit)

        TransformationMethod method = view.getTransformationMethod();
        if (method != null && method instanceof SingleLineTransformationMethod) {
            maxLines = 1;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // setMaxLines() and getMaxLines() are only available on android-16+
            maxLines = view.getMaxLines();
        }
        return maxLines;
    }

    // Attributes
    private TextView mTextView;
    private TextPaint mPaint;
    /**
     * Original textSize of the TextView.
     */
    private float mTextSize;

    private int mMaxLines;
    private float mMinTextSize;
    private float mMaxTextSize;
    private float mPrecision;

    private boolean mEnabled;
    private boolean mIsAutoFit;

    private ArrayList<OnTextSizeChangeListener> mListeners;

    private TextWatcher mTextWatcher = new AutoFitTextWatcher();

    private View.OnLayoutChangeListener mOnLayoutChangeListener =
            new AutoFitOnLayoutChangeListener();

    private AutoFitHelper(TextView view) {
        final Context context = view.getContext();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        mTextView = view;
        mPaint = new TextPaint();
        setRawTextSize(view.getTextSize());

        mMaxLines = getMaxLines(view);
        mMinTextSize = scaledDensity * DEFAULT_MIN_TEXT_SIZE;
        mMaxTextSize = mTextSize;
        mPrecision = DEFAULT_PRECISION;
    }

    /**
     * Adds an {@link OnTextSizeChangeListener} to the list of those whose methods are called
     * whenever the {@link TextView}'s {@code textSize} changes.
     */
    public AutoFitHelper addOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<OnTextSizeChangeListener>();
        }
        mListeners.add(listener);
        return this;
    }

    /**
     * Removes the specified {@link OnTextSizeChangeListener} from the list of those whose methods
     * are called whenever the {@link TextView}'s {@code textSize} changes.
     */
    public AutoFitHelper removeOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
        return this;
    }

    /**
     * Returns the amount of precision used to calculate the correct text size to fit within its
     * bounds.
     */
    public float getPrecision() {
        return mPrecision;
    }

    /**
     * Set the amount of precision used to calculate the correct text size to fit within its
     * bounds. Lower precision is more precise and takes more time.
     *
     * @param precision The amount of precision.
     */
    public AutoFitHelper setPrecision(float precision) {
        if (mPrecision != precision) {
            mPrecision = precision;

            autoFit();
        }
        return this;
    }

    /**
     * Returns the minimum size (in pixels) of the text.
     */
    public float getMinTextSize() {
        return mMinTextSize;
    }

    /**
     * Set the minimum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param size The scaled pixel size.
     * @attr ref me.grantland.R.styleable#AutofitTextView_minTextSize
     */
    public AutoFitHelper setMinTextSize(float size) {
        return setMinTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the minimum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     * @attr ref me.grantland.R.styleable#AutofitTextView_minTextSize
     */
    public AutoFitHelper setMinTextSize(int unit, float size) {
        Context context = mTextView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }

        setRawMinTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        return this;
    }

    private void setRawMinTextSize(float size) {
        if (size != mMinTextSize) {
            mMinTextSize = size;
            autoFit();
        }
    }

    /**
     * Returns the maximum size (in pixels) of the text.
     */
    public float getMaxTextSize() {
        return mMaxTextSize;
    }

    /**
     * Set the maximum text size to the given value, interpreted as "scaled pixel" units. This size
     * is adjusted based on the current density and user font size preference.
     *
     * @param size The scaled pixel size.
     * @attr ref android.R.styleable#TextView_textSize
     */
    public AutoFitHelper setMaxTextSize(float size) {
        return setMaxTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the maximum text size to a given unit and value. See TypedValue for the possible
     * dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     * @attr ref android.R.styleable#TextView_textSize
     */
    public AutoFitHelper setMaxTextSize(int unit, float size) {
        Context context = mTextView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }

        setRawMaxTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        return this;
    }

    private void setRawMaxTextSize(float size) {
        if (size != mMaxTextSize) {
            mMaxTextSize = size;

            autoFit();
        }
    }

    /**
     * @see TextView#getMaxLines()
     */
    public int getMaxLines() {
        return mMaxLines;
    }

    /**
     * @see TextView#setMaxLines(int)
     */
    public AutoFitHelper setMaxLines(int lines) {
        if (mMaxLines != lines) {
            mMaxLines = lines;

            autoFit();
        }
        return this;
    }

    /**
     * Returns whether or not automatically resizing text is enabled.
     */
    public boolean isEnabled() {
        return mEnabled;
    }

    /**
     * Set the enabled state of automatically resizing text.
     */
    public AutoFitHelper setEnabled(boolean enabled) {
        if (mEnabled != enabled) {
            mEnabled = enabled;

            if (enabled) {
                mTextView.addTextChangedListener(mTextWatcher);
                mTextView.addOnLayoutChangeListener(mOnLayoutChangeListener);

                autoFit();
            } else {
                mTextView.removeTextChangedListener(mTextWatcher);
                mTextView.removeOnLayoutChangeListener(mOnLayoutChangeListener);

                mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
        }
        return this;
    }

    /**
     * Returns the original text size of the View.
     *
     * @see TextView#getTextSize()
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * Set the original text size of the View.
     *
     * @see TextView#setTextSize(float)
     */
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the original text size of the View.
     *
     * @see TextView#setTextSize(int, float)
     */
    public void setTextSize(int unit, float size) {
        if (mIsAutoFit) {
            // We don't want to update the TextView's actual textSize while we're autofitting
            // since it'd get set to the autofitTextSize
            return;
        }
        Context context = mTextView.getContext();
        Resources r = Resources.getSystem();

        if (context != null) {
            r = context.getResources();
        }

        setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (mTextSize != size) {
            mTextSize = size;
        }
    }

    private void autoFit() {
        float oldTextSize = mTextView.getTextSize();
        float textSize;

        mIsAutoFit = true;
        autoFit(mTextView, mPaint, mMinTextSize, mMaxTextSize, mMaxLines, mPrecision, height);
        mIsAutoFit = false;

        textSize = mTextView.getTextSize();
        if (textSize != oldTextSize) {
            sendTextSizeChange(textSize, oldTextSize);
        }
    }

    private void sendTextSizeChange(float textSize, float oldTextSize) {
        if (mListeners == null) {
            return;
        }

        for (OnTextSizeChangeListener listener : mListeners) {
            listener.onTextSizeChange(textSize, oldTextSize);
        }
    }

    private int height;

    public void setMaxHeight(int height) {
        this.height = height;
    }

    private class AutoFitTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            autoFit();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // do nothing
        }
    }

    private class AutoFitOnLayoutChangeListener implements View.OnLayoutChangeListener {
        @Override
        public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
            autoFit();
        }
    }

    /**
     * When an object of a type is attached to an {@code AutoFitHelper}, its methods will be called
     * when the {@code textSize} is changed.
     */
    public interface OnTextSizeChangeListener {
        /**
         * This method is called to notify you that the size of the text has changed to
         * {@code textSize} from {@code oldTextSize}.
         */
        void onTextSizeChange(float textSize, float oldTextSize);
    }
}
