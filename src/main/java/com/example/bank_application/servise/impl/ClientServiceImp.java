package com.example.bank_application.servise.impl;

import com.example.bank_application.dto.clientDto.ClientDto;
import com.example.bank_application.dto.clientDto.ClientWithBalanceListDto;
import com.example.bank_application.entity.Client;
import com.example.bank_application.exceptions.DataNotFoundException;
import com.example.bank_application.exceptions.ErrorMessage;
import com.example.bank_application.exceptions.InvalidSearchArgumentException;
import com.example.bank_application.mapper.ClientMapper;
import com.example.bank_application.repository.ClientRepository;
import com.example.bank_application.servise.interf.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImp implements ClientService {
    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;


    @Override
    public ClientWithBalanceListDto getListClientsWithBalanceMoreThan(String balance) {
        double balanceD;
        try {
            balanceD = Double.valueOf(balance);
        } catch (IllegalArgumentException e) {
            throw new InvalidSearchArgumentException(ErrorMessage.ARGUMENT_IS_WRONG_TYPE_INCORRECT);
        }
        List<Client> clientList = clientRepository.findClientsBy(balanceD);
        if (clientList.size()==0) {
            throw new DataNotFoundException(ErrorMessage.CLIENTS_NOT_FOUND);
        }
       return new ClientWithBalanceListDto(clientMapper.toListDtoWithBalance(clientList));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        List<Client> clientList = clientRepository.getAllBy();
        if (clientList == null) {
            throw new DataNotFoundException(ErrorMessage.CLIENTS_NOT_FOUND);
        }
        return new ArrayList<>(clientMapper.toListDto(clientList));
    }
}
