package com.procesos.colas.application.Utils;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class MultipartFileConverter {

    // Convierte un solo archivo byte[] a MultipartFile
    public static MultipartFile convertToMultipartFile(byte[] fileData, String fileName) {
        return new MockMultipartFile(fileName, fileName, MediaType.APPLICATION_OCTET_STREAM_VALUE, fileData);
    }

    // Convierte una lista de archivos byte[] a List<MultipartFile>
    public static List<MultipartFile> convertToMultipartFiles(List<byte[]> filesData, List<String> fileNames) {
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (int i = 0; i < filesData.size(); i++) {
            multipartFiles.add(convertToMultipartFile(filesData.get(i), fileNames.get(i)));
        }
        return multipartFiles;
    }
}