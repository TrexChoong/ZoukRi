package com.trexworkshop.www;

/**
 * Created by trex on 9/2/2016.
 */
public interface ZoukRIPlatformDelegate {
    //New Font Framework
    byte[] createFont(String text, int size, int color, boolean bold, int strokewidth);

    byte[] createFont(String text, int size, int color, boolean bold, int strokewidth, String Align);

    byte[] drawMultiLineEllipsizedText( String _text, int size, int color, boolean bold, float height, float width, boolean elipses);

    byte[] drawMultiLineEllipsizedText( String _text, int size, int color, boolean bold, float height, float width, boolean elipses, String AlignS);

}
