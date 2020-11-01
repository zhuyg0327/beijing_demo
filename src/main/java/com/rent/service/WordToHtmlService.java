package com.rent.service;

import java.io.File;
import java.util.Map;

public interface WordToHtmlService {
    Map<String,Object> readHtml(File multipartFile);
}
