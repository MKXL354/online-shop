package com.local.servlet.mapper;

@FunctionalInterface
public interface DTOMapper<DTO, ENTITY> {
    ENTITY map(DTO dto) throws DTOMapperException;
}
