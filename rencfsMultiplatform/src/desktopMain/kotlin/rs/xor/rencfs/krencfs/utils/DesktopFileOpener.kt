package rs.xor.rencfs.krencfs.utils

class DesktopFileOpener : FileOpener {
    override fun openDirectory(uri: String) {
        val file = java.io.File(uri)
        if (file.exists() && file.isDirectory) {
            java.awt.Desktop.getDesktop().open(file)
        } else {
            throw IllegalArgumentException("Invalid or non-existent directory: $uri")
        }
    }
}
