package com.example.bank_application.servise.impl;

import com.example.bank_application.dto.managerDto.ManagerAfterCreateDto;
import com.example.bank_application.dto.managerDto.ManagerCreateDto;
import com.example.bank_application.dto.managerDto.ManagerDto;
import com.example.bank_application.dto.managerDto.ManagerListDto;
import com.example.bank_application.entity.Manager;
import com.example.bank_application.exceptions.ErrorMessage;
import com.example.bank_application.exceptions.ManagerAlreadyExists;
import com.example.bank_application.exceptions.ManagerNotFoundException;
import com.example.bank_application.mapper.ManagerMapper;
import com.example.bank_application.repository.ManagerRepository;
import com.example.bank_application.servise.interf.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ManagerServiceImp implements ManagerService {
    private final ManagerMapper managerMapper;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public ManagerDto getManagerById(String id) {
        return managerMapper.toDto(managerRepository.findManagerById(Long.parseLong(id)).orElseThrow(
                ()-> new ManagerNotFoundException(ErrorMessage.MANAGER_NOT_FOUND)));
    }
    @Override
    @Transactional
    public ManagerListDto getAllManagersWithClients() {
        return new ManagerListDto(managerMapper.toListDto(managerRepository.getAllByClientsNotNull()));
    }

    @Override
    @Transactional
    public ManagerAfterCreateDto managerNewCreate(ManagerCreateDto managerCreateDto) {
        Manager manager = managerMapper.toCreateEntity(managerCreateDto);
        managerRepository.findAll().forEach(m -> {
            if (m.equals(manager)) {
                throw new ManagerAlreadyExists(ErrorMessage.MANAGER_ALREADY_EXISTS);
            }
        });
        return  managerMapper.toAfterCreateDto(managerRepository.save(manager));
    }


}

