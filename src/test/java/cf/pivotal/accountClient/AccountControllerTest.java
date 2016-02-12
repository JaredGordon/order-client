package cf.pivotal.accountClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@WebIntegrationTest(value = "server.port=9873")
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class AccountControllerTest {

    private static final Long TEST_ID = 2L;

    @Autowired
    AccountController accountController;

    @Autowired
    AccountProfileController accountProfileController;

    @Test
    public void testFind() {
        Account obj = accountController.findAccount(TEST_ID);
        assertNotNull("Should find a result.", obj);

        assertNotNull(obj.getAccountid());
        assertNotNull(obj.getBalance());
        assertNotNull(obj.getCreationdate());
        assertNotNull(obj.getLogincount());
        assertNotNull(obj.getLogoutcount());
        assertNotNull(obj.getOpenbalance());
        assertNotNull(obj.getVersion());
        assertNotNull(obj.getAccountprofile());
    }

    @Test
    public void testFindByProfile() {
        Accountprofile ap = accountProfileController
                .findAccountProfile(TEST_ID);
        assertNotNull(ap);

        Account obj = accountController.findByProfile(ap);
        assertNotNull("Should find a result.", obj);
    }

    @Test
    public void testSaveAndDelete() {
        Accountprofile ap = accountProfileController
                .findAccountProfile(TEST_ID);
        assertNotNull(ap);

        Account a = new Account();
        Date now = new Date();
        a.setCreationdate(now);
        ap.addAccount(a);

        ap = accountProfileController.saveAccountProfile(ap);
        assertNotNull(ap);
        ap = accountProfileController.findAccountProfile(ap.getProfileid());
        Account a2 = null;
        for (Account ac : ap.getAccounts()) {
            if (now.equals(ac.getCreationdate())) {
                a2 = ac;
            }
        }
        assertNotNull(a2);
        Long id = a2.getAccountid();
        assertNotNull(id);

//        accountController.deleteAccount(a2);
    }
}
