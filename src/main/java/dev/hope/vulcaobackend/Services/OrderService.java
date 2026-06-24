package dev.hope.vulcaobackend.Services;

import dev.hope.vulcaobackend.Configuration.AWSSESConfig;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.UserFullName;
import dev.hope.vulcaobackend.Mappers.CompanyMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.AccountEntities.UserModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Offerings.WearableModel;
import dev.hope.vulcaobackend.Models.Properties.CvIslands;
import dev.hope.vulcaobackend.Models.Properties.OfferingType;
import dev.hope.vulcaobackend.Models.Properties.OrderStatus;
import dev.hope.vulcaobackend.Models.Shopping.CartItemModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderItemModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import dev.hope.vulcaobackend.Repositories.*;
import dev.hope.vulcaobackend.Repositories.Offerings.ProductRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.WearableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.mapper.Order;

import javax.swing.text.DateFormatter;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    private static final SecureRandom secureRandom = new SecureRandom();
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    private WearableRepository wearableRepository;

    AWSSESConfig sesService;
    OrderTemplate orderTemplate;

    @Autowired
    private CompanyMapper companyMapper;

    public OrderService(AWSSESConfig service, OrderTemplate orderTemplate) {
        this.sesService = service;
        this.orderTemplate = orderTemplate;
    }


    public ResponseEntity<?> validateOrderItems(List<CartItemModel> items, CompanyModel company, String userId) {
        for (CartItemModel item : items) {
            if (!item.getCompanyId().equals(company.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O item '" + item.getId() + "' não pertence à empresa selecionada.");
            }
            if (!item.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Incompatibilidade de autenticação: Não é o proprietário do item de carrinho '" + item.getId() + "'.");
            }
            if (item.getItemType() == OfferingType.PRODUCT) {
                ProductModel product = productRepository.findById(item.getId()).orElse(null);

                if (product == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("O produto '" + item.getId() + "' já não está disponível.");
                }

                if (item.getQuantity() > product.getStock()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Não é possível comprar " + item.getQuantity() + "x '" + product.getName() + "'. Apenas restam " + product.getStock() + " em stock.");
                }
            }
            if (item.getItemType() == OfferingType.WEARABLE) {
                WearableModel wearable = wearableRepository.findById(item.getId()).orElse(null);

                if (wearable == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("O produto '" + item.getId() + "' já não está disponível.");
                }

                if (item.getQuantity() > wearable.getStock()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Não é possível comprar " + item.getQuantity() + "x '" + wearable.getName() + "'. Apenas restam " + wearable.getStock() + " em stock.");
                }
            }
        }
        return ResponseEntity.accepted().build();
    }

    public Double calculateProductCost(String productId, Integer quantity) {
        ProductModel product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product with id " + productId + " not found"));
        return product.getPricePerUnit() * quantity;

    }

    public Double calculateWearableCost(String wearableId, Integer quantity) {
        WearableModel wearable = wearableRepository.findById(wearableId).orElseThrow(() -> new RuntimeException("Wearable with id " + wearableId + " not found"));
        return wearable.getPrice() * quantity;

    }

    public Double calculateOrderCost(List<CartItemModel> items) {
        Double totalCost = 0.0;
        for (CartItemModel item : items) {
            if (item.getItemType().equals(OfferingType.PRODUCT)) {
                totalCost += calculateProductCost(item.getId(), item.getQuantity());
            }
            if (item.getItemType().equals(OfferingType.WEARABLE)) {
                totalCost += calculateWearableCost(item.getId(), item.getQuantity());
            }

        }
        return totalCost;
    }

    public String generateTrackId(String userId) {
        //user first 4 id chars, 6 random generated numbers
        int min = 10_000;
        int max = 100_000;
        String randomTrackId = "USER-" + userId.substring(0, 4) + "ORD" + secureRandom.nextInt(min, max);
        boolean found = true;
        while (found) {
            boolean trackIdExists = orderRepository.findByTrackId(randomTrackId).isPresent();
            if (!trackIdExists) {
                found = false;
            }
        }
        return randomTrackId;
    }

    public String generateOrderId() {
        String randomId = UUID.randomUUID().toString();
        boolean found = true;
        while (found) {
            boolean orderExists = orderRepository.findById(randomId).isPresent();
            if (!orderExists) {
                found = false;
            }
        }
        return randomId;
    }

    public void saveOrderItem(CartItemModel cartItem, OrderModel order) {
        String itemName = "";
        Double price = 0.0;

        List<OrderItemModel> orderItems = order.getOrderItems();
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }

        if (cartItem.getItemType().equals(OfferingType.PRODUCT)) {
            ProductModel product = productRepository.findById(cartItem.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItem.getId()));
            itemName = product.getName();
            price = product.getPricePerUnit();
        } else if (cartItem.getItemType().equals(OfferingType.WEARABLE)) {
            WearableModel wearable = wearableRepository.findById(cartItem.getId())
                    .orElseThrow(() -> new RuntimeException("Wearable not found with id: " + cartItem.getId()));
            itemName = wearable.getName();
            price = wearable.getPrice();
        }

        OrderItemModel orderItem = new OrderItemModel();
        orderItem.setId(cartItem.getId());
        orderItem.setItemName(itemName);
        orderItem.setItemType(cartItem.getItemType());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setUserId(cartItem.getUserId());
        orderItem.setCompanyId(cartItem.getCompanyId());
        orderItem.setPriceAtPurchase(price);
        orderItem.onCreate();

        orderItemRepository.save(orderItem);

        // Add to the existing list
        orderItems.add(orderItem);

        // Update the order with the full list
        order.setOrderItems(orderItems);
        orderRepository.update(order);
    }

    public void sendNotification(String userEmail, String companyEmail, OrderModel order, String userNotification, String companyNotification) {
        System.out.println("=== ORDER ITEMS FROM DATABASE ===");
        for (OrderItemModel item : order.getOrderItems()) {
            System.out.println("Item ID: " + item.getId());
            System.out.println("Item Type: " + item.getItemType());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("---");
        }

        //send email to user
        List<OrderTemplate.ProductOrder> productOrderList = getProductOrderDetails(order.getOrderItems());
        List<OrderTemplate.WearableOrder> wearableOrderList = getWearableOrderDetails(order.getOrderItems());
        sesService.sendOrderNotification(userEmail, orderTemplate.buildHtmlOrderEmailForUser(order, productOrderList, wearableOrderList), userNotification);

        //send email to company
        sesService.sendOrderNotification(companyEmail, orderTemplate.buildHtmlOrderEmailForCompany(order, productOrderList, wearableOrderList), companyNotification);
    }


    public ResponseEntity<?> placeOrder(String sessionUser, OrderModel newOrder) {
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("Username: " + sessionUser + " not found"));
        if (!newOrder.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have the permissions to handle this operation");
        }
        CompanyModel company = companyRepository.findById(newOrder.getCompanyId()).orElseThrow(() -> new RuntimeException("Company With ID: " + newOrder.getCompanyId() + " not found"));
        ResponseEntity<?> response = validateOrderItems(newOrder.getCartItems(), company, user.getId());
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)) {
            return response;
        }
        newOrder.setStatus(OrderStatus.SENT);
        Double totalCost = calculateOrderCost(newOrder.getCartItems());
        newOrder.setId(generateOrderId());
        newOrder.setTotalCost(totalCost);
        newOrder.setTrackId(generateTrackId(user.getId()));
        newOrder.onCreate();
        orderRepository.save(newOrder);
        for (CartItemModel cartItem : newOrder.getCartItems()) {
            saveOrderItem(cartItem, newOrder);
        }
        String userFullName = user.getFirstName() + " " + user.getLastName();
        String userNotification = "Notificação de encomenda da empresa " + company.getName();
        String companyNotification = "Notificação de encomenda de: " + userFullName;
        sendNotification(user.getEmail(), company.getEmail(), newOrder, userNotification, companyNotification);
        return ResponseEntity.accepted().build();

    }

    public Optional<OrderItemModel> findOrderItemById(String itemId) {
        //TODO ADD GUARD RAILS OF WHO IS ALLOWED TO GET THE ITEM
        return orderItemRepository.findById(itemId);
    }


    private ResponseEntity<?> validateFindOrder(boolean isStaffPresent, boolean isEmployerPresent, boolean isUserPresent, String sessionUser, String companyId) {
        CompanyModel company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não existe nenhuma empresa com o ID: " + companyId);
        }

        if (!isStaffPresent && !isEmployerPresent && !isUserPresent) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Acesso negado: Nem o Utilizador, Funcionário ou Empregador associados existem para consultar este pedido.");
        }

        boolean found = false;

        if (isEmployerPresent) {
            EmployerModel employer = employerRepository.findByUsername(sessionUser).orElse(null);
            if (employer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Empregador com o nome de utilizador '" + sessionUser + "' não foi encontrado.");
            }

            List<CompanyDTO> companiesRegistered = employer.getCompaniesRegistered();
            for (CompanyDTO biz : companiesRegistered) {
                if (biz.getId().equals(company.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O empregador '" + employer.getUsername() + "' não é proprietário da empresa '" + company.getName() + "'.");
            }
        }

        if (isStaffPresent) {
            StaffModel staff = staffRepository.findByUsername(sessionUser).orElse(null);
            if (staff == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Funcionário com o nome de utilizador '" + sessionUser + "' não foi encontrado.");
            }

            if (!company.getId().equals(staff.getCompanyId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("O funcionário '" + staff.getUsername() + "' não pertence à empresa '" + company.getName() + "'.");
            }
        }

        // Se passou todas as validações com sucesso
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> findOrder(String companyId, String userId, String orderId, String sessionUser) {
        boolean isUserPresent = userRepository.findByUsername(sessionUser).isPresent();
        boolean isEmployerPresent = employerRepository.findByUsername(sessionUser).isPresent();
        boolean isStaffPresent = staffRepository.findByUsername(sessionUser).isPresent();
        ResponseEntity<?> response = validateFindOrder(isStaffPresent, isEmployerPresent, isUserPresent, sessionUser, companyId);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)) {
            return response;
        }
        return ResponseEntity.ok(orderRepository.findOrder(companyId, userId, orderId).orElseThrow(() -> new RuntimeException("Order with this company id and user Id does not exist")));
    }

    public ResponseEntity<?> findOrderByTrackId(String companyId, String sessionUser, String trackId) {
        boolean isUserPresent = userRepository.findByUsername(sessionUser).isPresent();
        boolean isEmployerPresent = employerRepository.findByUsername(sessionUser).isPresent();
        boolean isStaffPresent = staffRepository.findByUsername(sessionUser).isPresent();
        ResponseEntity<?> response = validateFindOrder(isStaffPresent, isEmployerPresent, isUserPresent, sessionUser, companyId);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.ACCEPTED)) {
            return response;
        }
        OrderModel order = orderRepository.findByTrackId(trackId).orElseThrow(() -> new RuntimeException("Order with this company id and user Id does not exist"));
        return ResponseEntity.ok(order);
    }

    public List<OrderModel> findAllCompanyAndUserOrders(String sessionUser, String userId, String companyId) {
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(() -> new RuntimeException("THIS USER DOES NOT EXISTS"));
        if (!user.getId().equals(userId)) {
            throw new AccessDeniedException("ACCESS DENIED, THE USER LOGGED IN DOES NOT MATCH THE ID IN THE DATABASE");
        }
        return orderRepository.findAllCompanyAndUserOrders(userId, companyId);
    }

    public List<OrderModel> staffFindCompanyOrders(String sessionUser, String companyId) {
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("THERE IS NO STAFF WITH THIS USERNAME: " + sessionUser));
        if (!staff.getCompanyId().equals(companyId)) {
            throw new AccessDeniedException("THIS STAFF " + staff.getUsername() + " does not have access to view this company data");
        }
        return orderRepository.findAllCompanyOrders(companyId);
    }

    public List<OrderModel> employerFindCompanyOrders(String sessionUser, String companyId) {
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(() -> new UsernameNotFoundException("THERE IS NO EMPLOYER WITH THIS USERNAME: " + sessionUser));
        List<CompanyDTO> companies = employer.getCompaniesRegistered();
        for (CompanyDTO company : companies) {
            if (company.getId().equals(companyId)) {
                return orderRepository.findAllCompanyOrders(companyId);
            }
        }
        throw new AccessDeniedException("THE EMPLOYER: " + sessionUser + " does not have access to view this company: " + companyId);

    }


    public List<OrderModel> findAllCompanyOrders(String sessionUser, String companyId) {
        boolean doesStaffExists = staffRepository.findByUsername(sessionUser).isPresent();
        boolean doesEmployerExists = employerRepository.findByUsername(sessionUser).isPresent();
        if (!doesEmployerExists && !doesStaffExists) {
            throw new AccessDeniedException("YOU DO NOT HAVE THE PERMISSIONS TO ACCESS THIS DATA.");
        }
        if (doesStaffExists) {
            return staffFindCompanyOrders(sessionUser, companyId);
        }
        return employerFindCompanyOrders(sessionUser, companyId);

    }


    public List<OrderModel> findAllUserOrders(String sessionUser, String userId) {
        UserModel user = userRepository.findByUsername(sessionUser).orElseThrow(() -> new RuntimeException("THIS USER DOES NOT EXISTS"));
        if (!user.getId().equals(userId)) {
            throw new AccessDeniedException("ACCESS DENIED, THE USER LOGGED IN DOES NOT MATCH THE ID IN THE DATABASE");
        }
        return orderRepository.findAllUserOrders(userId);
    }

    public OrderModel employerUpdateOrder(String username, OrderModel order, OrderStatus status) {
        EmployerModel employer = employerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Employer with this name does not exist: " + username));
        List<CompanyDTO> companies = employer.getCompaniesRegistered();
        for (CompanyDTO company : companies) {
            if (company.getId().equals(order.getCompanyId())) {
                order.setStatus(status);
                orderRepository.update(order);

                String userFullName = employer.getFirstName() + " " + employer.getLastName();
                String userNotification = "A empresa " + company.getName() + " alterou o status do seu pedido.";
                String companyNotification = "O status do pedido de " + userFullName + " foi alterado.";
                sendNotification(employer.getEmail(), company.getEmail(), order, userNotification, companyNotification);
                return order;
            }
        }


        throw new AccessDeniedException("THIS EMPLOYER DOES NOT OWN  THE COMPANY: " + order.getCompanyId());
    }

    public OrderModel staffUpdateOrder(String username, OrderModel order, OrderStatus status) {
        StaffModel staff = staffRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Employer with this name does not exist: " + username));
        if (staff.getCompanyId().equals(order.getCompanyId())) {
            order.setStatus(status);
            orderRepository.update(order);
            return order;
        }
        throw new AccessDeniedException("THIS STAFF DOES NOT WORK FOR THE COMPAMY: " + order.getCompanyId());

    }

    public OrderModel updateStatus(String orderId, OrderStatus status, String sessionUser) {
        OrderModel order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("ORDER NOT FOUND"));
        UserModel user = userRepository.findById(order.getUserId()).orElseThrow(() -> new RuntimeException("THE ORDER UserID does not match any user in the database: "));
        CompanyModel company = companyRepository.findById(order.getCompanyId()).orElseThrow(() -> new RuntimeException("THE ORDER UserID does not match any user in the database: "));

        boolean employerLoggedIn = employerRepository.findByUsername(sessionUser).isPresent();

        boolean staffLoggedIn = staffRepository.findByUsername(sessionUser).isPresent();

        if (!staffLoggedIn && !employerLoggedIn) {
            throw new AccessDeniedException("NEITHER Staff or employer are logged in");
        }
        if (staffLoggedIn) {
            return staffUpdateOrder(sessionUser, order, status);

        }

        return employerUpdateOrder(sessionUser, order, status);
    }

    public List<OrderTemplate.ProductOrder> getProductOrderDetails(List<OrderItemModel> orderItems) {
        System.out.println("Products from order");
        List<OrderTemplate.ProductOrder> products = new ArrayList<>();
        for (OrderItemModel item : orderItems) {
            System.out.println("Product Offering type: " + item.getItemType());
            if (item.getItemType().equals(OfferingType.PRODUCT)) {
                ProductModel product = productRepository.findById(item.getId())
                        .orElseThrow(() -> new RuntimeException("Product in this order not found with id: " + item.getId()));
                System.out.println("Product name " + product.getName());
                products.add(new OrderTemplate.ProductOrder(product, item.getQuantity()));
            }
        }
        return products;
    }

    public List<OrderTemplate.WearableOrder> getWearableOrderDetails(List<OrderItemModel> orderItems) {
        System.out.println("Products from order");
        List<OrderTemplate.WearableOrder> wearables = new ArrayList<>();
        for (OrderItemModel item : orderItems) {
            System.out.println("Wearable Offering type: " + item.getItemType());
            if (item.getItemType().equals(OfferingType.WEARABLE)) {
                WearableModel wearable = wearableRepository.findById(item.getId())
                        .orElseThrow(() -> new RuntimeException("Wearable in this order not found with id: " + item.getId()));
                wearables.add(new OrderTemplate.WearableOrder(wearable, item.getQuantity()));
            }
        }
        return wearables;
    }

}
