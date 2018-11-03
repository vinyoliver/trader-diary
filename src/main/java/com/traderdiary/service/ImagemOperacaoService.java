package com.traderdiary.service;

import com.traderdiary.model.ImagemOperacao;
import com.traderdiary.utils.FileUtils;
import org.imgscalr.Scalr;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Stateless
public class ImagemOperacaoService extends BaseService<ImagemOperacao> {

    @Inject
    private AmazonS3Service s3Manager;

    public void uploadImagem(ImagemOperacao imagem, InputStream inputStream) throws IOException {

        //Normal image
        final File file = new File(System.getProperty("java.io.tmpdir"), "copia_" + imagem.getNomeFisico());
        org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);

        //Thumbnail image
        final BufferedImage resizedImage = getThumbnail(file);
        final File thumbnail = new File(System.getProperty("java.io.tmpdir"), "small_" + "copia_" + imagem.getNomeFisico());
        ImageIO.write(resizedImage, FileUtils.getFileExtension(thumbnail), thumbnail);
        try {
            s3Manager.uploadFile(imagem.getPath(), file);
            s3Manager.uploadFile(imagem.getPathSmall(), thumbnail);
            save(imagem);
        } catch (Exception e) {
            System.out.println("erro...");
        } finally {
            file.delete();
            thumbnail.delete();
        }
    }


    private BufferedImage getThumbnail(File file) throws IOException {
        return getResizedImage(new FileInputStream(file));
    }

    private BufferedImage getResizedImage(InputStream inputstream) throws IOException {
        final BufferedImage bufferedImage = Scalr.resize(ImageIO.read(inputstream), 300);
        return bufferedImage;
    }

    public void remove(ImagemOperacao imagem) {
        s3Manager.delete(imagem.getPath());
        s3Manager.delete(imagem.getPathSmall());
        super.remove(imagem);
    }


}
