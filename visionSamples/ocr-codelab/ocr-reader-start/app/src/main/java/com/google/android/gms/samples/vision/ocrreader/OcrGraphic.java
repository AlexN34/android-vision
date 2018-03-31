/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.ocrreader;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
public class OcrGraphic extends GraphicOverlay.Graphic {

    private int mId;

    private static final int TEXT_COLOR = Color.WHITE;

    private static Paint sRectPaint;
    private static Paint sTextPaint;
    private final TextBlock mText;

    OcrGraphic(GraphicOverlay overlay, TextBlock text) {
        super(overlay);

        mText = text;

        if (sRectPaint == null) {
            sRectPaint = new Paint();
            sRectPaint.setColor(TEXT_COLOR);
            sRectPaint.setStyle(Paint.Style.STROKE);
            sRectPaint.setStrokeWidth(4.0f);
        }

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(TEXT_COLOR);
            sTextPaint.setTextSize(54.0f);
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public TextBlock getTextBlock() {
        return mText;
    }

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    public boolean contains(float x, float y) {
        // TODO: Check if this graphic's text contains this point.
        if (mText == null) {
            return false;
        }
        RectF rect = new RectF(mText.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateX(rect.bottom);
        return (rect.left < x && rect.right > x && rect.bottom < y && 2 * rect.bottom - rect.top > y);
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        if (mText == null) {
            return;
        }

        //Draws the bounding box around the TextBlock.
        RectF rect = new RectF(mText.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.bottom);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(2 * rect.bottom - rect.top);
        canvas.drawRect(rect, sRectPaint);

        // canvas.drawText(mText.getValue(), rect.left, rect.bottom, sTextPaint);
        // Break the text into lines and draw each one according to its own bounding
        List<? extends Text> textComponents = mText.getComponents();
        for (Text currentText : textComponents) {
            float left = translateX(currentText.getBoundingBox().left);
            float bottom = translateY(2 * currentText.getBoundingBox().bottom - currentText.getBoundingBox().top);
//            canvas.drawText(mText.getValue(), left, bottom, sTextPaint);
            canvas.drawText(detectCurrency(mText.getValue()), left, bottom, sTextPaint);
        }
    }



    // Assume everything passed in will be a currency (filtered by processor)
    private String detectCurrency(String inputText) {
        // find part after $ symbol
        String pattern = "^.*\\$[\\-]*([0-9]+[\\.]*[0-9]*).*$";
        // replace \\$ with currency in question - lookup table EUR => €
//        String pattern = "^\\$?(?=\\(.*\\)|[^()]*$)\\(?\\d{1,3}(,?\\d{3})?(\\.\\d\\d?)?\\)?$";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(inputText);
        String convertedString = "Err: No currency value after $ detected";
        if (!matcher.find()) {
            return convertedString;
        } else {
            // parse as number
            // multiply conversion factor
//            Log.d(TAG, "\n === Matched value for currency: " + matcher.group(1) + "(end) === \n");

            double currencyToConvert = Double.parseDouble(matcher.group(1));
            double conversion = 0.78;
            double finalValue = currencyToConvert / conversion;
            // add $ and convert to string and return
            return String.format("AUD: $%.10g", finalValue);
        }
    }
}
