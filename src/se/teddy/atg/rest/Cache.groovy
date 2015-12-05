package se.teddy.atg.rest

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import se.teddy.atg.utils.ZIPPER

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

import static java.nio.file.Files.createDirectories
import static java.nio.file.Files.createFile

/**
 * Created by gengdahl on 2015-11-22.
 */
class Cache {
    private String basePath;

    public Cache(String basePath) throws IOException{
        if (!Files.exists(Paths.get(basePath)) ){
            createDirectories(Paths.get(basePath))
        }
        this.basePath = Paths.get(basePath).toRealPath(LinkOption.NOFOLLOW_LINKS)
    }
    public put(String path, Map map){
        createDirectories(getPath(path).parent)
        createFile(getPath(path)).write(ZIPPER.UTIL.zip(new JsonBuilder(map).toPrettyString()));
        //println(" --> ${getPath(path)}")
    }
    public Map get(String path){
        def map = null
        if (exists(path)){
            //println "FILE GET ${getPath(path).toString()}"
            def jzonText = new String(Files.readAllBytes(getPath(path)), Charset.defaultCharset())
            map = new JsonSlurper().parseText(ZIPPER.UTIL.unzip(jzonText)) as Map
        }
        map
    }

    public boolean exists(String path){
        return getPath(path).toFile().exists()
    }

    private Path getPath(String path){
        Paths.get(basePath, path, "data.jzon");
    }


}
