import * as fs from "fs";
import { PDFDocument } from "pdf-lib";

const A4_PAGE_WIDTH = 595.28;
const A4_PAGE_HEIGHT = 841.89;

async function loadPdf(path: string): Promise<PDFDocument> {
  const bytes = fs.readFileSync(path);
  const pdf = await PDFDocument.load(bytes);
  return pdf;
}

async function savePdf(path: string, pdf: PDFDocument) {
  const manipulatedPDFBytes = await pdf.save();
  fs.writeFileSync(path, manipulatedPDFBytes);
}

export async function writeBlankPages(
  inputPath: string,
  outputPath: string
): Promise<void> {
  const originalPDF = await loadPdf(inputPath);
  const manipulatedPDF = await PDFDocument.create();

  // Add a blank page after every page in the document
  for (let i = 0; i < originalPDF.getPageCount(); i++) {
    const [copiedPage] = await manipulatedPDF.copyPages(originalPDF, [i]);
    manipulatedPDF.addPage(copiedPage);
    const { width, height } = copiedPage.getSize();
    manipulatedPDF.addPage([width, height]);
  }

  await savePdf(outputPath, manipulatedPDF);
}
