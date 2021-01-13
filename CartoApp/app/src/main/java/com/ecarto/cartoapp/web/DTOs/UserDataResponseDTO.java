package com.ecarto.cartoapp.web.DTOs;

import java.util.List;

public class UserDataResponseDTO {
    List<ProjectDTO> projectDTOList;

    public List<ProjectDTO> getProjectDTOList() {
        return projectDTOList;
    }

    public void setProjectDTOList(List<ProjectDTO> projectDTOList) {
        this.projectDTOList = projectDTOList;
    }
}
