package cf.pivotal.orderClient;

import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import feign.FeignException;
import feign.Response;
import feign.gson.GsonDecoder;
import net.minidev.json.JSONArray;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDecoder extends GsonDecoder {

    private static final Type LIST_OF_ORDERS = new TypeToken<List<Order>>() {
    }.getType();

    private static final Type LIST_OF_HOLDINGS = new TypeToken<List<Holding>>() {
    }.getType();

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private static final Logger LOG = Logger.getLogger(OrderDecoder.class);

    @Override
    public Object decode(Response response, Type type) throws IOException,
            FeignException {

        Response.Body body = response.body();
        if (body == null) {
            return null;
        }

        // System.out.println(Util.toString(body.asReader()));

        if (Order.class.equals(type)) {
            return orderFromJson(JsonPath.parse(body.asInputStream()));
        }

        if (LIST_OF_ORDERS.equals(type)) {
            return ordersFromJson(JsonPath.parse(body.asInputStream()));
        }

        if (Holding.class.equals(type)) {
            return holdingFromJson(JsonPath.parse(body.asInputStream()), true);
        }

        if (LIST_OF_HOLDINGS.equals(type)) {
            return holdingsFromJson(JsonPath.parse(body.asInputStream()));
        }

        return super.decode(response, type);
    }

    private Holding holdingFromJson(ReadContext ctx, boolean processOrders) {
        if (ctx.json().toString().length() < 1) {
            return null;
        }

        Holding h = new Holding();
        h.setHoldingid(getLongValue(ctx, "$.holdingId"));
        h.setPurchaseprice(getBigDecimalValue(ctx, "$.purchasePrice"));
        h.setQuantity(getBigDecimalValue(ctx, "$.quantity"));
        h.setPurchasedate(getDateValue(ctx, "$.purchaseDate"));
        h.setAccountAccountid(getLongValue(ctx, "$.accountId"));
        h.setQuoteSymbol(getStringValue(ctx, "$.quoteSymbol"));

        JSONArray orders = ctx.read("$.orders");
        if (orders != null && processOrders) {
            h.setOrders(ordersFromJson(JsonPath.parse(orders.toString())));
            for (Order o : h.getOrders()) {
                o.setHoldingHoldingid(h);
            }
        }

        return h;
    }

    private List<Holding> holdingsFromJson(ReadContext ctx) {
        ArrayList<Holding> holdings = new ArrayList<Holding>();

        JSONArray as = ctx.read("$");
        for (int i = 0; i < as.size(); i++) {
            Holding h = new Holding();

            h.setHoldingid(getLongValue(ctx, "$.[" + i + "].holdingId"));
            h.setPurchaseprice(getBigDecimalValue(ctx, "$.[" + i + "].purchasePrice"));
            h.setQuantity(getBigDecimalValue(ctx, "$.[" + i + "].quantity"));
            h.setPurchasedate(getDateValue(ctx, "$.[" + i + "].purchaseDate"));
            h.setAccountAccountid(getLongValue(ctx, "$.[" + i + "].accountId"));
            h.setQuoteSymbol(getStringValue(ctx, "$.[" + i + "].quoteSymbol"));

            holdings.add(h);

            JSONArray orders = ctx.read("$.[" + i + "].orders");
            if (orders != null) {
                h.setOrders(ordersFromJson(JsonPath.parse(orders.toString())));
            }
        }

        return holdings;
    }

    private String getStringValue(ReadContext ctx, String path) {
        return ctx.read(path);
    }

    private Long getLongValue(ReadContext ctx, String path) {
        Object o = ctx.read(path);
        if (o == null) {
            return 0L;
        }
        return Long.decode(o.toString());
    }

    private Integer getIntegerValue(ReadContext ctx, String path) {
        Object o = ctx.read(path);
        if (o == null) {
            return 0;
        }
        return Integer.decode(o.toString());
    }

    private BigDecimal getBigDecimalValue(ReadContext ctx, String path) {
        Object o = ctx.read(path);
        if (o == null) {
            return new BigDecimal(0);
        }
        return new BigDecimal(o.toString());
    }

    private Date getDateValue(ReadContext ctx, String path) {
        Object o = ctx.read(path);
        if (o == null) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(o.toString());
        } catch (ParseException e) {
            LOG.error("unable to aprse date string: " + o.toString(), e);
            return null;
        }
    }

    private Order orderFromJson(ReadContext ctx) {
        Order o = new Order();

        o.setQuoteid(getStringValue(ctx, "$.quoteSymbol"));
        o.setQuantity(getBigDecimalValue(ctx, "$.quantity"));
        o.setPrice(getBigDecimalValue(ctx, "$.price"));
        o.setAccountid(getLongValue(ctx, "$.accountId"));
        o.setCompletiondate(getDateValue(ctx, "$.completionDate"));
        o.setOpendate(getDateValue(ctx, "$.openDate"));
        o.setOrderfee(getBigDecimalValue(ctx, "$.orderFee"));
        o.setOrderid(getLongValue(ctx, "$.orderId"));
        o.setOrderstatus(getStringValue(ctx, "$.orderStatus"));
        o.setOrdertype(getStringValue(ctx, "$.orderType"));

        Object h = ctx.read("$.holding");
        if (h != null) {
            o.setHoldingHoldingid(holdingFromJson(JsonPath.parse(h.toString()), false));
        }

        return o;
    }

    private List<Order> ordersFromJson(ReadContext ctx) {
        ArrayList<Order> orders = new ArrayList<Order>();

        JSONArray as = ctx.read("$");
        for (int i = 0; i < as.size(); i++) {
            Order o = new Order();

        //    o.setHoldingHoldingid(getLongValue(ctx, "$.[" + i + "].accountId"));
            o.setQuoteid(getStringValue(ctx, "$.[" + i + "].quoteSymbol"));
            o.setQuantity(getBigDecimalValue(ctx, "$.[" + i + "].quantity"));
            o.setPrice(getBigDecimalValue(ctx, "$.[" + i + "].price"));
            o.setAccountid(getLongValue(ctx, "$.[" + i + "].accountId"));
            o.setCompletiondate(getDateValue(ctx, "$.[" + i + "].completionDate"));
            o.setOpendate(getDateValue(ctx, "$.[" + i + "].openDate"));
            o.setOrderfee(getBigDecimalValue(ctx, "$.[" + i + "].orderFee"));
            o.setOrderid(getLongValue(ctx, "$.[" + i + "].orderId"));
            o.setOrderstatus(getStringValue(ctx, "$.[" + i + "].orderStatus"));
            o.setOrdertype(getStringValue(ctx, "$.[" + i + "].orderType"));

            orders.add(o);
        }

        return orders;
    }
}