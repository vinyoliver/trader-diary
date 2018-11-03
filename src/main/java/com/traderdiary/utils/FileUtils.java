package com.traderdiary.utils;

import com.traderdiary.exception.AppException;
import com.traderdiary.model.Anexo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.IteratorUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FileUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    public static final String IMAGEM_DEFAULT = "img/image-default.png";
    public static final String USER_HOME_PATH = System.getProperty("user.home") + File.separator;


    public static String getFileExtension(File file) {
        return getFileExtension(file.getName());
    }

    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static String getExtension(String originalFileName) {
        int index = originalFileName.lastIndexOf(".");
        return originalFileName.substring(index, originalFileName.length());
    }

    public static void writeFile(InputStream inputStream, Anexo anexo) {
        try {
            String pathFile = USER_HOME_PATH + anexo.getPath();
            File file = new File(pathFile);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
            file.createNewFile();
        } catch (Exception e) {
            throw new AppException("Error write file " + anexo.getPath());
        }
    }

    /**
     * get file name from InputPart
     */
    public static String getFileName(InputPart inputPart) {

        try {
            Field field = inputPart.getClass().getDeclaredField("bodyPart");
            field.setAccessible(true);
            Object bodyPart = field.get(inputPart);
            Method methodBodyPart = bodyPart.getClass().getMethod("getHeader", new Class[]{});
            Iterable iterable = (Iterable) methodBodyPart.invoke(bodyPart, new Class[]{});
            Object[] content = IteratorUtils.toArray(iterable.iterator());
            Method methodContent = content[0].getClass().getMethod("getRaw", new Class[]{});

            String[] contentDisposition = methodContent.invoke(content[0], new Class[]{}).toString().split(";");

            for (String filename : contentDisposition) {
                if ((filename.trim().startsWith("filename"))) {

                    String[] name = filename.split("=");

                    String finalFileName = name[1].trim().replaceAll("\"", "");
                    return finalFileName;
                }
            }
            return "unknown";

        } catch (Exception e) {
            throw new AppException("Error get file name");
        }
    }

    /**
     * get file type from header MultivaluedMap
     */
    public static String getFileType(MultivaluedMap<String, String> header) {
        return header.getFirst("Content-type").split(";")[0];
    }

    /**
     * get image as base64 format
     */
    public static String getImageAsBase64(String url) {
        try {
            File file = new File(USER_HOME_PATH + url);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            fileInputStream.close();
            return new String(Base64.encodeBase64(bytes));
        } catch (FileNotFoundException e) {
            LOGGER.error("Não foi possível encontrar a imagem: " + url);
            return IMAGEM_DEFAULT;
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    public static File getFileOrNull(String path) {
        try {
            final File file = new File(USER_HOME_PATH + path);
            if (file.exists()) {
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro inesperado ao recuperar a imagem: " + path);
            return null;
        }
    }


    /**
     * get image as base64 format
     */
    public static String getImageAsByteArray(String url) {
        try {
            File file = new File(url);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            fileInputStream.close();
            return new String(Base64.encodeBase64(bytes));
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * delete file by url
     */
    public static void deleteFile(String url) {
        if (!new File(USER_HOME_PATH + url).delete()) {
            throw new AppException("erro.file.delete");
        }
    }

}