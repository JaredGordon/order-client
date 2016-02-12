/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cf.pivotal.accountClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountController implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountProfileService accountProfileService;

    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    public Account findAccount(Long id) {
        return accountRepository.findOne(id);
    }

    public Account saveAccount(Account account) {
        Accountprofile ap = accountProfileService.findAccountProfile(account.getAccountprofile().getProfileid());
        if (ap.getAccounts() == null || ap.getAccounts().size() < 1) {
            ap.addAccount(account);
        } else {
            ap.getAccounts().remove(0);
            ap.getAccounts().add(account);
        }
        ap = accountProfileService.saveAccountProfile(ap);
        return ap.getAccounts().get(0);
    }

    @Override
    public Account findByProfile(Accountprofile accountprofile) {
        Accountprofile ap = accountProfileService.findAccountProfile(accountprofile
                .getProfileid());
        if (ap != null && ap.getAccounts() != null && ap.getAccounts().size() > 0) {
            return ap.getAccounts().get(0);
        }
        return null;
    }
}
