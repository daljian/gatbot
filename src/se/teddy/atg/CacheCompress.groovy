package se.teddy.atg

import groovy.json.JsonBuilder
import se.teddy.atg.utils.ZIPPER

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by gengdahl on 2015-12-01.
 */
import static groovy.io.FileType.FILES
import static java.nio.file.Files.createFile

new File('../../../../cache/atg/services/v1').eachFileRecurse(FILES) { file ->
    if(file.name.endsWith('.json')) {
        def newFile = Paths.get(file.parent, file.name.replace('json', 'jzon'))

        print "${newFile}..."

        def jsonText = new String(Files.readAllBytes(Paths.get(file.toURI())), Charset.defaultCharset())
        createFile(newFile).write(ZIPPER.UTIL.zip(jsonText));
        def jzonText = new String(Files.readAllBytes(newFile), Charset.defaultCharset())
        if (jsonText.equals(ZIPPER.UTIL.unzip(jzonText))){
            println "OK"
        }else{
            println "NOK"
            System.exit(-1)
        }
    }
}