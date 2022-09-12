package support

import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URI

fun String.toUri(): URI = URI.create(this)

object Resources {
    fun openStream(path: String): InputStream {
        return javaClass.classLoader.getResource(path)?.openStream() ?: throw FileNotFoundException("해당 파일을 찾을 수 없습니다.")
    }
}
