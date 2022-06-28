import org.apache.pdfbox.pdmodel.PDDocument;
import pdf.Reader;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PdfReader {
    private static Reader reader;

    private static String docText(){
        return
                "";
    }
    public static void main(String[] args) throws IOException {
        reader = new Reader();
        String name = "Район_ОЛЕКСАНДРІВКА_БУГАЇВКА_Харківська_обл_20220614_1101";
        String folderName = "1406 1101";
        String filePath = "C:\\Users\\Daria\\Downloads\\Telegram Desktop\\" + name + ".pdf";

        String directoryName = "C:\\Users\\Daria\\Downloads\\Telegram Desktop\\"+folderName;
        String textFilePath = "C:\\Users\\Daria\\Downloads\\Telegram Desktop\\"+folderName+"\\coordinates.txt";

        String docText = docText();

        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }

        File file = new File(filePath);
        PDDocument document = PDDocument.load(file);
        reader.getText(document);
        List<RenderedImage> images = reader.getImagesFromPDF(document);
        for (int i =0; i<images.size(); ++i){
            RenderedImage image = images.get(i);
            String imageName = Integer.toString(i);
            ImageIO.write(image, "jpg", new File(directoryName+"\\"+imageName+".jpg"));
        }

        String refactoredText = reader.changeCoordinatesFormat(folderName.replaceAll("_", " "), docText);
        File textFile = new File(textFilePath);
        FileWriter writer = new FileWriter(textFile);
        writer.write(refactoredText);
        writer.close();


    }
}
