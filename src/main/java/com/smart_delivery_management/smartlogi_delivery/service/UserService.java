package com.smart_delivery_management.smartlogi_delivery.service;

import com.smart_delivery_management.smartlogi_delivery.dto.RegisterRequest;
import com.smart_delivery_management.smartlogi_delivery.entity.User;

public interface UserService {
    User register(RegisterRequest request);
}
