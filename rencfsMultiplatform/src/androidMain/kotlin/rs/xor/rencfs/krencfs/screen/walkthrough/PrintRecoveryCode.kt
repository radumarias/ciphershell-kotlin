package rs.xor.rencfs.krencfs.screen.walkthrough

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

actual fun printRecoveryCode(recoveryCode: String, folderName: String) {
    object : KoinComponent {
        val context: Context by inject()
    }.apply {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback?.onLayoutCancelled()
                    return
                }

                val info = PrintDocumentInfo.Builder("recovery_code_${folderName}.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(1)
                    .build()
                callback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                val document = PdfDocument()
                try {
                    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                    val page = document.startPage(pageInfo)
                    val canvas = page.canvas
                    val paint = Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 20f
                    }

                    canvas.drawText("Recovery Code for Folder: $folderName", 50f, 50f, paint)
                    canvas.drawText("Code: $recoveryCode", 50f, 100f, paint)
                    canvas.drawText("Generated on: ${LocalDate.now()}", 50f, 150f, paint)

                    document.finishPage(page)

                    ParcelFileDescriptor.AutoCloseOutputStream(destination).use { output ->
                        document.writeTo(output)
                    }

                    callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                } catch (e: Exception) {
                    callback?.onWriteFailed(e.message)
                } finally {
                    document.close()
                }
            }
        }

        printManager.print("RecoveryCode_${folderName}", printAdapter, null)
    }
}
