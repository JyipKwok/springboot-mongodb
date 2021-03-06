package com.example.services.mongodb.impl;

import com.example.entities.mongodb.Customer;
import com.example.repositories.mongodb.CustomerMongoRepository;
import com.example.repositories.mongodb.CustomerPageAndSortRepository;
import com.example.services.mongodb.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

/**
 * @author:wangdong
 * @description:
 */
@Service
public class MongoServiceImpl implements MongoService {

    @Autowired
    private CustomerMongoRepository customerMongoRepository;

    @Autowired
    private CustomerPageAndSortRepository customerPageAndSortRepository;

    /**
     * 保存客户信息
     *
     *
     * @param id
     * @param lastName
     * @param firstName
     * @return
     */
    @Override
    public Boolean saveCustomer(String id, String lastName, String firstName) {
        if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName)) {
            return false;
        }

        Long ctime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        Long utime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

        Customer customer = new Customer(firstName, lastName, ctime, utime);
        if (!StringUtils.isEmpty(id)){
            customer.setId(id);
        }
        Customer result = customerMongoRepository.save(customer);
        if (null == result) {
            return false;
        }
        return true;
    }

    /**
     * 根据第一个名字查询
     *
     * @param firstName
     * @return
     */
    @Override
    public List<Customer> listCustomerByFirstName(String firstName) {
        if (StringUtils.isEmpty(firstName)) {
            return Collections.EMPTY_LIST;
        }
        List<Customer> customers = customerMongoRepository.findByFirstName(firstName);
        if (CollectionUtils.isEmpty(customers)) {
            return Collections.EMPTY_LIST;
        }

        return customers;
    }

    /**
     * 根据最后一个名字查询
     *
     * @param lastName
     * @return
     */
    @Override
    public List<Customer> listCustomerByLastName(String lastName) {
        if (StringUtils.isEmpty(lastName)) {
            return Collections.EMPTY_LIST;
        }
        List<Customer> customers = customerMongoRepository.findByLastName(lastName);
        if (CollectionUtils.isEmpty(customers)) {
            return Collections.EMPTY_LIST;
        }

        return customers;
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @Override
    public List<Customer> getCustomer(String id) {
        if (StringUtils.isEmpty(id)) {
            return Collections.EMPTY_LIST;
        }
        List<Customer> customers = customerMongoRepository.findAllById(id);
        if (CollectionUtils.isEmpty(customers)) {
            return Collections.EMPTY_LIST;
        }

        return customers;
    }

    /**
     * 查询所有，不需要任何条件
     *
     * @return
     */
    @Override
    public List<Customer> listCustomer() {

        List<Customer> customers = customerMongoRepository.findAll();


        if (CollectionUtils.isEmpty(customers)) {
            return Collections.EMPTY_LIST;
        }

        return customers;
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Customer> listCustomer(Integer pageNum, Integer pageSize) {
        if (null == pageNum || null == pageSize) {
            return Collections.EMPTY_LIST;
        }

        Pageable pageable = new PageRequest(pageNum, pageSize);

        Page<Customer> customerPage = customerPageAndSortRepository.findAll(pageable);
        if (null == customerPage) {
            return Collections.EMPTY_LIST;
        }
        return customerPage.getContent();
    }

    /**
     * 分页查询
     * 根据指定的条件排序
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @return
     */
    @Override
    public List<Customer> listCustomer(Integer pageNum, Integer pageSize, Sort sort) {
        if (null == pageNum || null == pageSize || null == sort) {
            return Collections.EMPTY_LIST;
        }
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<Customer> customerPage = customerPageAndSortRepository.findAll(pageable);

        if (null == customerPage) {
            return Collections.EMPTY_LIST;
        }
        return customerPage.getContent();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteCustomer(String id) {

        customerMongoRepository.deleteById(id);


        return true;
    }

    /**
     * 删除
     *
     * @param customer
     * @return
     */
    @Override
    public Boolean deleteCustomer(Customer customer) {

        customerMongoRepository.delete(customer);

        return true;
    }

    /**
     * 根据ids查询
     *
     * @param ids
     * @return
     */
    @Override
    public List<Customer> listCustomers(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        List<Customer> customers = customerMongoRepository.findAllByIdIn(ids);

        return CollectionUtils.isEmpty(customers) ? Collections.EMPTY_LIST : customers;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean deleteCustomer(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }

        List<Customer> customers = listCustomers(ids);

        customerMongoRepository.deleteAll(customers);


        return true;
    }

    /**
     * 有一下满足条件的有多少条
     *
     * @param firstName
     * @return
     */
    @Override
    public Long countCustomerByFirstName(String firstName) {
        if (StringUtils.isEmpty(firstName)){
            return null;
        }

        Long result = customerMongoRepository.countAllByFirstName(firstName);

        return result;
    }


}
