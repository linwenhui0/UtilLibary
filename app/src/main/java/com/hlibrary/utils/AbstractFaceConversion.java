package com.hlibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.ArrayMap;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.hlibrary.util.Logger;
import com.hlibrary.util.constants.Constants;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linwenhui
 * @date 2015/11/17
 */
public abstract class AbstractFaceConversion {

    static final String TAG = "FaceConversion";
    private static Map<String, Integer> faceMap = new ArrayMap<>();
    private static AbstractFaceConversion mFaceConversion;

    private AbstractFaceConversion() {
    }

    public static synchronized AbstractFaceConversion getInstance() {
        return getInstance(Constants.FACE_REGEX);
    }

    public static synchronized AbstractFaceConversion getInstance(final String faceFormat) {
        if (mFaceConversion == null) {
            mFaceConversion = new AbstractFaceConversion() {
                @Override
                public String getFaceFormat() {
                    return faceFormat;
                }
            };
        }
        return mFaceConversion;
    }

    /**
     * 表情正则表达式
     * @return 返回正则表达式, 表情在字符串以某种格式进行显示, 如 我好[开心]啊, 表达式格式 \\[[^\\]]+\\]
     */
    public abstract String getFaceFormat();

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public SpannableStringBuilder getExpressionString(Context context, String str) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = getFaceFormat();
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Logger.Companion.getInstance().e(TAG, e.getMessage());
        }
        return spannableString;
    }

    /**
     * 往map添加表情
     *
     * @param imgId
     * @param faceContent
     * @return
     */
    public boolean addFace(int imgId, String faceContent) {
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = getFaceFormat();
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        Matcher matcher = sinaPatten.matcher(faceContent);
        if (matcher.find()) {
            faceMap.put(faceContent, imgId);
            return true;
        }
        return false;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private void dealExpression(Context context, SpannableStringBuilder spannableString, Pattern patten, int start) {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            int resId = faceMap.get(key);
            if (resId != 0) {
                Drawable drawable = ContextCompat.getDrawable(context, resId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ImageSpan imageSpan = new ImageSpan(drawable);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

}
