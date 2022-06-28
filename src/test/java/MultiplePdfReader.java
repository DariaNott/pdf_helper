import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.zeroturnaround.zip.ZipUtil;
import pdf.Reader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MultiplePdfReader {

    private static Reader reader;
    private static String sourceDirectory = "C:\\Users\\Daria\\Downloads\\Telegram Desktop\\";


    //load all images and text from all pdf files from directory
    public static void main1(String[] args) throws IOException {
        reader = new Reader();
        File sourceDir = new File(sourceDirectory);
        List<File> files = List.of(sourceDir.listFiles());
        for (File file : files) {
            String name = file.getName();
            System.out.println("Processing " + name);
            String targetDir = sourceDirectory + name.substring(0, name.length() - 4);
            File directory = new File(targetDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            PDDocument document = PDDocument.load(file);
            List<RenderedImage> images = reader.getImagesFromPDF(document);
            for (int i = 0; i < images.size(); ++i) {
                RenderedImage image = images.get(i);
                String imageName = Integer.toString(i);
                ImageIO.write(image, "jpg", new File(targetDir + "\\" + imageName + ".jpg"));
            }
            File textFile = new File(targetDir + "\\coordinates.txt");
            FileWriter writer = new FileWriter(textFile);
            String docText = reader.getText(document);
            String text = reader.changeCoordinatesFormat(docText);
            writer.write(text);
            writer.close();
            document.close();
            file.renameTo(new File(targetDir + "\\" + name));
        }
    }

    //screenshots all pages
    public static void main2(String[] args) throws IOException {
        reader = new Reader();
        String fileExtension = "jpg";
        File sourceDir = new File(sourceDirectory);
        List<File> files = List.of(sourceDir.listFiles());
        for (File file : files) {
            String name = file.getName();
            System.out.println("Processing " + name);
            String targetDir = sourceDirectory + name.substring(0, name.length() - 4);
            File directory = new File(targetDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            PDDocument document = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int i = 0; i < document.getNumberOfPages(); ++i) {
                File outPutFile = new File(targetDir + "\\" + (i) + "." + fileExtension);
                BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
                ImageIO.write(bImage, fileExtension, outPutFile);
            }
            File textFile = new File(targetDir + "\\all_text.txt");
            FileWriter writer = new FileWriter(textFile);
            String docText = reader.getTextForPages(document);
            writer.write(docText);
            writer.close();
            document.close();
            file.renameTo(new File(targetDir + "\\" + name));
        }
    }

    //gets all pictures and all text for pages
    public static void main3(String[] args) throws IOException {
        reader = new Reader();
        File sourceDir = new File(sourceDirectory);
        List<File> files = List.of(sourceDir.listFiles());
        for (File file : files) {
            String name = file.getName();
            System.out.println("Processing " + name);
            String targetDir = sourceDirectory + name.substring(0, name.length() - 4);
            File directory = new File(targetDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            PDDocument document = PDDocument.load(file);
            List<RenderedImage> images = reader.getImagesFromPDF(document);
            for (int i = 0; i < images.size(); ++i) {
                RenderedImage image = images.get(i);
                String imageName = Integer.toString(i);
                ImageIO.write(image, "jpg", new File(targetDir + "\\" + imageName + ".jpg"));
            }
            File textFile = new File(targetDir + "\\coordinates.txt");
            FileWriter writer = new FileWriter(textFile);
            String docText = reader.getTextForPages(document);
            writer.write(docText);
            writer.close();
            document.close();
            file.renameTo(new File(targetDir + "\\" + name));
        }
    }
}

