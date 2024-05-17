package com.pdf.manipulation;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class FontUtility {
    @Getter
    private static PDFont regularBaseFont;

    public static void loadFonts(PDDocument document) throws IOException {
        InputStream fontResource = Thread.currentThread().getContextClassLoader().getResourceAsStream("fonts/calibri.ttf");
        Encoding encoding = Encoding.getInstance(COSName.WIN_ANSI_ENCODING);
        regularBaseFont = PDTrueTypeFont.load(document, fontResource, encoding);
    }
}
