package com.example.bank_application.servise.interf;

import com.example.bank_application.dto.clientDto.ClientDto;
import com.example.bank_application.dto.clientDto.ClientWithBalanceListDto;

import java.util.List;

public interface ClientService {

    ClientWithBalanceListDto getListClientsWithBalanceMoreThan(String balance);

    List<ClientDto> getAllClients();
}
