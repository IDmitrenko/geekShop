package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.supershop.persistence.entities.Image;
import ru.geekbrains.supershop.persistence.repositories.ImageRepository;
import ru.geekbrains.supershop.utils.Validators;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private String getImageForSpecificProduct(UUID id) {
        return imageRepository.obtainImageNameByProductId(id);
    }

    public BufferedImage loadFileAsResource(String id) throws IOException {
        try {
            String imageName;
            Resource resource;
            if (Validators.isUUID(id)) {
                imageName = getImageForSpecificProduct(UUID.fromString(id));
                resource = new ClassPathResource("/static/images/" + imageName);
            } else {
                imageName = id;
                resource = new ClassPathResource("/static/icons/" + imageName);
            }
            return resource.exists() ?
                    ImageIO.read(resource.getFile()) :
                    ImageIO.read(new ClassPathResource("/static/icons/" + "image_not_found.png").getFile());
        } catch (MalformedInputException | FileNotFoundException ex) {
            return null;
        }

    }

/*
    private String getImage(UUID id) {
        return imageRepository.obtainImageNameByImageId(id);
    }

    public BufferedImage loadFileAsResourceImage(String id) throws IOException {
        try {
            String imageName = getImage(UUID.fromString(id));
            Resource resource = new ClassPathResource("/static/images/" + imageName);
            if (resource.exists()) {
                return ImageIO.read(resource.getFile());
            } else {
                log.error("Image not found!");
                throw new FileNotFoundException("File " + imageName + " not found!");
            }
        } catch (MalformedInputException | FileNotFoundException ex) {
            return null;
        }
    }
*/

}