package com.felipe.docker_manager.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class DockerService
{
    private final DockerClient dockerClient;


    public DockerService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }


    public void createContainer(String imageName) {
        dockerClient.createContainerCmd(imageName).exec();
    }

    public List<Container> ListContainers(boolean showAll) {
        return dockerClient.listContainersCmd().withShowAll(showAll).exec();
    }

    public void startContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    public void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
    }

    public List<Image> listImages() {
        return dockerClient.listImagesCmd().exec();
    }

    public List<Image> filterImagesByName(String filterName) {
        return dockerClient.listImagesCmd().withImageNameFilter(filterName).exec();
    }
}
