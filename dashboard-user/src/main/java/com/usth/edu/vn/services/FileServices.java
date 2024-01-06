package com.usth.edu.vn.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;

@Singleton
public class FileServices {

  public List<Item> uploadFile(MultipartFormDataInput input) throws IOException {
    Map<String, Collection<FormValue>> map = input.getValues();
    List<Item> items = new ArrayList<>();
    for (var entry : map.entrySet()) {
      for (FormValue value : entry.getValue()) {
        items.add(new Item(
            entry.getKey(),
            value.isFileItem() ? value.getFileItem().getFileSize() : value.getValue().length(),
            value.getCharset(),
            value.getFileName(),
            value.isFileItem(),
            value.getHeaders()));
        InputStream inputStream = value.getFileItem().getInputStream();
        writeFile(inputStream, "test.png");
      }
    }
    return items;
  }

  private void writeFile(InputStream inputStream, String filename) throws IOException {
    byte[] data = inputStream.readAllBytes();
    File uploadDir = new File("~");
    filename = uploadDir.getPath() + filename;
    System.out.println(filename);
    Files.write(Paths.get(filename), data, StandardOpenOption.CREATE_NEW);
  }

  @Data
  @AllArgsConstructor
  private static class Item {
    private final String name;
    private final long size;
    private final String charset;
    private final String filename;
    private final boolean isFileItem;
    private final Map<String, List<String>> headers;
  }
}
