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

import java.util.List;

@Service
public class AccountProfileController implements AccountProfileService {
    @Autowired
    AccountProfileRepository accountProfileRepository;

    @Override
    public void deletelAccountProfile(Accountprofile accountProfile) {
        accountProfileRepository.delete(accountProfile);
    }

    @Override
    public Accountprofile findAccountProfile(Long id) {
        return accountProfileRepository.findOne(id);
    }

    @Override
    public Accountprofile saveAccountProfile(Accountprofile accountProfile) {
        return accountProfileRepository.save(accountProfile);

    }

    @Override
    public Accountprofile findByUseridAndPasswd(String userId, String passwd) {
        List<Accountprofile> aps = accountProfileRepository
                .findByUseridAndPasswd(userId, passwd);
        if (aps == null || aps.size() < 1) {
            return null;
        }
        return aps.get(0);
    }

    @Override
    public Accountprofile findByUserid(String username) {
        List<Accountprofile> aps = accountProfileRepository
                .findByUserid(username);
        if (aps == null || aps.size() < 1) {
            return null;
        }
        return aps.get(0);
    }

    @Override
    public Accountprofile findByAuthtoken(String authtoken) {
        List<Accountprofile> aps = accountProfileRepository
                .findByAuthtoken(authtoken);
        if (aps == null || aps.size() < 1) {
            return null;
        }
        return aps.get(0);
    }

}
