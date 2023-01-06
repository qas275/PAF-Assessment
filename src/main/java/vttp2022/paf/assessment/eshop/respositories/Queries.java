package vttp2022.paf.assessment.eshop.respositories;

public class Queries {
    
    public static final String SQL_CHECK_USER_EXISTS_BY_NAME= "select * from eshop.customers where name = ?";

    public static final String SQL_INSERT_ORDER= "insert into eshop.order(orderId, name, address, email, orderDate) values(?,?,?,?,?);";
    
    public static final String SQL_INSERT_LINE_ITEM= "insert into eshop.line_items(item, quantity, orderId) values(?,?,?);";

    public static final String SQL_INSERT_STATUS= "insert into eshop.order_status(order_id, delivery_id, status, status_update) values(?,?,?,?);";
    public static final String SQL_COUNT_DISPATCHED= "select count(orderId) dispatched from eshop.order o join eshop.order_status os on o.orderId = os.order_id where os.status = 'dispatched' and name = ?";
    public static final String SQL_COUNT_PENDING= "select count(orderId) pending from eshop.order o join eshop.order_status os on o.orderId = os.order_id where os.status = 'pending' and name = ?";

}
