package se.teddy.atg.utils

import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Created by gengdahl on 2015-12-01.
 */
enum ZIPPER {
    UTIL;
    public String zip(String s){
        def targetStream = new ByteArrayOutputStream()
        def zipStream = new GZIPOutputStream(targetStream)
        zipStream.write(s.getBytes('UTF-8'))
        zipStream.close()
        def zippedBytes = targetStream.toByteArray()
        targetStream.close()
        return zippedBytes.encodeBase64()
    }

    public String unzip(String compressed){
        def inflaterStream = new GZIPInputStream(new ByteArrayInputStream(compressed.decodeBase64()))
        def uncompressedStr = inflaterStream.getText('UTF-8')
        return uncompressedStr
    }

}