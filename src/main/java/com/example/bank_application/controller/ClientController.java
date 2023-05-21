package com.example.bank_application.controller;

import com.example.bank_application.dto.clientDto.ClientDto;
import com.example.bank_application.dto.clientDto.ClientWithBalanceListDto;
import com.example.bank_application.servise.interf.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    public final ClientService clientService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients();
    }


    @GetMapping("/balance_more/{balance}")
    @ResponseStatus(HttpStatus.OK)
    public ClientWithBalanceListDto getListClientsWithBalanceMoreThan(@PathVariable("balance") String balance) {
        return clientService.getListClientsWithBalanceMoreThan(balance);
    }
}
