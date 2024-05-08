package org.coursework.conferencemanagementsystem.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.coursework.conferencemanagementsystem.exception.PaperStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/paper")
public class PaperController {

    @Value("${submissions.storage-folder}")
    private String submissionStorageFolder;

    @Value("${reviews.storage-folder}")
    private String reviewStorageFolder;

    @GetMapping("/submission/{fileName}")
    public void viewSubmissionPdf(@PathVariable String fileName, HttpServletResponse response) {
        loadPdf(fileName, submissionStorageFolder, response);
    }

    @GetMapping("/reviews/{fileName}")
    public void viewReviewPdf(@PathVariable String fileName, HttpServletResponse response) {
        loadPdf(fileName, reviewStorageFolder, response);
    }

    private void loadPdf(String fileName, String folder, HttpServletResponse response) {

        try {
            ClassPathResource pdfFile = new ClassPathResource(folder + "/" + fileName);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            IOUtils.copy(pdfFile.getInputStream(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new PaperStorageException("Failed to load the paper: " + ex.getMessage());
        }
    }


}
