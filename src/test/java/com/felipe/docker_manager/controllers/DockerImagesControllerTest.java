package com.felipe.docker_manager.controllers;

import com.felipe.docker_manager.services.DockerService;
import com.github.dockerjava.api.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DockerImagesControllerTest {

    @Mock
    private DockerService dockerService;

    @InjectMocks
    private DockerImagesController dockerImagesController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dockerImagesController).build();
    }

    @Test
    @DisplayName("Should list all images")
    void testListImages() throws Exception {
        // Arrange
        List<Image> mockedImages = Collections.emptyList();
        when(dockerService.listImages()).thenReturn(mockedImages);

        // Act & Assert
        mockMvc.perform(get("/api/images"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).listImages();
    }

    @Test
    @DisplayName("Should list images with default filter")
    void testListImagesWithDefaultFilter() throws Exception {
        // Arrange
        List<Image> mockedImages = Collections.emptyList();
        when(dockerService.filterImagesByName("image-")).thenReturn(mockedImages);

        // Act & Assert
        mockMvc.perform(get("/api/images/filter"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).filterImagesByName("image-");
    }

    @Test
    @DisplayName("Should list images with custom filter")
    void testListImagesWithCustomFilter() throws Exception {
        // Arrange
        String filter = "my-custom-image";
        List<Image> mockedImages = Collections.emptyList();
        when(dockerService.filterImagesByName(filter)).thenReturn(mockedImages);

        // Act & Assert
        mockMvc.perform(get("/api/images/filter").param("filterName", filter))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).filterImagesByName(filter);
    }
}