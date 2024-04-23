import * as fs from 'fs';
import * as pdfLib from 'pdf-lib';

const PDFDocument = pdfLib.PDFDocument;

export async function writePdf(inputPath: string, outputPath: string): Promise<void> {
  const originalPDFBytes = fs.readFileSync(inputPath);

  // Load the original PDF into a new document
  const originalPDF = await PDFDocument.load(originalPDFBytes);
  const manipulatedPDF = await PDFDocument.create();

  // Add a blank page after every page in the document
  for (let i = 0; i < originalPDF.getPageCount(); i++) {
    const [copiedPage] = await manipulatedPDF.copyPages(originalPDF, [i]);
    manipulatedPDF.addPage(copiedPage);
    const {width, height} = copiedPage.getSize();
    manipulatedPDF.addPage([width, height]);
  }

  // Write the manipulated PDF to a new file
  const manipulatedPDFBytes = await manipulatedPDF.save();
  fs.writeFileSync(outputPath, manipulatedPDFBytes);
}