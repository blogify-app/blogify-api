package com.blogify.blogapi.integration.conf.MockData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileMockData {
  private static byte[] readFileToByteArray(String filePath) {
    try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
      //      fileInputStream.read(bytes);
      return new byte[fileInputStream.available()];
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static String jpgFilePath =
      "/home/herilala/Bureau/HEI/Prog5/projet-group/Back/blogify-api/src/main/resources/file/testFile/test.jpg";

  public static File jpgFile() {
    File file = new File(jpgFilePath);
    System.out.println(
        "-----------------------------//////////////////////////////////////////////////-----------------------------------------------...00");
    System.out.println(file);
    return file;
  }

  public static byte[] jpgFileContent() {
    File file = new File(jpgFilePath);
    System.out.println(file);
    return readFileToByteArray(jpgFilePath);
  }
  ;

  public static MultipartFile jpgMultipartFile() {
    return new MockMultipartFile(
        "test", // File name
        "test.png", // Origin file name
        "image/png", // MIME Type
        jpgFileContent());
  }
  ;

  //  static String pngFilePath = "file/testFile/test.png";
  //  static byte[] pngFileContent = readFileToByteArray(pngFilePath);
  //  public static MultipartFile pngMultipartFile(){
  //    return new MockMultipartFile(
  //            "test",       // File name
  //            "test.png",   // Origin file name
  //            "image/png",  // MIME Type
  //            pngFileContent);
  //  };

  //  static String gifFilePath = "file/testFile/test.gif";
  //  static byte[] gifFileContent = readFileToByteArray(gifFilePath);
  //  public static MultipartFile gifMultipartFile(){
  //    return new MockMultipartFile(
  //            "test",       // File name
  //            "test.gif",   // Origin file name
  //            "image/gif",  // MIME Type
  //            gifFileContent);
  //  };

  //  static String pdfFilePath = "file/testFile/test.pdf";
  //  static byte[] pdfFileContent = readFileToByteArray(pdfFilePath);
  //  public static MultipartFile pdfMultipartFile(){
  //    return new MockMultipartFile(
  //            "test",       // File name
  //            "test.pdf",   // Origin file name
  //            "application/pdf",  // MIME Type
  //            pdfFileContent);
  //  };

  //  static String bigPngFilePath = "file/testFile/test_big.png";
  //  static byte[] bigPngFileContent = readFileToByteArray(bigPngFilePath);
  //  public static MultipartFile bigPngMultipartFile(){
  //    return new MockMultipartFile(
  //            "test",       // File name
  //            "test.png",   // Origin file name
  //            "image/png",  // MIME Type
  //            bigPngFileContent);
  //  };

  //  static String wrongPngFilePath = "file/testFile/test.pdf.png";
  //  static byte[] wrongPngFileContent = readFileToByteArray(wrongPngFilePath);
  //  public static MultipartFile wrongPngMultipartFile(){
  //    return new MockMultipartFile(
  //            "test",       // File name
  //            "test.png",   // Origin file name
  //            "image/png",  // MIME Type
  //            wrongPngFileContent);
  //  };

}
