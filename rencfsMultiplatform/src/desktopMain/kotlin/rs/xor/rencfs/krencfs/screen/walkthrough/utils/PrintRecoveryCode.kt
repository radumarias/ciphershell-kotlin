package rs.xor.rencfs.krencfs.screen.walkthrough.utils

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.apache.pdfbox.printing.PDFPageable
import java.awt.print.PrinterJob
import java.time.LocalDate

actual fun printRecoveryCode(recoveryCode: String, folderName: String) {
    val document = PDDocument()
    try {
        val page = PDPage()
        document.addPage(page)

        val contentStream = PDPageContentStream(document, page)
        contentStream.setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20f)
        contentStream.beginText()
        contentStream.newLineAtOffset(50f, 750f)
        contentStream.showText("Recovery Code for Folder: $folderName")
        contentStream.newLineAtOffset(0f, -50f)
        contentStream.showText("Code: $recoveryCode")
        contentStream.newLineAtOffset(0f, -50f)
        contentStream.showText("Generated on: ${LocalDate.now()}")
        contentStream.endText()
        contentStream.close()

        val printerJob = PrinterJob.getPrinterJob()
        printerJob.setPageable(PDFPageable(document))
        if (printerJob.printDialog()) {
            printerJob.print()
        }
    } catch (e: Exception) {
        throw RuntimeException("Failed to print recovery code: ${e.message}", e)
    } finally {
        document.close()
    }
}
