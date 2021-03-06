/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wangyl.base.util

import kotlin.jvm.JvmOverloads
import android.os.Build
import java.io.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.UTF_8
import java.util.ArrayList
import java.util.zip.*

/**
 * ================================================
 * 处理压缩和解压的工具类
 *
 *
 * Created by JessYan on 10/05/2016
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class ZipHelper private constructor() {
    companion object {
        /**
         * zlib decompress 2 String
         *
         * @param bytesToDecompress
         * @param charsetName
         * @return
         */
        /**
         * zlib decompress 2 String
         *
         * @param bytesToDecompress
         * @return
         */
        @JvmOverloads
        fun decompressToStringForZlib(
            bytesToDecompress: ByteArray,
            charsetName: String = "UTF-8"
        ): String? {
            val bytesDecompressed = decompressForZlib(
                bytesToDecompress
            )
            var returnValue: String? = null
            try {
                returnValue = String(
                    bytesDecompressed!!,
                    0,
                    bytesDecompressed.size,
                    Charset.forName(charsetName)
                )
            } catch (uee: UnsupportedEncodingException) {
                uee.printStackTrace()
            }
            return returnValue
        }

        /**
         * zlib decompress 2 byte
         *
         * @param bytesToDecompress
         * @return
         */
        fun decompressForZlib(bytesToDecompress: ByteArray): ByteArray? {
            var returnValues: ByteArray? = null
            val inflater = Inflater()
            val numberOfBytesToDecompress = bytesToDecompress.size
            inflater.setInput(
                bytesToDecompress,
                0,
                numberOfBytesToDecompress
            )
            var numberOfBytesDecompressedSoFar = 0
            val bytesDecompressedSoFar: MutableList<Byte> = ArrayList()
            try {
                while (!inflater.needsInput()) {
                    val bytesDecompressedBuffer = ByteArray(numberOfBytesToDecompress)
                    val numberOfBytesDecompressedThisTime = inflater.inflate(
                        bytesDecompressedBuffer
                    )
                    numberOfBytesDecompressedSoFar += numberOfBytesDecompressedThisTime
                    for (b in 0 until numberOfBytesDecompressedThisTime) {
                        bytesDecompressedSoFar.add(bytesDecompressedBuffer[b])
                    }
                }
                returnValues = ByteArray(bytesDecompressedSoFar.size)
                for (b in returnValues.indices) {
                    returnValues[b] = bytesDecompressedSoFar[b]
                }
            } catch (dfe: DataFormatException) {
                dfe.printStackTrace()
            }
            inflater.end()
            return returnValues
        }

        /**
         * zlib compress 2 byte
         *
         * @param bytesToCompress
         * @return
         */
        fun compressForZlib(bytesToCompress: ByteArray?): ByteArray {
            val deflater = Deflater()
            deflater.setInput(bytesToCompress)
            deflater.finish()
            val bytesCompressed = ByteArray(Short.MAX_VALUE.toInt())
            val numberOfBytesAfterCompression = deflater.deflate(bytesCompressed)
            val returnValues = ByteArray(numberOfBytesAfterCompression)
            System.arraycopy(
                bytesCompressed,
                0,
                returnValues,
                0,
                numberOfBytesAfterCompression
            )
            return returnValues
        }

        /**
         * zlib compress 2 byte
         *
         * @param stringToCompress
         * @return
         */
        fun compressForZlib(stringToCompress: String): ByteArray? {
            var returnValues: ByteArray? = null
            try {
                returnValues = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    compressForZlib(
                        stringToCompress.toByteArray(StandardCharsets.UTF_8)
                    )
                } else {
                    compressForZlib(
                        stringToCompress.toByteArray(charset("UTF-8"))
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return returnValues
        }

        /**
         * gzip compress 2 byte
         *
         * @param string
         * @return
         * @throws IOException
         */
        fun compressForGzip(string: String): ByteArray? {
            var os: ByteArrayOutputStream? = null
            var gos: GZIPOutputStream? = null
            try {
                os = ByteArrayOutputStream(string.length)
                gos = GZIPOutputStream(os)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    gos.write(string.toByteArray(StandardCharsets.UTF_8))
                } else {
                    gos.write(string.toByteArray(charset("UTF-8")))
                }
                return os.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                closeQuietly(gos)
                closeQuietly(os)
            }
            return null
        }
        /**
         * gzip decompress 2 string
         *
         * @param compressed
         * @param charsetName
         * @return
         */
        /**
         * gzip decompress 2 string
         *
         * @param compressed
         * @return
         * @throws IOException
         */
        @JvmOverloads
        fun decompressForGzip(compressed: ByteArray, charsetName: String = "UTF-8"): String? {
            val BUFFER_SIZE = compressed.size
            var gis: GZIPInputStream? = null
            var `is`: ByteArrayInputStream? = null
            try {
                `is` = ByteArrayInputStream(compressed)
                gis = GZIPInputStream(`is`, BUFFER_SIZE)
                val string = StringBuilder()
                val data = ByteArray(BUFFER_SIZE)
                var bytesRead: Int
                while (gis.read(data).also { bytesRead = it } != -1) {
                    string.append(String(data, 0, bytesRead, Charset.forName(charsetName)))
                }
                return string.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                closeQuietly(gis)
                closeQuietly(`is`)
            }
            return null
        }

        fun closeQuietly(closeable: Closeable?) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (rethrown: RuntimeException) {
                    throw rethrown
                } catch (ignored: Exception) {
                }
            }
        }
    }

    init {
        throw IllegalStateException("you can't instantiate me!")
    }
}