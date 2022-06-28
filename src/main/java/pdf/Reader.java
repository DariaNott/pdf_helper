package pdf;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    public String getText(PDDocument document) throws IOException {
        return new PDFTextStripper().getText(document);
    }

    public String getTextForPages(PDDocument document) throws IOException {
        PDFTextStripper reader = new PDFTextStripper();
        String result = "";
        for (int i = 0; i < document.getPages().getCount(); i++) {
            reader.setStartPage(i);
            reader.setEndPage(i);
            String pageText = reader.getText(document);
            result = result + "\n\n--------------Page " + i + 1 + "--------------\n\n" + pageText;
        }
        return result;
    }

    public List<RenderedImage> getImagesFromPDF(PDDocument document) throws IOException {
        List<RenderedImage> images = new ArrayList<>();
        for (PDPage page : document.getPages()) {
            images.addAll(getImagesFromResources(page.getResources()));
        }
        return images;
    }

    public String changeCoordinatesFormat(String date, String original) {
        String refactored = original
                .replaceAll("Пн. Ш. = ", "")
                .replaceAll("Сх. Д. =", "N")
                .replaceAll("\\s+N\\s+", "N")
                .replaceAll("\\nX=\\d+", "E\n")
                .replaceAll("Y=\\d+", "")
                .replaceAll("(\\d+)\n", "$1\n" + date + "\n");
        return refactored;
    }

    public String changeCoordinatesFormat(String original) {
        String refactored = original
                .replaceAll("Пн. Ш. = ", "")
                .replaceAll("Сх. Д. =", "N")
                .replaceAll("\\s+N\\s+", "N")
                .replaceAll("\\nX=\\d+", "E\n")
                .replaceAll("^E", "E")
                .replaceAll("\\s+E", "E")
                .replaceAll("Y=\\d+", "");
        return refactored;
    }

    private List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
        List<RenderedImage> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            } else if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
            }
        }
        return images;
    }
}
