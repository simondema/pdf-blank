package com.pdf.swissknife;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PdfHandler {

    private static final Logger log = Logger.getLogger(PdfHandler.class.getName());

    public static void main(String[] args) throws IOException {
        String inputPdfPath = null;
        String outputPdfPath = null;
        String operation = null;


        if (args.length < 6 || args.length > 7) {
            log.info("Invalid number of arguments");
            return;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-i")) {
                inputPdfPath = args[i + 1];
            } else if (args[i].equals("-o")) {
                outputPdfPath = args[i + 1];
            } else if (args[i].equals("-op")) {
                operation = args[i + 1];
            }
        }

        if (inputPdfPath == null || outputPdfPath == null || operation == null) {
            log.info("Invalid arguments");
            return;
        }

        log.log(Level.INFO, "Input PDF path: {0}", inputPdfPath);
        log.log(Level.INFO, "Output PDF path: {0}", outputPdfPath);
        log.log(Level.INFO, "Operation: {0}", operation);

        switch (operation) {
            case "blank" -> PdfBoxUtility.addBlankPages(inputPdfPath, outputPdfPath);
            case "merge" -> PdfBoxUtility.mergePages(inputPdfPath, outputPdfPath);
            case "annotate" -> PdfBoxUtility.bma(inputPdfPath, outputPdfPath);
            default -> log.info("Invalid operation");
        }
    }
}
