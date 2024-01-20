package com.usth.edu.vn.services;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import com.usth.edu.vn.exception.CustomException;
import com.usth.edu.vn.model.Users;
import com.usth.edu.vn.repository.ModelRepository;
import com.usth.edu.vn.repository.ResourceRepository;
import com.usth.edu.vn.repository.UserRepository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.usth.edu.vn.services.FileName.*;

@Singleton
public class FileServices {

  @ConfigProperty(name = "system.folder", defaultValue = "GroupProject")
  String defaultFolder;

  @Inject
  UserRepository userRepository;

  @Inject
  ModelRepository modelRepository;

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

  public String uploadFile(long user_id, FileUpload input, String folder) throws IOException, CustomException {
    Path file = input.uploadedFile();
    byte[] data = Files.readAllBytes(file);
    Path writeDir = Paths
        .get(defaultFolder + File.separator + user_id + File.separator + folder + File.separator + input.fileName());
    writeDir.getParent().toFile().mkdirs();
    if (Files.notExists(writeDir)) {
      Files.write(writeDir, data);
      return writeDir.toAbsolutePath().toString();
    } else {
      throw new CustomException("File already existed!");
    }
  }

  public File getFile(long user_id, long file_id, String resourceType) throws IOException, CustomException {
    if (resourceType == MODELS && modelRepository.findByIdOptional(file_id).isPresent()) {
      File modelFile = new File(modelRepository.findModelById(file_id).getFilepath());
      if (modelFile.isFile()) {
        return modelFile;
      }
      throw new CustomException("You have not uploaded model script!");
    } else if (resourceType == RESOURCES && resourceRepository.findByIdOptional(file_id).isPresent()) {
      File resourceFile = new File(resourceRepository.findResourceById(file_id).getFilepath());
      if (resourceFile.isFile()) {
        return resourceFile;
      }
      throw new CustomException("Resource file has not been uploaded!");
    } else {
      throw new CustomException("File does not exist!");
    }
  }

  public byte[] getAvatar(long user) throws IOException, CustomException {
    File avatarDir = new File(defaultFolder + File.separator + user + File.separator + AVATARS);
    if (avatarDir.listFiles() == null) {
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
        defaultFolder + File.separator + user_id + File.separator + folder + File.separator + filename);
    OutputStream outputStream = new FileOutputStream(writeDir);
    outputStream.write(data);
    outputStream.close();
  }

  public void createUserDir(long user_id) {
    File userModelDir = new File(defaultFolder + File.separator + user_id + File.separator + MODELS);
    File userResourceDir = new File(defaultFolder + File.separator + user_id + File.separator + RESOURCES);
    File userAvatarDir = new File(defaultFolder + File.separator + user_id + File.separator + AVATARS);
    userModelDir.mkdirs();
    userResourceDir.mkdirs();
    userAvatarDir.mkdirs();
  }

  public void deleteUserDir(String username) {
    Optional<Users> existedUser = userRepository.findByUsername(username);
    if (existedUser.isPresent()) {
      long user_id = existedUser.get().getId();
      File userDir = new File(defaultFolder + File.separator + user_id);
      deleteDir(userDir);
    }
  }

  public void deleteUserDir(long user_id) {
    File userDir = new File(defaultFolder + File.separator + user_id);
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
    File userAvatarDir = new File(defaultFolder + File.separator + user_id + File.separator + folder);
    File[] allFiles = userAvatarDir.listFiles();
    for (File file : allFiles) {
      deleteDir(file);
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  private static class Model {
    @RestForm
    private String name;
    @RestForm
    private File file;
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
