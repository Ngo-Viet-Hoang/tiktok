package com.example.tiktok.restapi;

import com.example.tiktok.entity.Account;
import com.example.tiktok.entity.dto.AccountLoginDto;
import com.example.tiktok.entity.dto.AccountRegisterDto;
import com.example.tiktok.repository.AccountRepository;
import com.example.tiktok.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "api/v1/accounts")
@RequiredArgsConstructor
public class AccountRestApi {
    final AccountService accountService;

    final AccountRepository accountRepository;
    final PasswordEncoder passwordEncoder;
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody @Valid AccountRegisterDto accountRegisterDto) throws Exception {
        return ResponseEntity.ok(accountService.register(accountRegisterDto));
    }
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AccountLoginDto accountLoginDto){
        return ResponseEntity.ok(accountService.login(accountLoginDto));
    }
    @RequestMapping(method = RequestMethod.GET)
    public String getInformation(){
        return "";
    }
    @RequestMapping(path = "{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getDetail(@PathVariable Long id) {
        Optional<Account> optionalAccount = accountService.findById(id);
        if (!optionalAccount.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(optionalAccount.get());
    }
    @RequestMapping(path = "/list",method = RequestMethod.GET)
    public ResponseEntity<List<Account>> getList(){
        return ResponseEntity.ok(accountService.findAll());
    }
    @PutMapping
    public ResponseEntity<?> update( @RequestBody AccountRegisterDto accountRegisterDto){
//
        Optional<Account> account = accountService.findById(accountRegisterDto.getId());
        if (!account.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Account existAccount = account.get();
        if (accountRegisterDto.getUsername() != null)
            existAccount.setUsername(accountRegisterDto.getUsername());
        if (accountRegisterDto.getEmail() != null)
            existAccount.setEmail(accountRegisterDto.getEmail());
        if (accountRegisterDto.getPassword() != null)
            existAccount.setPasswordHash(passwordEncoder.encode(accountRegisterDto.getPassword()));
        existAccount.setRole(accountRegisterDto.getRole());

        accountService.save(existAccount);
        return ResponseEntity.ok(existAccount);

    }
    @RequestMapping(method = RequestMethod.GET,path = "/profile")
    public ResponseEntity<?> getProfile(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Account> account  = accountRepository.findById(Long.parseLong(principal.toString()));
        return ResponseEntity.ok(account.get());
    }
    @DeleteMapping( "{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!accountService.findById(id).isPresent()) {
            ResponseEntity.badRequest().build();
        }
        accountService.deletedById(id);
        return ResponseEntity.ok().build();
    }


}
