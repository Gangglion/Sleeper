package com.example.sleep_project;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
//타임피커의 색 바꾸는 메소드
public class TimepickerColorChange {
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i=0;i<count;i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
//                    Field selectorWheelPaintField = numberPicker.getClass()
//                            .getDeclaredField("mSelectorWheelPaint");
                    Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
//Log.w("setNumberPickerTextColor", e);
                } catch (IllegalAccessException e) {
//Log.w("setNumberPickerTextColor", e);
                } catch (IllegalArgumentException e) {
//Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }
    public void setNumberPickerDividerColour(NumberPicker number_picker, Context mContext){
        final int count = number_picker.getChildCount();
        for(int i = 0; i < count; i++){
            try{
                Field dividerField = number_picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(mContext.getResources().getColor(android.R.color.white));
                dividerField.set(number_picker,colorDrawable);
                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("setNumberPickerTxtClr", e);
            }
            catch(IllegalAccessException e){
                Log.w("setNumberPickerTxtClr", e);
            }
            catch(IllegalArgumentException e){
                Log.w("setNumberPickerTxtClr", e);
            }
        }
    }
}
