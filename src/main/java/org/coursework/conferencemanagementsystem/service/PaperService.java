package org.coursework.conferencemanagementsystem.service;

import org.coursework.conferencemanagementsystem.exception.PaperStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class PaperService {

    @Value("${submissions.storage-folder}")
    private String submissionsStorageFolder;

    @Value("${reviews.storage-folder}")
    private String reviewsStorageFolder;

    private ResourceLoader resourceLoader;

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void saveSubmissionPaper(MultipartFile submissionPaper) {
        savePaper(submissionPaper, submissionsStorageFolder);
    }

    public void saveReviewPaper(MultipartFile reviewPaper) {
        savePaper(reviewPaper, reviewsStorageFolder);
    }

    public void deleteReviewPaper(String fileName) {
        deletePaper(fileName, reviewsStorageFolder);
    }

    private void savePaper(MultipartFile paper, String folder) {
        try {
            if (paper == null || paper.isEmpty()) {
                throw new PaperStorageException("Paper is empty or missing");
            }

            if (!Objects.equals(paper.getContentType(), "application/pdf"))
                throw new PaperStorageException("Paper is not a PDF");

            Path folderPath = Paths.get(resourceLoader.getResource("classpath:").getURI()).resolve(folder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            Path filePath = folderPath.resolve(Objects.requireNonNull(paper.getOriginalFilename()));
            paper.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new PaperStorageException(e.getMessage());
        }
    }

    private void deletePaper(String fileName, String folder) {
        try {
            Path filePath = Paths.get(resourceLoader.getResource("classpath:").
                    getURI()).resolve(folder).resolve(fileName);
            Files.delete(filePath);
        } catch (IOException e) {
            throw new PaperStorageException("Failed to delete the paper: " + e.getMessage());
        }
    }
}
