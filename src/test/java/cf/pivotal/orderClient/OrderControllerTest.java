package cf.pivotal.orderClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class OrderControllerTest {

    @Autowired
    OrderServiceController orderServiceController;

    @Autowired
    HoldingServiceController holdingServiceController;

    private Holding holding;

    @Before
    public void setUp() {
        if (holding == null) {
            Holding h = new Holding();
            h.setQuantity(new BigDecimal(23.45));
            h.setAccountAccountid(123L);
            h.setPurchasedate(new Date());
            h.setPurchaseprice(new BigDecimal(34.56));
            h.setQuoteSymbol("GOOG");

            holding = holdingServiceController.save(h);
        }
    }

    @After
    public void tearDown() {
        if (holding != null) {
            holdingServiceController.delete(holding.getHoldingid());
        }
    }

    @Test
    public void testCreateFind() {
        Order o = new Order();
        o.setAccountid(123L);
        o.setCompletiondate(new Date());
        o.setOpendate(new Date());
        o.setOrderfee(new BigDecimal(345.67));
        o.setOrderstatus("status");
        o.setOrdertype("type");
        o.setPrice(new BigDecimal(45.67));
        o.setQuantity(new BigDecimal(67));
        o.setQuoteid("GOOG");

        o.setHoldingHoldingid(holding);

        o = orderServiceController.saveOrder(o);
        assertNotNull(o);
        assertNotNull(o.getOrderid());

        Order o2 = orderServiceController.findOrder(o.getOrderid());
        assertNotNull(o2);

        assertNotNull(o2.getQuantity());
        assertNotNull(o2.getAccountid());
        assertNotNull(o2.getCompletiondate());
        assertNotNull(o2.getHoldingHoldingid());
        assertNotNull(o2.getOpendate());
        assertNotNull(o2.getOrderfee());
        assertNotNull(o2.getOrderid());
        assertNotNull(o2.getOrderstatus());
        assertNotNull(o2.getOrdertype());
        assertNotNull(o2.getPrice());
        assertNotNull(o2.getQuoteid());

        List<Order> l = orderServiceController.findByAccountId(o2.getAccountid());
        assertNotNull(l);
        assertTrue(l.size() > 0);
        for(Order oo: l) {
            assertEquals(o2.getAccountid(), oo.getAccountid());
        }

    }
}
