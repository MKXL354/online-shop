package com.local.servlet.mapper;

import com.local.commonexceptions.ApplicationException;

public interface DTOMapper<DTO, ENTITY> {
    ENTITY map(DTO dto) throws ApplicationException;
}
