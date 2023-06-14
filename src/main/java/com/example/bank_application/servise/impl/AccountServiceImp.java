package com.example.bank_application.servise.impl;

import com.example.bank_application.dto.accountDto.AccountAfterCreateDto;
import com.example.bank_application.dto.accountDto.AccountCreateDto;
import com.example.bank_application.dto.accountDto.AccountDto;
import com.example.bank_application.entity.Account;
import com.example.bank_application.entity.Client;
import com.example.bank_application.entity.enums.AccountStatus;
import com.example.bank_application.entity.enums.AccountType;
import com.example.bank_application.mapper.AccountMapper;
import com.example.bank_application.repository.AccountRepository;
import com.example.bank_application.repository.ClientRepository;
import com.example.bank_application.exceptions.*;
import com.example.bank_application.servise.interf.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountById(String id) {
        return accountMapper.toDto(accountRepository.findAccountById(UUID.fromString(id))
                .orElseThrow(
                () -> new DataNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccounts() {
        List<Account> accountList = accountRepository.getAllBy();
        if (accountList == null) {
            throw new DataNotFoundException(ErrorMessage.ACCOUNTS_NOT_FOUND);
        }
        return new ArrayList<>(accountMapper.toListDto(accountList));
    }

    @Override
    @Transactional
    public List<AccountDto> getAllAccountsByStatus(String status) {
        List<Account> accountList = accountRepository.getAllByStatus(AccountStatus.valueOf(status));
        if (accountList == null) {
            throw new DataNotFoundException(ErrorMessage.ACCOUNTS_NOT_FOUND_BY_STATUS);
        }
        return new ArrayList<>(accountMapper.toListDto(accountList));
    }

    @Override
    @Transactional
    public AccountAfterCreateDto createNewAccount(AccountCreateDto accountCreateDto, String clientTaxCode) {
        Client client = clientRepository.findClientByTaxCode(clientTaxCode);
        AccountAfterCreateDto accountAfterCreateDto;
        Account account;
        if (client == null) {
            throw new DataNotFoundException(ErrorMessage.CLIENT_NOT_FOUND_BY_TAX_CODE);
        } else if (accountRepository.findAccountByName(accountCreateDto.getName()) != null) {
            throw new DataAlreadyExistException(ErrorMessage.ACCOUNT_ALREADY_EXISTS);
        }
        account = accountMapper.toEntity(accountCreateDto);
        if (account.getBalance() == null) account.setBalance((double) 0);
        if (account.getStatus() == null) account.setStatus(AccountStatus.PENDING);
        if (account.getType() == null) account.setType(AccountType.CURRENT);
        account.setClient(client);
        accountAfterCreateDto = accountMapper.toDtoAfterCreate(account);
        accountRepository.save(account);
        return accountAfterCreateDto;
    }

    @Override
    @Transactional
    public List<AccountAfterCreateDto> blockAccountByProductIdAndStatus(String productId, String status) {
        List<Account> accountsByStatus = accountRepository.getAllByStatus(AccountStatus.valueOf(status));
        List<Account> accountsByStatusAndProductId = new ArrayList<>();
        Integer prodId = Integer.valueOf(productId);
        accountsByStatus.forEach(account -> {
            if (Objects.equals(account.getAgreement().getProduct().getId(), prodId)) {
                account.setStatus(AccountStatus.BLOCKED);
                account.setUpdatedAt(LocalDateTime.now());
                accountsByStatusAndProductId.add(account);
            }
        });
        if (accountsByStatusAndProductId.size() == 0) {
            throw new DataNotFoundException(ErrorMessage.ACCOUNTS_NOT_FOUND_BY_STATUS_AND_PRODUCT_ID);
        }
        return new ArrayList<>(accountMapper.toListAfterCreateDto(accountRepository.saveAll(accountsByStatusAndProductId)));
    }


}

