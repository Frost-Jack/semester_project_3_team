package com.example.accessingdata;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;


@Controller
public class UploadController {
    private final String UPLOAD_DIR = "C://upload/";

    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, HttpServletRequest request) {

        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }



        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String[] lab_and_name = fileName.split("_", 1);
        String lab_number = lab_and_name[0];
        String lab_name_local = lab_and_name[1];
        System.out.println(fileName + " " + lab_name_local + " " + lab_number);
        try {
            File theDir = new File(UPLOAD_DIR + lab_number);
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            Path path = Paths.get(UPLOAD_DIR + lab_number + "/" + lab_name_local);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        return "redirect:/";
    }

}
