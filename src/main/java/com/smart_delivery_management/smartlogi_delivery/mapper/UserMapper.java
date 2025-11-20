package com.smart_delivery_management.smartlogi_delivery.mapper;

import com.smart_delivery_management.smartlogi_delivery.dto.UserDTO;
import com.smart_delivery_management.smartlogi_delivery.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDTO toDTO(User user);
}
