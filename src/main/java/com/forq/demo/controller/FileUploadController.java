package com.forq.demo.controller;

import com.sun.xml.internal.ws.api.wsdl.parser.MetaDataResolver;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * author long
 *
 * @date 2022/3/4
 * @apiNote
 */
@RestController
public class FileUploadController {
    @RequestMapping("/form")

    public String handleUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file)
    {
        if(!file.isEmpty()){
            return "redirect :uploadSuccess";
        }

        return "redirect : uploadFailure";

    }
    /*@PostMapping("/upload")
    public String onSubmit(@RequestPart("meta-data") , ){

    }*/
}
