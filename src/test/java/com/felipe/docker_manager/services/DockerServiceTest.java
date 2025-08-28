package com.felipe.docker_manager.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class DockerServiceTest {
    @Mock
    private DockerClient dockerClient;

    @Mock
    private ListContainersCmd listContainersCmd;

    @Mock
    private StartContainerCmd startContainerCmd;

    @Mock
    private StopContainerCmd stopContainerCmd;

    @Mock
    private CreateContainerCmd createContainerCmd;

    @Mock
    private CreateContainerResponse createContainerResponse;

    @Mock
    private RemoveContainerCmd removeContainerCmd;

    @Mock
    private ListImagesCmd listImagesCmd;

    @InjectMocks
    private DockerService dockerService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should check containers when `listContainers` method receives showAll=true")
    public void testListContainers1() {
        //Arrange
        List<Container> mockContainers = Collections.emptyList();

        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withShowAll(true)).thenReturn(listContainersCmd);
        when(listContainersCmd.exec()).thenReturn(mockContainers);

        //Act
        List<Container> realResult = dockerService.listContainers(true);

        //Assert
        assertEquals(mockContainers, realResult);
        verify(dockerClient).listContainersCmd();
        verify(listContainersCmd).withShowAll(true);
        verify(listContainersCmd).exec();
    }

    @Test
    @DisplayName("Should check containers when `listContainers` method receives showAll=false")
    public void testListContainers2() {
        //Arrange
        List<Container> mockContainers = Collections.emptyList();

        when(dockerClient.listContainersCmd()).thenReturn(listContainersCmd);
        when(listContainersCmd.withShowAll(false)).thenReturn(listContainersCmd);
        when(listContainersCmd.exec()).thenReturn(mockContainers);

        //Act
        List<Container> realResult = dockerService.listContainers(false);

        //Assert
        assertEquals(mockContainers, realResult);
        verify(dockerClient).listContainersCmd();
        verify(listContainersCmd).withShowAll(false);
        verify(listContainersCmd).exec();
    }

    @Test
    @DisplayName("Should init a container with success passing Id")
    public void testStartContainer1() {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.startContainerCmd(eq(containerId))).thenReturn(startContainerCmd);

        //Act
        dockerService.startContainer(containerId);

        //Assert
        verify(dockerClient).startContainerCmd(containerId);
        verify(startContainerCmd).exec();
    }

    @Test
    @DisplayName("Should throw `NotFoundException` when starting a non-existent container")
    public void testStartContainer2() {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.startContainerCmd(eq(containerId))).thenReturn(startContainerCmd);
        when(startContainerCmd.exec()).thenThrow(new NotFoundException("Container not found"));

        //Act and Assert
        assertThrows(NotFoundException.class, () -> dockerService.startContainer(containerId));
        verify(dockerClient).startContainerCmd(containerId);
        verify(startContainerCmd).exec();
    }

    @Test
    @DisplayName("Should stop a container with success passing Id")
    public void testStopContainer1() {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.stopContainerCmd(eq(containerId))).thenReturn(stopContainerCmd);

        //Act
        dockerService.stopContainer(containerId);

        //Assert
        verify(dockerClient).stopContainerCmd(containerId);
        verify(stopContainerCmd).exec();
    }

    @Test
    @DisplayName("Should throw `NotFoundException` when stopping a non-existent container")
    public void testStopContainer2() {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.stopContainerCmd(eq(containerId))).thenReturn(stopContainerCmd);
        when(stopContainerCmd.exec()).thenThrow(new NotFoundException("Container not found"));

        //Act and Assert
        assertThrows(NotFoundException.class, () -> dockerService.stopContainer(containerId));
        verify(dockerClient).stopContainerCmd(containerId);
        verify(stopContainerCmd).exec();
    }

    @Test
    @DisplayName("Should create a container with success passing an image name")
    public void testCreateContainer() {
        //Arrange
        String imageName = "nginx:latest";
        when(dockerClient.createContainerCmd(imageName)).thenReturn(createContainerCmd);
        when(createContainerCmd.exec()).thenReturn(createContainerResponse);

        //Act
        dockerService.createContainer(imageName);

        //Assert
        verify(dockerClient).createContainerCmd(imageName);
        verify(createContainerCmd).exec();
    }

    @Test
    @DisplayName("Should delete a container with success passing Id")
    public void testDeleteContainer1() {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.removeContainerCmd(eq(containerId))).thenReturn(removeContainerCmd);

        //Act
        dockerService.deleteContainer(containerId);

        //Assert
        verify(dockerClient).removeContainerCmd(containerId);
        verify(removeContainerCmd).exec();
    }

    @Test
    @DisplayName("Should throw `NotFoundException` when deleting a non-existent container")
    public void testDeleteContainer2() {
        //Arrange
        String containerId = UUID.randomUUID().toString();
        when(dockerClient.removeContainerCmd(eq(containerId))).thenReturn(removeContainerCmd);
        when(removeContainerCmd.exec()).thenThrow(new NotFoundException("Container not found"));

        //Act and Assert
        assertThrows(NotFoundException.class, () -> dockerService.deleteContainer(containerId));
        verify(dockerClient).removeContainerCmd(containerId);
        verify(removeContainerCmd).exec();
    }

    @Test
    @DisplayName("Should list all images")
    public void testListImages() {
        //Arrange
        List<Image> mockImages = Collections.emptyList();
        when(dockerClient.listImagesCmd()).thenReturn(listImagesCmd);
        when(listImagesCmd.exec()).thenReturn(mockImages);

        //Act
        List<Image> realResult = dockerService.listImages();

        //Assert
        assertEquals(mockImages, realResult);
        verify(dockerClient).listImagesCmd();
        verify(listImagesCmd).exec();
    }

    @Test
    @DisplayName("Should filter images by name")
    public void testFilterImagesByName() {
        //Arrange
        String filterName = "my-image";
        List<Image> mockImages = Collections.emptyList();
        when(dockerClient.listImagesCmd()).thenReturn(listImagesCmd);
        when(listImagesCmd.withImageNameFilter(filterName)).thenReturn(listImagesCmd);
        when(listImagesCmd.exec()).thenReturn(mockImages);

        //Act
        List<Image> realResult = dockerService.filterImagesByName(filterName);

        //Assert
        assertEquals(mockImages, realResult);
        verify(dockerClient).listImagesCmd();
        verify(listImagesCmd).withImageNameFilter(filterName);
        verify(listImagesCmd).exec();
    }
}

