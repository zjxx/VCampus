package utils

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

fun downloadImageIfNotExists(imageUrl: String, localPath: String) {
    val file = File(localPath)
    val parentDir = file.parentFile
    if (!parentDir.exists()) {
        parentDir.mkdirs()
    }
    if (!file.exists()) {
        downloadImage(imageUrl, localPath)
    }
}

fun downloadImage(imageUrl: String, localPath: String) {
    val url = URL(imageUrl)
    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.doInput = true
    connection.connect()

    val inputStream: InputStream = connection.inputStream
    val outputStream = FileOutputStream(localPath)

    val buffer = ByteArray(1024)
    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        outputStream.write(buffer, 0, bytesRead)
    }

    outputStream.close()
    inputStream.close()
    connection.disconnect()
}

//downpdfifnotexist
fun downloadPdfIfNotExists(pdfUrl: String, localPath: String) {
    val file = File(localPath)
    val parentDir = file.parentFile
    if (!parentDir.exists()) {
        parentDir.mkdirs()
    }
    if (!file.exists()) {
        downloadPdf(pdfUrl, localPath)
    }
}

fun downloadPdf(pdfUrl: String, localPath: String) {
    val url = URL(pdfUrl)
    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.doInput = true
    connection.connect()

    val inputStream: InputStream = connection.inputStream
    val outputStream = FileOutputStream(localPath)

    val buffer = ByteArray(1024)
    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        outputStream.write(buffer, 0, bytesRead)
    }

    outputStream.close()
    inputStream.close()
    connection.disconnect()
}

//download mp4
fun downloadMp4IfNotExists(mp4Url: String, localPath: String) {
    val file = File(localPath)
    val parentDir = file.parentFile
    if (!parentDir.exists()) {
        parentDir.mkdirs()
    }
    if (!file.exists()) {
        downloadMp4(mp4Url, localPath)
    }
}

fun downloadMp4(mp4Url: String, localPath: String) {
    val url = URL(mp4Url)
    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.doInput = true
    connection.connect()

    val inputStream: InputStream = connection.inputStream
    val outputStream = FileOutputStream(localPath)

    val buffer = ByteArray(1024)
    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        outputStream.write(buffer, 0, bytesRead)
    }

    outputStream.close()
    inputStream.close()
    connection.disconnect()
}