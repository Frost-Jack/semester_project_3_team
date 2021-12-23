package com.example.accessingdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Text;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.List;


@Controller
public class UploadController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabRepository labRepository;

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
        List<User> Ip_users = userRepository.findByIp(request.getRemoteAddr());
        String access_level = Ip_users.stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getStat();
        if (access_level.equals("-1")){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String[] lab_and_name = fileName.split("_", 2);
            String lab_number = lab_and_name[0];
            String lab_name_local = lab_and_name[1];
            System.out.println(fileName + " " + lab_name_local + " " + lab_number);
            try {
                File theDir = new File(UPLOAD_DIR + "/lab_terms");
                if (!theDir.exists()){
                    theDir.mkdirs();
                }
                Path path = Paths.get(UPLOAD_DIR + "/lab_terms/" + lab_name_local);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');
        }else {


            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String[] lab_and_name = fileName.split("_", 1);
            String lab_number = lab_and_name[0];
            String lab_name_local = lab_and_name[1];
            System.out.println(fileName + " " + lab_name_local + " " + lab_number);
            try {
                File theDir = new File(UPLOAD_DIR + lab_number);
                if (!theDir.exists()) {
                    theDir.mkdirs();
                }
                Path path = Paths.get(UPLOAD_DIR + lab_number + "/" + lab_name_local);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');
        }
        return "redirect:/";
    }

    private static final String EXTERNAL_FILE_PATH = "C:\\upload\\";
    @RequestMapping("/file/{fileName:.+}")
    public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable("fileName") String fileName) throws IOException {

        File file = new File(EXTERNAL_FILE_PATH + fileName);
        if (file.exists()) {
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }

}
