package com.pdf.swissknife;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationFreeText;
import org.apache.pdfbox.pdmodel.interactive.annotation.handlers.PDFreeTextAppearanceHandler;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PdfBoxUtility {

    public static void addBlankPages(String inputPdfPath, String outputPdfPath) throws IOException {
        Path inputPath = Paths.get(inputPdfPath);
        Path outputPath = Paths.get(outputPdfPath);

        if (!inputPath.toFile().exists()) {
            throw new IllegalArgumentException("Input file does not exist");
        }
        if (outputPath.toFile().exists()) {
            // delete the file if it already exists
            Files.delete(outputPath);
        }

        try (PDDocument outputPdf = new PDDocument(); PDDocument inputPdf = Loader.loadPDF(inputPath.toFile())) {
            int nPages = inputPdf.getNumberOfPages();
            PDFRenderer pdfRenderer = new PDFRenderer(inputPdf);
            try (ProgressBar pb = new ProgressBarBuilder().setStyle(ProgressBarStyle.ASCII).setTaskName("blanking").setInitialMax(nPages).build()) {
                for (int i = 0; i < nPages; i++) {
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
                    PDImageXObject pdImage = LosslessFactory.createFromImage(outputPdf, bim);

                    PDPage newPage = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
                    float scale = Math.min(newPage.getMediaBox().getWidth() / pdImage.getWidth(), newPage.getMediaBox().getHeight() / pdImage.getHeight());

                    PDPageContentStream contentStream = new PDPageContentStream(outputPdf, newPage);

                    // center the image on the page
                    float x = (newPage.getMediaBox().getWidth() - pdImage.getWidth() * scale) / 2;
                    float y = (newPage.getMediaBox().getHeight() - pdImage.getHeight() * scale) / 2;

                    contentStream.drawImage(pdImage, x, y, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
                    contentStream.close();
                    outputPdf.addPage(newPage);

                    outputPdf.addPage(new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())));

                    pb.stepBy(i);
                }
                outputPdf.save(outputPdfPath);
            }
        }
    }

    public static void mergePages(String inputPdfPath, String outputPdfPath) throws IOException {

        String inputPdfTempPath = inputPdfPath.replace(".pdf", "_merge_temp.pdf");
        addBlankPages(inputPdfPath, inputPdfTempPath);

        Path inputPath = Paths.get(inputPdfTempPath);
        Path outputPath = Paths.get(outputPdfPath);

        if (!inputPath.toFile().exists()) {
            throw new IllegalArgumentException("Input file does not exist");
        }

        if (outputPath.toFile().exists()) {
            Files.delete(outputPath);
        }

        try (PDDocument inputPdf = Loader.loadPDF(inputPath.toFile()); PDDocument outputPdf = new PDDocument()) {
            PDFRenderer pdfRenderer = new PDFRenderer(inputPdf);
            PDPageContentStream contentStream;
            int nPages = inputPdf.getNumberOfPages();
            try (ProgressBar pb = new ProgressBarBuilder().setStyle(ProgressBarStyle.ASCII).setTaskName("merging").setInitialMax(nPages).build()) {
                for (int i = 0; i < nPages; i += 6) {
                    PDPage newPage = new PDPage(PDRectangle.A4);
                    contentStream = new PDPageContentStream(outputPdf, newPage);

                    for (int j = 0; j < 6; j++) {
                        if (i + j < inputPdf.getNumberOfPages()) {
                            BufferedImage bim = pdfRenderer.renderImageWithDPI(i + j, 300, ImageType.RGB);
                            PDImageXObject pdImage = LosslessFactory.createFromImage(outputPdf, bim);

                            float scale = 0.08f;

                            float xPosition = (j % 2 == 0) ? 0 : newPage.getMediaBox().getWidth() / 2;
                            float yPosition = newPage.getMediaBox().getHeight() / 3 * (2 - ((float) j / 2));
                            float yOffset = 60.0f;
                            float xOffset = 20.0f;
                            contentStream.drawImage(pdImage, xPosition + xOffset, yPosition + yOffset, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
                        }
                    }
                    contentStream.close();
                    outputPdf.addPage(newPage);
                    pb.stepBy(6);
                }


                Files.delete(inputPath);
                outputPdf.save(outputPath.toFile());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // bmt stands for "Blanking, Merging, Annotating"
    public static void bma(String inputPdfPath, String outputPdfPath) throws IOException {

        String inputPdfTempPath = inputPdfPath.replace(".pdf", "_bma_temp.pdf");
        mergePages(inputPdfPath, inputPdfTempPath);

        Path inputPath = Paths.get(inputPdfTempPath);
        Path outputPath = Paths.get(outputPdfPath);

        if (!inputPath.toFile().exists()) {
            throw new IllegalArgumentException("Input file does not exist");
        }

        if (outputPath.toFile().exists()) {
            Files.delete(outputPath);
        }

        try (PDDocument inputPdf = Loader.loadPDF(inputPath.toFile()); PDDocument outputPdf = new PDDocument()) {
            FontUtility.loadFonts(outputPdf);

            int nPages = inputPdf.getNumberOfPages();
            try (ProgressBar pb = new ProgressBarBuilder().setStyle(ProgressBarStyle.ASCII).setTaskName("annotating").setInitialMax(nPages).build()) {
                for (int i = 0; i < nPages; i++) {
                    PDPage page = inputPdf.getPage(i);
                    PDAnnotation annotation = new PDAnnotationFreeText();

                    PDRectangle rectangle = new PDRectangle(new COSArray(List.of(
                            new COSFloat(314.127f),
                            new COSFloat(23.3037f),
                            new COSFloat(583.254f),
                            new COSFloat(824.902f))));
                    annotation.setRectangle(rectangle);
                    annotation.setBorder(COSArray.ofCOSIntegers(List.of(0, 0, 0)));

                    PDFreeTextAppearanceHandler appearanceHandler = new PDFreeTextAppearanceHandler(annotation);
                    appearanceHandler.generateAppearanceStreams();

                    annotation.getCOSObject().setString(COSName.DA, "/" + FontUtility.getRegularBaseFont().getName() + " 10 Tf 0 g");

                    page.getAnnotations().add(annotation);
                    outputPdf.addPage(page);
                    pb.step();
                }
            }

            if (inputPath.toFile().exists()) {
                Files.delete(inputPath);
            }
            outputPdf.save(outputPdfPath);
        }

    }
}
