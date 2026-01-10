package com.pm.imageservice.service;

import com.pm.imageservice.dto.ImageUploadRequestDTO;
import com.pm.imageservice.model.MedicalImage;
import com.pm.imageservice.repository.MedicalImageRepository;
import com.pm.imageservice.storage.ImageStorageService;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MedicalImageService {

    private final MedicalImageRepository repository;
    private final ImageStorageService storageService;

    public MedicalImageService(MedicalImageRepository repository,
                               ImageStorageService storageService) {
        this.repository = repository;
        this.storageService = storageService;
    }

    public MedicalImage uploadImage(MultipartFile file, ImageUploadRequestDTO request) throws IOException {
        String path = storageService.save(file, request.getPatientId());

        MedicalImage image = new MedicalImage();
        image.setPatientId(request.getPatientId());
        image.setUploadedByStaffId(request.getStaffId());
        image.setImageType(request.getImageType());
        image.setDiseaseTag(request.getDiseaseTag());
        image.setFilePath(path);
        image.setContentType(file.getContentType());
        image.setFileSize(file.getSize());
        image.setUploadedAt(LocalDateTime.now());

        return repository.save(image);
    }

    public List<MedicalImage> getImagesByPatient(UUID patientId) {
        return repository.findByPatientId(patientId);
    }

    public MedicalImage getImageById(UUID imageId) {
        return repository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found: " + imageId));
    }

    public void deleteImage(UUID imageId) throws IOException {
        MedicalImage image = getImageById(imageId);
        storageService.delete(image.getFilePath());
        repository.delete(image);
    }

   public Resource getResourceForImage(MedicalImage image) throws IOException {
    return storageService.load(image.getFilePath());
}

}
