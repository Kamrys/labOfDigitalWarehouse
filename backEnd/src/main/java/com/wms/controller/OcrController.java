package com.wms.controller;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class OcrController {

    @Autowired
    private AipOcr ocrClient;

    // 选项1：通过文件路径识别
    @PostMapping("/ocr/path")
    public String ocrByPath(@RequestParam String imagePath) {
        JSONObject res = ocrClient.basicGeneral(imagePath, new HashMap<>());
        return res.toString(2);
    }

    // 选项2：通过上传文件识别（更推荐）
    @PostMapping("/ocr/upload")
    public String ocrByUpload(@RequestParam("file") MultipartFile file) {
        try {
            // 创建临时文件
            String tempFileName = "ocr_temp_" + UUID.randomUUID() +
                    getFileExtension(file.getOriginalFilename());
            Path tempFile = Files.createTempFile(tempFileName, "");

            // 保存上传文件到临时路径
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // 调用OCR识别
            JSONObject res = ocrClient.basicGeneral(tempFile.toString(), new HashMap<>());

            // 删除临时文件
            Files.deleteIfExists(tempFile);

            return res.toString(2);
        } catch (IOException e) {
            return "{\"error\": \"文件处理失败: " + e.getMessage() + "\"}";
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}