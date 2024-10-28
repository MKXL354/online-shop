package com.local.servlet.mapper;

public interface DTOMapper<DTO, ENTITY> {
    ENTITY map(DTO dto) throws DTOMapperException;
}
