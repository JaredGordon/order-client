package cf.pivotal.orderClient;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class OrderControllerTest {

    @Autowired
    OrderServiceController orderServiceController;

    @Autowired
    HoldingServiceController holdingServiceController;

    @Test
    public void testCreateFind() {
        Holding h = new Holding();
        h.setQuantity(new BigDecimal(23.45));
        h.setAccountAccountid(123L);
        h.setPurchasedate(new Date());
        h.setPurchaseprice(new BigDecimal(34.56));
        h.setQuoteSymbol("GOOG");

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

        o.setHoldingHoldingid(h);
        List<Order> orders = new ArrayList<Order>();
        orders.add(o);
        h.setOrders(orders);

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
        for (Order oo : l) {
            assertEquals(o2.getAccountid(), oo.getAccountid());
            assertNotNull(o2.getHoldingHoldingid());
        }

        l = orderServiceController.findByAccountId(5432L);
        assertNotNull(l);
        assertTrue(l.size() == 0);

        l = orderServiceController.findByAccountIdAndStatus(o2.getAccountid(), o2.getOrderstatus());
        assertNotNull(l);
        assertTrue(l.size() > 0);
        for (Order oo : l) {
            assertEquals(o2.getOrderstatus(), oo.getOrderstatus());
        }

        l = orderServiceController.findByAccountIdAndStatus(o2.getAccountid(), "foo");
        assertNotNull(l);
        assertTrue(l.size() == 0);
    }

    @Test
    public void testDateParse() throws ParseException {
        String s = "2016-03-03T15:40:55.000+0000";
        DateTimeFormatter isoDateFormat = ISODateTimeFormat.dateTime();
        DateTime dateTimeObj1 = DateTime.parse(s, isoDateFormat);

        Date d = dateTimeObj1.toDate();

        System.out.println(d.toString());
        assertNotNull(d);
    }
}
