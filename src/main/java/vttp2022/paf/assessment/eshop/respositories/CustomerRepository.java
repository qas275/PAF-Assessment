package vttp2022.paf.assessment.eshop.respositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.paf.assessment.eshop.models.Customer;
import static vttp2022.paf.assessment.eshop.respositories.Queries.*;

@Repository
public class CustomerRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	// You cannot change the method's signature
	public Optional<Customer> findCustomerByName(String name) {
		// TODO: Task 3 
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_CHECK_USER_EXISTS_BY_NAME, name);
		if(rs.next()){
			Customer res = Customer.createCustomer(rs);
			System.out.println(res.getName());
			return Optional.of(res);
		}else{
			return Optional.empty();
		}
	}
}
