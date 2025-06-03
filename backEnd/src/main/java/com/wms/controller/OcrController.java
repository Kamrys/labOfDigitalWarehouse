package com.wms.controller;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
        Path tempFile = null;
        try {
            // 创建临时文件
            String tempFileName = "ocr_temp_" + UUID.randomUUID() +
                    getFileExtension(file.getOriginalFilename());
            tempFile = Files.createTempFile(tempFileName, "");

            // 用try-with-resources确保流关闭
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // 调用OCR识别
            JSONObject res = ocrClient.basicGeneral(tempFile.toString(), new HashMap<>());

            return res.toString(2);
        } catch (IOException e) {
            return "{\"error\": \"文件处理失败: " + e.getMessage() + "\"}";
        } finally {
            // 最后删除临时文件
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    // 可以记录日志，但不要抛出
                }
            }
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}