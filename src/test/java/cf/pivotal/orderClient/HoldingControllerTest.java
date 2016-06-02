package cf.pivotal.orderClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value=SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class HoldingControllerTest {

    private static final Long TEST_ID = 2L;
    private static final Long TEST_ACCOUNT_ID = 2L;

    @Autowired
    HoldingServiceController holdingServiceController;


    @Test
    public void testFind() {
        Holding h = holdingServiceController.find(TEST_ID);
        assertNotNull(h);
        assertEquals(TEST_ID, h.getHoldingid());
    }

    @Test
    public void testSaveFindDelete() {
        Holding h = new Holding();
        h.setQuantity(new BigDecimal(23.45));
        h.setAccountAccountid(123L);
        h.setPurchasedate(new Date());
        h.setPurchaseprice(new BigDecimal(34.56));
        h.setQuoteSymbol("GOOG");

        Holding h2 = holdingServiceController.save(h);
        assertNotNull(h2);

        Long id = h2.getHoldingid();
        assertNotNull(id);

        Holding h3 = holdingServiceController.find(id);
        assertNotNull(h3.getAccountAccountid());
        assertNotNull(h3.getQuoteSymbol());
        assertNotNull(h3.getPurchaseprice());
        assertNotNull(h3.getPurchasedate());
        assertNotNull(h3.getQuantity());

        holdingServiceController.delete(h3.getHoldingid());

        assertNull(holdingServiceController.find(id));
    }

    @Test
    public void testFindByAccountId() {
        List<Holding> l = holdingServiceController.findByAccountid(TEST_ACCOUNT_ID);
        assertNotNull(l);
        assertTrue(l.size() > 0);
        assertEquals(TEST_ACCOUNT_ID, l.get(0).getAccountAccountid());
    }

    @Test
    public void testFindHoldingSummary() {
        HoldingSummary s = holdingServiceController.findHoldingSummary(2L);
        assertNotNull(s);
        assertNotNull(s.getHoldingRollups());
        assertTrue(s.getHoldingRollups().size() > 0);
        assertNotNull(s.getHoldingRollups().get(0).getGain());
        assertNotNull(s.getHoldingRollups().get(0).getSymbol());
        assertEquals("100.0", s.getHoldingRollups().get(0).getPercent().toString());
        assertNotNull(s.getHoldingsTotalGains());
    }

    @Test
    public void testFindPortfolioSummmary() {
        PortfolioSummary s = holdingServiceController.findPortfolioSummary(2L);
        assertNotNull(s);
        assertTrue(s.getNumberOfHoldings() > 0);
        assertNotNull(s.getGain());
        assertNotNull(s.getTotalBasis());
        assertNotNull(s.getTotalMarketValue());
    }
}
