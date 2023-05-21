package com.example.bank_application.servise.interf;

import com.example.bank_application.dto.accountDto.AccountAfterCreateDto;
import com.example.bank_application.dto.accountDto.AccountCreateDto;
import com.example.bank_application.dto.accountDto.AccountDto;

import java.util.List;

public interface AccountService {

    AccountDto getAccountById(String id);

    List<AccountDto> getAllAccounts();
    List<AccountDto> getAllAccountsByStatus(String Status);

    AccountAfterCreateDto createNewAccount(AccountCreateDto accountCreateDto, String clientTaxCode);

    List<AccountAfterCreateDto> blockAccountByProductIdAndStatus(String productId, String status);
}
