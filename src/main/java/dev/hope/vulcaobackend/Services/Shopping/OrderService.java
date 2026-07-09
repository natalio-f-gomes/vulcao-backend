package dev.hope.vulcaobackend.Services.Shopping;

import dev.hope.vulcaobackend.Configuration.AWSSESConfig;
import dev.hope.vulcaobackend.DTOs.OrderRequest;
import dev.hope.vulcaobackend.DTOs.UserDTO;
import dev.hope.vulcaobackend.Models.Business.BusinessModel;
import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.Shopping.OrderItem;
import dev.hope.vulcaobackend.Models.Shopping.OrderModel;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Properties.OrderStatus;
import dev.hope.vulcaobackend.Properties.UserType;
import dev.hope.vulcaobackend.Repositories.BusinessRepository;
import dev.hope.vulcaobackend.Repositories.OfferingRepository;
import dev.hope.vulcaobackend.Repositories.OrderRepository;
import dev.hope.vulcaobackend.Repositories.UserRepository;
import dev.hope.vulcaobackend.Services.Account.ClientService;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import dev.hope.vulcaobackend.Services.GlobalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;


@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private OfferingRepository offeringRepository;

    private static final SecureRandom secureRandom = new SecureRandom();

    AWSSESConfig sesService;
    OrderTemplate orderTemplate;
    @Autowired
    private GlobalServices globalServices;
    @Autowired
    private UserRepository userRepository;

    public OrderService(AWSSESConfig service, OrderTemplate orderTemplate) {
        this.sesService = service;
        this.orderTemplate = orderTemplate;
    }


    public List<OrderItem> validateOrder(OrderRequest orderRequest, String businessId){

        List<OrderItem> orderItems = new ArrayList<>();
        if(orderRequest.getNote().length() > 500){
            throw new ApiException(HttpStatus.BAD_REQUEST, "As instruções da sua encomenda não podem ter mais de 500 caracteres.");
        }

        HashSet<String> set = new HashSet<>();
        for(OrderRequest.OrderItemRequest item: orderRequest.getItems()){
            set.add(item.id());
            System.out.println("Order Item Request: " + item.name());
            OfferingModel offering = offeringRepository.findById(item.id()).orElseThrow(
                    () -> new ApiException(HttpStatus.NOT_FOUND, "Esta oferta já não existe: " + item.name())
            );
            if(offering.getStock() == 0){
                throw new ApiException(HttpStatus.BAD_REQUEST, "Parece que o artigo " + item.name() + " já não tem stock disponível.");
            }
            if(!item.businessId().equals(businessId)){
                throw new ApiException(HttpStatus.BAD_REQUEST, "O ID da empresa não corresponde ao ID da empresa desta oferta.");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOffering(offering);
            orderItem.setQuantity(item.quantity());
            orderItem.setId(item.id());
            orderItems.add(orderItem);
        }
        System.out.println("SIZE OF THE SET:" + set.size());
        System.out.println("SIZE OF THE Orders:" + orderRequest.getItems().size());
        if(set.size() != orderRequest.getItems().size()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Ocorreu um problema com o seu pedido. Estamos a trabalhar para o resolver o mais depressa possível.");
        }
        return orderItems;
    }

    public Double calculateCost(List<OrderItem> orderItems){
        return orderItems.stream().mapToDouble(item -> item.getOffering().getPrice() * item.getQuantity()).sum();
    }

    public String generateId() {
        String id = "ORDER-" + UUID.randomUUID();
        return orderRepository.findById(id).isPresent() ? generateId() : id;
    }


    public String generateTrackId(String clientId){
        String randomTrackId = "CLIENTE-" + clientId.substring(5, 10).toUpperCase() + "ORD" + secureRandom.nextInt(10_000, 100_000);
        return orderRepository.findById(randomTrackId).isPresent() ? generateTrackId(clientId) : randomTrackId;
    }


    public void placeOrder(String businessId, OrderRequest orderRequest){
        UserDTO client =  clientService.getProfile();
        BusinessModel business = businessRepository.findById(businessId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhuma empresa associada a esta encomenda.")
        );
        List<OrderItem> orderItems =  validateOrder(orderRequest, businessId);
        OrderModel order = new OrderModel();
        String orderId = generateId();
        System.out.println("Order id: "+ orderId);
        String trackId = generateTrackId(client.getId());
        System.out.println("Track Id: " + trackId);
        Double totalCost = calculateCost(orderItems);
        System.out.println("Total Cost: "+ totalCost);


        order.setId(orderId);
        order.setNote(orderRequest.getNote());
        order.setStatus(OrderStatus.ENVIADO.toString());
        order.setTrackId(trackId);
        order.setTotalCost(totalCost);
        order.setBusinessId(business.getId());
        order.setClientId(client.getId());
        order.setItems(orderItems);
        orderRepository.save(order);

        sendNotification(client, business, order);

    }

    public void sendNotification(UserDTO client, BusinessModel business, OrderModel order) {
        System.out.println("=== sendNotification ===");
        for (OrderItem item : order.getItems()) {
            System.out.println("Item Name: " + item.getOffering().getName());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("---");
        }
        String clientNotification = "Notificação de encomenda do negocio " + business.getName();
        String userFullName = client.getFirstName() + " " + client.getLastName();
        String businessNotification = "Notificação de encomenda do cliente: " + userFullName;


        sesService.sendOrderNotification(client.getEmail(), orderTemplate.buildHtmlOrderEmailForClient(order,business), clientNotification);

        //send email to company

        sesService.sendOrderNotification(business.getEmail(), orderTemplate.buildHtmlOrderEmailForBusiness(order,client), businessNotification);
    }

    public OrderModel findByTrackId(String trackId) {
        UserDTO client =  clientService.getProfile();
        return orderRepository.findByTrackId(trackId).orElseThrow(()->
                new ApiException(HttpStatus.NOT_FOUND, "Não existe nenhuma encomenda com este código de rastreio")
        );
    }



    public void EmployerUpdateOrderStatus(String trackId, OrderStatus status){
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = globalServices.getEmployerByUsername(sessionUser);
        OrderModel order =  orderRepository.findByTrackId(trackId).orElseThrow(()->
                new ApiException(HttpStatus.NOT_FOUND, "Encomenda não encontrada")
        );
        UserModel clientModel = userRepository.findById(order.getClientId()).orElseThrow(
        () -> new ApiException(HttpStatus.NOT_FOUND, "We couldn't find a client associated witht his order")
                );
        UserDTO client = clientService.convertModelToDto(clientModel);

        BusinessModel business = businessRepository.findById(order.getBusinessId()).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "There is no business attached to this order"));

        if(order.getBusinessId().equals(business.getId()) && business.getCreatorId().equals(user.getId())){
            if(order.getCanceled() == true){
                throw new ApiException(HttpStatus.FORBIDDEN, "O cliente cancelou este pedido. Por favor, entre em contato diretamente com o cliente se tiver alguma dúvida sobre o pedido.");
            }
            order.setStatus(status.toString());
            orderRepository.update(order);
            String notification = business.getName() + " alterou o estado da encomenda para " + status;
            sesService.sendOrderNotification(client.getEmail(), orderTemplate.buildHtmlOrderEmailForClient(order,business), notification);
            return;
        }
        throw new ApiException(HttpStatus.FORBIDDEN, "Não tem permissão para cancelar esta encomenda");

    }


    public List<OrderModel> findAllClientOrders() {
        UserDTO client =  clientService.getProfile();
        return orderRepository.getLatestOrders(client.getId());
    }

    public OrderModel findOrderById(String orderId) {
        UserDTO client =  clientService.getProfile();
        return orderRepository.findById(orderId).orElseThrow(()->
                new ApiException(HttpStatus.NOT_FOUND, "Encomenda não encontrada")
        );
    }

    public List<OrderModel> findAllBusinessOrders(String businessId) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel employer = globalServices.getEmployerByUsername(sessionUser);
        BusinessModel business = businessRepository.findById(businessId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Este negócio não foi encontrado")
        );
        if(!business.getCreatorId().equals(employer.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Não tem permissão para aceder a esta página");
        }
       return  orderRepository.findByBusinessId(businessId);
    }

    public void ClientCancelOrder(String trackId) {
        UserDTO user = clientService.getProfile();
        OrderModel order =  orderRepository.findByTrackId(trackId).orElseThrow(()->
                new ApiException(HttpStatus.NOT_FOUND, "Encomenda não encontrada")
        );
        if(!order.getClientId().equals(user.getId())){
            throw new ApiException(HttpStatus.FORBIDDEN, "Não tem permissão para cancelar esta encomenda");
        }
        order.setStatus(OrderStatus.CANCELADO.toString());
        order.setCanceled(true);
        orderRepository.update(order);

        String businessNotification = "O cliente " + user.getFirstName() + " " + user.getLastName() + " cancelou o pedido: " + trackId;

        sesService.sendOrderNotification(user.getEmail(), orderTemplate.buildHtmlOrderCancelledEmailForBusiness(order,user), businessNotification);

    }
}
