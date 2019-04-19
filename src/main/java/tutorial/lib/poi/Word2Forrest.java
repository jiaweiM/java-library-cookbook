package tutorial.lib.poi;


import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public final class Word2Forrest
{
    Writer _out;
    HWPFDocument _doc;

    public Word2Forrest(HWPFDocument doc, OutputStream stream) throws IOException, UnsupportedEncodingException
    {
        OutputStreamWriter out = new OutputStreamWriter(stream, "UTF-8");
        _out = out;
        _doc = doc;

        init();
        openDocument();
        openBody();

        Range r = doc.getRange();
        StyleSheet styleSheet = doc.getStyleSheet();

        int sectionLevel = 0;
        int lenParagraph = r.numParagraphs();
        boolean inCode = false;
        for (int x = 0; x < lenParagraph; x++) {
            Paragraph p = r.getParagraph(x);
            String text = p.text();
            if (text.trim().length() == 0) {
                continue;
            }
            StyleDescription paragraphStyle = styleSheet.getStyleDescription(p.getStyleIndex());
            String styleName = paragraphStyle.getName();
            if (styleName.startsWith("Heading")) {
                if (inCode) {
                    closeSource();
                    inCode = false;
                }

                int headerLevel = Integer.parseInt(styleName.substring(8));
                if (headerLevel > sectionLevel) {
                    openSection();
                } else {
                    for (int y = 0; y < (sectionLevel - headerLevel) + 1; y++) {
                        closeSection();
                    }
                    openSection();
                }
                sectionLevel = headerLevel;
                openTitle();
                writePlainText(text);
                closeTitle();
            } else {
                int cruns = p.numCharacterRuns();
                CharacterRun run = p.getCharacterRun(0);
                String fontName = run.getFontName();
                if (fontName.startsWith("Courier")) {
                    if (!inCode) {
                        openSource();
                        inCode = true;
                    }
                    writePlainText(p.text());
                } else {
                    if (inCode) {
                        inCode = false;
                        closeSource();
                    }
                    openParagraph();
                    writePlainText(p.text());
                    closeParagraph();
                }
            }
        }
        for (int x = 0; x < sectionLevel; x++) {
            closeSection();
        }
        closeBody();
        closeDocument();
        _out.flush();

    }

    public static void main(String[] args)
    {
        try {
            OutputStream out = new FileOutputStream("c:\\test.xml");

            new Word2Forrest(new HWPFDocument(new FileInputStream(args[0])), out);
            out.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void init() throws IOException
    {
        _out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        _out.write(
                "<!DOCTYPE document PUBLIC \"-//APACHE//DTD Documentation V1.1//EN\" \"./dtd/document-v11.dtd\">\r\n");
    }

    public void openDocument() throws IOException
    {
        _out.write("<document>\r\n");
    }

    public void closeDocument() throws IOException
    {
        _out.write("</document>\r\n");
    }

    public void openBody() throws IOException
    {
        _out.write("<body>\r\n");
    }

    public void closeBody() throws IOException
    {
        _out.write("</body>\r\n");
    }

    public void openSection() throws IOException
    {
        _out.write("<section>");

    }

    public void closeSection() throws IOException
    {
        _out.write("</section>");

    }

    public void openTitle() throws IOException
    {
        _out.write("<title>");
    }

    public void closeTitle() throws IOException
    {
        _out.write("</title>");
    }

    public void writePlainText(String text) throws IOException
    {
        _out.write(text);
    }

    public void openParagraph() throws IOException
    {
        _out.write("<p>");
    }

    public void closeParagraph() throws IOException
    {
        _out.write("</p>");
    }

    public void openSource() throws IOException
    {
        _out.write("<source><![CDATA[");
    }

    public void closeSource() throws IOException
    {
        _out.write("]]></source>");
    }
}