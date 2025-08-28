package com.felipe.docker_manager.controllers;

import com.felipe.docker_manager.services.DockerService;
import com.github.dockerjava.api.model.Container;
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
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DockerContainersControllerTest {
    @Mock
    private DockerService dockerService;

    @InjectMocks
    private DockerContainersController dockerContainersController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dockerContainersController).build();
    }

    @Test
    @DisplayName("Should list containers passing showAll=true")
    void testListContainers1() throws Exception {
        //Arrange
        List<Container> mockedContainers = Collections.emptyList();
        when(dockerService.listContainers(true)).thenReturn(mockedContainers);

        //Act & Assert
        mockMvc.perform(get("/api/containers"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).listContainers(true);
    }

    @Test
    @DisplayName("Should list containers passing showAll=false")
    void testListContainers2() throws Exception {
        //Arrange
        List<Container> mockedContainers = Collections.emptyList();
        when(dockerService.listContainers(false)).thenReturn(mockedContainers);

        //Act & Assert
        mockMvc.perform(get("/api/containers")
                        .param("showAll", "false"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(dockerService, times(1)).listContainers(false);
    }

    @Test
    @DisplayName("Should create a container")
    void testCreateContainer() throws Exception {
        //Arrange
        String imageName = "nginx:latest";
        doNothing().when(dockerService).createContainer(imageName);

        //Act & Assert
        mockMvc.perform(post("/api/containers").param("imageName", imageName))
                .andExpect(status().isOk()); // Assuming POST / returns 200 OK

        verify(dockerService, times(1)).createContainer(imageName);
    }

    @Test
    @DisplayName("Should start a container")
    void testStartContainer() throws Exception {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        doNothing().when(dockerService).startContainer(containerId);

        //Act & Assert
        mockMvc.perform(post("/api/containers/{id}/start", containerId))
                .andExpect(status().isOk());

        verify(dockerService, times(1)).startContainer(containerId);
    }

    @Test
    @DisplayName("Should stop a container")
    void testStopContainer() throws Exception {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        doNothing().when(dockerService).stopContainer(containerId);

        //Act & Assert
        mockMvc.perform(post("/api/containers/{id}/stop", containerId))
                .andExpect(status().isOk());

        verify(dockerService, times(1)).stopContainer(containerId);
    }

    @Test
    @DisplayName("Should delete a container")
    void testDeleteContainer() throws Exception {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        doNothing().when(dockerService).deleteContainer(containerId);

        //Act & Assert
        mockMvc.perform(delete("/api/containers/{id}", containerId))
                .andExpect(status().isOk());

        verify(dockerService, times(1)).deleteContainer(containerId);
    }
}

