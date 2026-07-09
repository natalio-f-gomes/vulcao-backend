package dev.hope.vulcaobackend.RestControllers.Private;

import dev.hope.vulcaobackend.DTOs.*;
import dev.hope.vulcaobackend.Models.Business.WeeklySchedule;
import dev.hope.vulcaobackend.Properties.Address;
import dev.hope.vulcaobackend.Properties.EntityType;
import dev.hope.vulcaobackend.Properties.OrderStatus;
import dev.hope.vulcaobackend.Services.BusinessService;
import dev.hope.vulcaobackend.Services.ImageUploadService;
import dev.hope.vulcaobackend.Services.Shopping.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/business")
public class BusinessController {
    @Autowired
    BusinessService businessService;
    @Autowired
    ImageUploadService imageUploadService;
    @Autowired
    OrderService orderService;

    @PostMapping("/register")
    public ResponseEntity<?> registerBusiness(@RequestBody BusinessDTO newBusiness) {
        System.out.println("registerBusiness endpoint reached");
        String id = businessService.save(newBusiness);
        return imageUploadService.requestPresignedUrls(EntityType.BUSINESS, id,  newBusiness.getImagesMetadata());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBusiness(@PathVariable String id, @RequestBody BusinessDTO business) {
        System.out.println("registerBusiness endpoint reached");
        businessService.update(id, business);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/about/{id}")
    public ResponseEntity<?> updateBusinessAboutInfo(@PathVariable String id, @RequestBody BusinessAboutInfo info){
        businessService.updateAboutInfo(id, info);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/address/{id}")
    public ResponseEntity<?> updateBusinessAddress(@PathVariable String id,@RequestBody Address address){
        businessService.updateAddress(id, address);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/contact/{id}")
    public ResponseEntity<?> updateBusinessContact(@PathVariable String id,@RequestBody Contact contact){
        businessService.updateContact(id, contact);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/schedule/{id}")
    public ResponseEntity<?> updateBusinessSchedule( @PathVariable String id,@RequestBody WeeklySchedule schedule){
        businessService.updateSchedule(id, schedule);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{businessId}/get/orders")
    public ResponseEntity<?> FindOrders(@PathVariable String businessId){
        return ResponseEntity.ok(orderService.findAllBusinessOrders(businessId));
    }

    @PutMapping("/update/order/{trackId}/status")
    public ResponseEntity<?> UpdateOrder(@PathVariable String trackId, @RequestBody OrderStatus orderStatus){
        orderService.EmployerUpdateOrderStatus(trackId, orderStatus);
        return ResponseEntity.ok().build();
    }



}
