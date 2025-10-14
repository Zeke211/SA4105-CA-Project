package sg.iss.javaspring.ca.checkout.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.iss.javaspring.ca.checkout.model.Customer;
import sg.iss.javaspring.ca.checkout.repository.CustomerRepository;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = false)
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
