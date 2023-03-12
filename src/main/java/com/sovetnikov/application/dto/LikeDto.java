package com.sovetnikov.application.dto;

import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto.UserDto;

public class LikeDto {

    private UserDto userDto;

    private ExcursionDto excursionDto;

    public LikeDto() {
    }

    public LikeDto(UserDto userDto, ExcursionDto excursionDto) {
        this.userDto = userDto;
        this.excursionDto = excursionDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public ExcursionDto getExcursionDto() {
        return excursionDto;
    }

    public void setExcursionDto(ExcursionDto excursionDto) {
        this.excursionDto = excursionDto;
    }
}
