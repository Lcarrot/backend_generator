package ru.tyshchenko.vkr.engine.api.models.general;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralInfoDto {

    private String projectPath;
    private String projectPacket;
    private String projectName;
    private String db_url;
    private String db_password;
    private String db_user;
}