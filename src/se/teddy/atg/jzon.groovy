#!/usr/bin/env groovy
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

import static java.nio.file.Files.createFile

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

public String read(Path path){
    new String(Files.readAllBytes(path), Charset.defaultCharset())
}
public void write(Path path, String content){
    createFile(path).write(content);
    println "Created ${path}"
}
private void usage(){
    println "\nUsage: " +
            "\n" +
            "\nCompress:" +
            "\njzon /path/to/file.json" +
            "\n" +
            "\nWill create /path/to/file.jzon" +
            "\n" +
            "\nDecompress:" +
            "\n" +
            "\njzon /path/to/file.jzon" +
            "\n" +
            "\nWill create /path/to/file.json"
    System.exit(-1)
}
try{
    def originalFile = Paths.get(args[0])
    if (!originalFile.toFile().exists()){
        usage()
    }
    originalFile = originalFile.toRealPath(LinkOption.NOFOLLOW_LINKS)
    if (originalFile.toFile().name.endsWith('json')){ // compress (json -> jzon)
        def newFile = Paths.get(originalFile.parent.toString(), originalFile.toFile().name.replace('json', 'jzon'))
        write(newFile, zip(read(originalFile)))
    }else if(originalFile.toFile().name.endsWith('jzon')){ // decompress (jzon -> json)
        def newFile = Paths.get(originalFile.parent.toString(), originalFile.toFile().name.replace('jzon', 'json'))
        write(newFile, unzip(read(originalFile)))
    }else{
        println "Nothing to do with ${originalFile}"
    }
}catch(Exception ex){
    //ex.printStackTrace()
    usage()
}
