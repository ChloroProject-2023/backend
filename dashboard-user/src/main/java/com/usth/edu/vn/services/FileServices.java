package com.usth.edu.vn.services;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Resources;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.repository.ResourceRepository;
import com.usth.edu.vn.repository.UserRepository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.usth.edu.vn.services.FileName.*;

@Singleton
public class FileServices {

  @Inject
  UserRepository userRepository;

  @Inject
  ResourceRepository resourceRepository;

  public List<Item> uploadFile(long user_id, MultipartFormDataInput input, String folder) throws IOException {
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
        writeFile(user_id, inputStream, folder, value.getFileName());
      }
    }
    return items;
  }

  public String uploadFile(long user_id, FileUpload input, String folder) throws IOException {
    Path file = input.uploadedFile();
    byte[] data = Files.readAllBytes(file);
    Path writeDir = Paths
        .get("GroupProject" + File.separator + user_id + File.separator + folder + File.separator + input.fileName());
    Files.write(writeDir, data);
    System.out.println(writeDir.toAbsolutePath().toString());
    return writeDir.toAbsolutePath().toString();
  }

  public byte[] getAvatar(long user_id) throws IOException, CustomException {
    File avatarDir = new File("GroupProject" + File.separator + user_id + File.separator + AVATARS);
    if (avatarDir.listFiles().length == 0) {
      throw new CustomException("No avatar uploaded!");
    } else {
      FileInputStream inputStream = new FileInputStream(avatarDir.getPath() + File.separator + avatarDir.list()[0]);
      byte[] imageData = inputStream.readAllBytes();
      inputStream.close();
      return imageData;
    }
  }

  private void writeFile(long user_id, InputStream inputStream, String folder, String filename)
      throws IOException {
    byte[] data = inputStream.readAllBytes();
    File writeDir = new File(
        "GroupProject" + File.separator + user_id + File.separator + folder + File.separator + filename);
    OutputStream outputStream = new FileOutputStream(writeDir);
    outputStream.write(data);
    outputStream.close();
  }

  public void createUserDir(long user_id) {
    File userModelDir = new File("GroupProject" + File.separator + user_id + File.separator + MODELS);
    File userResourceDir = new File("GroupProject" + File.separator + user_id + File.separator + RESOURCES);
    File userAvatarDir = new File("GroupProject" + File.separator + user_id + File.separator + AVATARS);
    Resources resource = new Resources();
    resource.setType("Directories");
    resource.setFilepath("GroupProject" + File.separator + user_id + File.separator + AVATARS);
    resourceRepository.addResource(user_id, resource);
    userModelDir.mkdirs();
    userResourceDir.mkdirs();
    userAvatarDir.mkdirs();
  }

  public void deleteUserDir(String username) {
    Optional<Users> existedUser = userRepository.findByUsername(username);
    if (existedUser.isPresent()) {
      long user_id = existedUser.get().getId();
      File userDir = new File("GroupProject" + File.separator + user_id);
      deleteDir(userDir);
    }
  }

  public void deleteUserDir(long user_id) {
    File userDir = new File("GroupProject" + File.separator + user_id);
    deleteDir(userDir);
  }

  public void deleteDir(File directory) {
    if (directory.isDirectory()) {
      File[] files = directory.listFiles();
      if (files != null && files.length > 0) {
        for (File file : files) {
          deleteDir(file);
        }
      }
      directory.delete();
    }
    directory.delete();
  }

  public void deleteDir(long user_id, String folder) {
    File userAvatarDir = new File("GroupProject" + File.separator + user_id + File.separator + folder);
    File[] allFiles = userAvatarDir.listFiles();
    for (File file : allFiles) {
      deleteDir(file);
    }
  }

  @Data
  @AllArgsConstructor
  private static class Item {
    private final String key;
    private final long size;
    private final String charset;
    private final String filename;
    private final boolean isFileItem;
    private final Map<String, List<String>> headers;
  }
}
