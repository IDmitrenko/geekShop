package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.supershop.exceptions.UnsupportedMediaTypeException;
import ru.geekbrains.supershop.persistence.entities.Image;
import ru.geekbrains.supershop.persistence.repositories.ImageRepository;
import ru.geekbrains.supershop.utils.Validators;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${files.storepath.images}")
    private Path IMAGES_STORE_PATH;

    @Value("${files.storepath.icons}")
    private Path ICONS_STORE_PATH;

    private final ImageRepository imageRepository;

    private String getImageForSpecificProduct(UUID id) {
        return imageRepository.obtainImageNameByProductId(id);
    }

    private String getImage(UUID id) {
        return imageRepository.obtainImageNameByImageId(id);
    }

    private String getImageForSpecificReview(UUID id) {
        return imageRepository.obtainImageNameByReviewId(id);
    }

    public BufferedImage loadFileAsResource(String id) {
        String imageName = null;

        try {
            Path filePath;

            if (Validators.isUUID(id)) {

                imageName = getImageForSpecificProduct(UUID.fromString(id));

                if (imageName != null) {
                    filePath = IMAGES_STORE_PATH.resolve(imageName).normalize();
                } else {
                    imageName = "image_not_found.png";
                    filePath = ICONS_STORE_PATH.resolve(imageName).normalize();
                }
            } else {
                filePath = ICONS_STORE_PATH.resolve("cart.png").normalize();
            }

            if (filePath != null) {
                return ImageIO.read(new UrlResource(filePath.toUri()).getFile());
            } else {
                throw new IOException();
            }

        } catch (IOException ex) {
            log.error("Error! Image {} file wasn't found!", imageName);
            return null;
        }
    }

    public BufferedImage loadFileAsResourceImage(String id) throws IOException {
        String imageName = null;

        try {
            Path filePath;

            if (Validators.isUUID(id)) {
                imageName = getImageForSpecificReview(UUID.fromString(id));
                if (imageName != null) {
                    filePath = IMAGES_STORE_PATH.resolve(imageName).normalize();
                } else {
                    imageName = "image_not_found.png";
                    filePath = ICONS_STORE_PATH.resolve(imageName).normalize();
                }
            } else {
                imageName = "image_not_found.png";
                filePath = ICONS_STORE_PATH.resolve(imageName).normalize();
            }

            if (filePath != null) {
                return ImageIO.read(new UrlResource(filePath.toUri()).getFile());
            } else {
                throw new IOException();
            }
        } catch (IOException ex) {
            log.error("Error! Image {} file wasn't found!", imageName);
            return null;
        }
    }

    @Transactional
    public Image uploadImage(MultipartFile image, String imageName)
        throws IOException, UnsupportedMediaTypeException {
//        String fileExtension = image.getOriginalFilename().split("\\.")[1];
//        if (isExtension(fileExtension)) {
        if (image.getBytes().length != 0) {
            String uploadedFileName = imageName + "." + determineImageExtension(image);
            Path targetLocation = IMAGES_STORE_PATH.resolve(uploadedFileName);
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("File {} has been succesfully uploaded!", uploadedFileName);
            return imageRepository.save(new Image(uploadedFileName));
        } else {
            return null;
        }
    }

    private String determineImageExtension(MultipartFile image) throws UnsupportedMediaTypeException {

        switch (Objects.requireNonNull(image.getContentType())) {

            case MediaType.IMAGE_JPEG_VALUE:
                return "jpeg";

            case MediaType.IMAGE_PNG_VALUE:
                return "png";

            case MediaType.IMAGE_GIF_VALUE:
                return "gif";

            default:
                throw new UnsupportedMediaTypeException("Error! This file is not supported!");
        }
    }

/*1
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
*/

/*2
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