package com.trexworkshop.www;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.ByteArrayOutputStream;

/**
 * Created by trex on 9/2/2016.
 */
public class ZoukRIAndroidDelegate implements ZoukRIPlatformDelegate {

    //New Font Framework
    public byte[] createFont(String text, int size, int color, boolean bold, int strokewidth) {

        Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(size);
        if (strokewidth != -1) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokewidth);
        } else {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        //paint.setTypeface((bold) ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        paint.setFakeBoldText(bold);

        return CFLogic(paint,text);
    }

    public byte[] createFont(String text, int size, int color, boolean bold, int strokewidth, String Align) {

        Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(size);
        //paint.setTypeface((bold) ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        paint.setFakeBoldText(bold);

        if (strokewidth != -1) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokewidth);
        } else {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        switch(Align) {
            case "Right":
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            case "Center":
                paint.setTextAlign(Paint.Align.CENTER);
                break;
            default:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
        }

        return CFLogic(paint,text);
    }

    public byte[] drawMultiLineEllipsizedText( String _text, int size, int color, boolean bold, float height, float width, boolean elipses) {

        TextPaint _textPaint = new TextPaint();
        _textPaint.setColor(color);
        _textPaint.setTextSize(size);
        //_textPaint.setTypeface((bold) ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        _textPaint.setFakeBoldText(bold);

        return MLLogic(_textPaint, _text, height, width, elipses, "");
    }

    public byte[] drawMultiLineEllipsizedText( String _text, int size, int color, boolean bold, float height, float width, boolean elipses, String AlignS) {

        TextPaint _textPaint = new TextPaint();
        _textPaint.setColor(color);
        _textPaint.setTextSize(size);
        //_textPaint.setTypeface((bold) ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        _textPaint.setFakeBoldText(bold);

        return MLLogic(_textPaint, _text, height, width, elipses, AlignS);
    }

    public byte[] MLLogic(TextPaint _textPaint, String _text, float height, float width, boolean elipses, String AlignS)
    {
        _textPaint.setSubpixelText(true);
        _textPaint.setFilterBitmap(true);
        _textPaint.setLinearText(true);
        _textPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Layout.Alignment Align = Layout.Alignment.ALIGN_NORMAL;
        switch(AlignS) {
            case "Right":
                Align = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case "Center":
                Align = Layout.Alignment.ALIGN_CENTER;
                break;
            default:
                break;
        }

        final StaticLayout measuringTextLayout = new StaticLayout(_text, _textPaint, (int) width, Align, 1.0f, 0.0f, false);

        int line;
        final int totalLineCount = measuringTextLayout.getLineCount();
        for (line = 0; line < totalLineCount; line++) {
            final int lineBottom = measuringTextLayout.getLineBottom(line);
            if (elipses && lineBottom > height) {
                break;
            }
        }
        line--;

        int lineEnd;
        try {
            lineEnd = measuringTextLayout.getLineEnd(line);
        } catch (Throwable t) {
            lineEnd = _text.length();
        }

        String truncatedText = _text.substring(0, Math.max(0, lineEnd));

        if (elipses && (truncatedText.length() < _text.length())) {
            truncatedText = truncatedText.substring(0, Math.max(0, truncatedText.length() - 3));
            truncatedText += "...";
        }

        final StaticLayout drawingTextLayout = new StaticLayout(truncatedText, _textPaint, (int) width, Align, 1.0f, 0.0f, false);

        int theight;
        if(elipses)
        {
            theight = (int) height;
        }else {
            theight = drawingTextLayout.getHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap((int) width, theight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.translate(0, 0);
        drawingTextLayout.draw(canvas);
        canvas.restore();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        bitmap.recycle();

        return data;
    }

    public byte[] CFLogic(Paint paint, String text) {
        Paint paint2 = null;
        paint.setSubpixelText(true);
        paint.setFilterBitmap(true);
        paint.setLinearText(true);
//		paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (paint.getStyle() == Paint.Style.STROKE) {
            paint2 = new Paint();
            paint2.setTextAlign(paint.getTextAlign());
            paint2.setTextSize(paint.getTextSize());
            paint2.setAntiAlias(paint.isAntiAlias());
            paint2.setARGB(255, 0, 0, 0);
            paint2.setStyle(Paint.Style.FILL);
        }

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        String[] lines = text.split("\n");
        float height = bounds.bottom + bounds.height() + 5;
        float width = 0;
        if (lines.length == 1) {
            width = bounds.left + bounds.width() + 5;
        } else {
            for (String line : lines) {
                paint.getTextBounds(line, 0, line.length(), bounds);
                width = Math.max(bounds.left + bounds.width() + 5, width);
            }
        }

        height = Math.max(height, 1);
        width = Math.max(width, 1);
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height * lines.length, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        int x = 0, y = bounds.height();

        if (paint.getTextAlign().toString().equals("CENTER"))
            x += bounds.width() / 2;

        for (String line : text.split("\n")) {

            if (paint2 != null)
                canvas.drawText(line, x, y, paint2);
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        bitmap.recycle();
        return data;
    }
}
