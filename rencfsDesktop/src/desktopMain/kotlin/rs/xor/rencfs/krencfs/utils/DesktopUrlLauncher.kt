package rs.xor.rencfs.krencfs.utils

import java.awt.Desktop
import java.net.URI

class DesktopUrlLauncher : UrlLauncher {
    override fun openUrl(url: String) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI(url))
        } else {
            throw UnsupportedOperationException("Opening URLs is not supported on this desktop environment")
        }
    }
}
