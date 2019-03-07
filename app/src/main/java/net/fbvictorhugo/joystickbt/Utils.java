package net.fbvictorhugo.joystickbt;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class Utils {


    public static Spannable higlightText(final Context contexto, String textFull,
                                         String textHiglight, @ColorRes int color) {

        int start = textFull.indexOf(textHiglight);
        int end = start + textHiglight.length();

        Spannable spannableText = new SpannableString(textFull);
        spannableText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(contexto, color)), start, end, 0);
        spannableText.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);

        return spannableText;
    }
}
