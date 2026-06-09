package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import dev.hope.vulcaobackend.Models.Properties.OrderStatus;
import dev.hope.vulcaobackend.Models.Shopping.CartItemModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import dev.hope.vulcaobackend.Repositories.*;
import dev.hope.vulcaobackend.Repositories.Offerings.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    @Autowired private  UserRepository userRepository;
    @Autowired private  CompanyRepository companyRepository;
    @Autowired private  ProductRepository productRepository;
    @Autowired  OrderRepository orderRepository;
    private static final SecureRandom secureRandom = new SecureRandom();
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private StaffRepository staffRepository;


    public boolean validateCartItems(List<CartItemModel> items, CompanyModel company, String userId) {

        for (CartItemModel item : items) {
            if (!item.getCompanyId().equals(company.getId())) {
                throw new OrderValidationException("Item '" + item.getId() + "' does not belong to the selected company.");
            }
            if (!item.getUserId().equals(userId)) {
                throw new OrderValidationException("Authentication mismatch: You do not own the cart item for '" + item.getId() + "'.");
            }
            if (item.getItemType() == OfferingType.PRODUCT) {
                ProductModel product = productRepository.findById(item.getId())
                        .orElseThrow(() -> new OrderValidationException("The product '" + item.getId() + "' is no longer available."));

                if (item.getQuantity() > product.getStock()) {
                    throw new OrderValidationException("Cannot purchase " + item.getQuantity() + "x '" + product.getName() + "'. Only " + product.getStock() + " left in stock.");
                }
            }
        }
        return true;
    }

    public Double calculateProductCost(String productId, Integer quantity){
        ProductModel product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product with id "+ productId + " not found"));
        return product.getPricePerUnit() * quantity;

    }

    public Double calculateOrderCost(List<CartItemModel> items){
        Double totalCost = 0.0;
        for(CartItemModel item: items){
            if(item.getItemType().equals(OfferingType.PRODUCT)){
                totalCost += calculateProductCost(item.getId(), item.getQuantity());
            }

        }
        return totalCost;
    }

    public String generateTrackId(String userId) {
        //user first 4 id chars, 6 random generated numbers
        return "USER#" + userId.substring(0, 5) + "ORD" + secureRandom.nextInt(100_000) + 900_000;
    }


    public void placeOrder(String sessionUser,  OrderModel newOrder){
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("Username: "+ sessionUser + " not found"));
        if(!newOrder.getUserId().equals(user.getId())){
            throw new AccessDeniedException("You do not have the permissions to handle this operation");
        }
        CompanyModel company = companyRepository.findById(newOrder.getCompanyId()).orElseThrow(()-> new RuntimeException("Company With ID: "+ newOrder.getCompanyId() + " not found"));
        validateCartItems(newOrder.getItems(), company, user.getId());
        newOrder.setStatus(OrderStatus.SENT);
        Double totalCost = calculateOrderCost(newOrder.getItems());
        newOrder.setTotalCost(totalCost);
        newOrder.setTrackId(generateTrackId(user.getId()));

        orderRepository.save(newOrder);
    }

    private boolean validateFindOrder(boolean isStaffPresent,boolean isEmployerPresent, boolean isUserPresent,String sessionUser, String companyId ){
        CompanyModel company = companyRepository.findById(companyId).orElseThrow(()-> new RuntimeException("Ther eis no company with this id: " + companyId));
        if(!isStaffPresent && !isEmployerPresent && !isUserPresent){
            throw new OrderValidationException("Neither the User, staff of employer exists to get this order");
        }
        boolean found = false;

        if(isEmployerPresent){
            EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("Employer with username: "+ sessionUser + " not found"));
            List<String> companyIds =  employer.getCompaniesRegistered();
            for (String id : companyIds) {
                if (id.equals(company.getId())) {
                    found = true;
                    break;
                }
            }
            if(!found){throw new OrderValidationException("The employer " + employer.getUsername() + " does not own the company " + company.getName());}
        }
        if(isStaffPresent){
            StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("Employer with username: "+ sessionUser + " not found"));
            if (!company.getId().equals(staff.getCompanyId())) {
                throw new AccessDeniedException("The staff " + staff.getUsername() + " does not belong to the company "+ company.getName());
            }
        }
        return true;
    }

    public OrderModel findOrder(String companyId, String userId, String sessionUser){
        boolean isUserPresent = userRepository.findByUsername(sessionUser).isPresent();
        boolean isEmployerPresent = employerRepository.findByUsername(sessionUser).isPresent();
        boolean isStaffPresent = staffRepository.findByUsername(sessionUser).isPresent();
        validateFindOrder(isStaffPresent, isEmployerPresent, isUserPresent, sessionUser, companyId);
        return orderRepository.findOrder(companyId, userId).orElseThrow(()-> new RuntimeException("Order with this company id and user Id does not exist"));
    }

    public OrderModel findOrderByTrackId(String companyId, String sessionUser, String trackId){
        boolean isUserPresent = userRepository.findByUsername(sessionUser).isPresent();
        boolean isEmployerPresent = employerRepository.findByUsername(sessionUser).isPresent();
        boolean isStaffPresent = staffRepository.findByUsername(sessionUser).isPresent();
        validateFindOrder(isStaffPresent, isEmployerPresent, isUserPresent, sessionUser, companyId);
        return orderRepository.findByTrackId(trackId).orElseThrow(()-> new RuntimeException("Order with this company id and user Id does not exist"));
    }
}
