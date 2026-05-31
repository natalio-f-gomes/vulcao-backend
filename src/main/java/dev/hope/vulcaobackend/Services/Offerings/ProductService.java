package dev.hope.vulcaobackend.Services.Offerings;

import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Mappers.OfferingsMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.*;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import dev.hope.vulcaobackend.Services.GlobalAccessControl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {
    @Autowired private EmployerRepository employerRepository;
    @Autowired private StaffRepository staffRepository;

    @Autowired private BookRepository bookRepository;
    @Autowired private TVShowRepository tvShowRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private MusicRepository musicRepository;
    @Autowired private OfferingsMapper offeringsMapper;
    @Autowired private GlobalAccessControl globalAccessControl;




    public void employerSaveProduct(ProductModel newProduct, String sessionUser, HttpServletRequest request){
        System.out.println("EMPLOYER SAVE PRODUCT SERVICE REACHED "+ newProduct.getName());
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        CompanyModel company = companyRepository.findById(newProduct.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newProduct.setId(UUID.randomUUID().toString());
        newProduct.setCompanyName(company.getName());
        newProduct.setCreatorUsername(employer.getUsername());
        newProduct.setCreatorId(employer.getId());
        newProduct.onCreate();
        newProduct.setUpdatedBy(employer.getUsername());
        System.out.println("DEBUG ID: " + newProduct.getId());
        System.out.println("DEBUG CREATED_AT: " + newProduct.getCreatedAt());
        System.out.println("DEBUG UPDATED_AT: " + newProduct.getUpdatedAt());

        globalAccessControl.isProductValid(newProduct);
        productRepository.save(newProduct);
    }

    public void staffSaveProduct(ProductModel newProduct, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        if(!staff.getAssignedPermissions().contains(Permissions.ADD_PRODUCT)){
            throw new AccessDeniedException("STAFF " + staff.getUsername() + " Does not the the permissions to save produts");
        }
        CompanyModel company = companyRepository.findById(newProduct.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newProduct.setId(UUID.randomUUID().toString());
        newProduct.setCompanyName(company.getName());
        newProduct.setCreatorUsername(staff.getUsername());
        newProduct.setCreatorId(staff.getId());
        newProduct.setCompanyId(company.getId());
        newProduct.onCreate();
        globalAccessControl.isProductValid(newProduct);
        productRepository.save(newProduct);

    }

    public void employerUpdateProduct(String id, ProductModel updatedProduct, String sessionUser, HttpServletRequest request){
        if(!id.equals(updatedProduct.getId())){
            throw new RuntimeException("PRODUCT WITH ID DOES NOT MATCH THE ID IN THE PARAMETER: " + id);
        }
        // 1. Verify the product actually exists in DB first
        ProductModel product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("THERE IS NO PRODUCT WITH THIS ID"));
        EmployerModel employer = employerRepository.findByUsername(sessionUser)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));
        ProductModel productUpdated =  updateProduct(product, updatedProduct, employer.getUsername());
        globalAccessControl.isProductValid(product);

        productRepository.update(productUpdated);

    }

    public ProductModel updateProduct(ProductModel product, ProductModel updatedProduct, String username){
        product.setName(updatedProduct.getName());
        product.setProductType(updatedProduct.getProductType());
        product.setExpirationDate(updatedProduct.getExpirationDate());
        product.setPricePerUnit(updatedProduct.getPricePerUnit());
        product.setUnitType(updatedProduct.getUnitType());
        product.setDescription(updatedProduct.getDescription());
        product.setCurrency(updatedProduct.getCurrency());
        product.setStock(updatedProduct.getStock());
        product.setUpdatedBy(username);
        product.onUpdate();
        return product;
    }

    public void staffUpdateProduct(String id, ProductModel updatedProduct, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        if(!staff.getAssignedPermissions().contains(Permissions.UPDATE_PRODUCT)){
            throw new AccessDeniedException("STAFF " + staff.getUsername() + " Does not the the permissions to save produts");
        }
        CompanyModel company = companyRepository.findById(updatedProduct.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        ProductModel product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("THERE IS NO PRODUCT WITH THIS ID"));
        globalAccessControl.isProductValid(product);
        ProductModel productUpdated =  updateProduct(product, updatedProduct, staff.getUsername());

        product.onUpdate();
        productRepository.update(productUpdated);
    }

    public void employerDeleteProduct(String productId, String sessionUser, HttpServletRequest request){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        ProductModel product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(product.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        productRepository.deleteProduct(product);
    }

    public void staffDeleteProduct(String productId, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        if(!staff.getAssignedPermissions().contains(Permissions.DELETE_PRODUCT)){
            throw new AccessDeniedException("STAFF " + staff.getUsername() + " Does not the the permissions to save produts");
        }
        ProductModel product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(product.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        productRepository.deleteProduct(product);
    }

    public List<ProductDTO> findAll(){
        return productRepository.findAll().stream().map(offeringsMapper::transformToProductDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> findProductsByCompanyId(String companyId){
        return productRepository.findByCompanyId(companyId).stream().map(offeringsMapper::transformToProductDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> findProductsByCompanyName(String companyName){
        return productRepository.findByCompanyId(companyName).stream().map(offeringsMapper::transformToProductDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> findCreatorProductsByCreatorId(String creatorId){
        return productRepository.findByCreatorId(creatorId).stream().map(offeringsMapper::transformToProductDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> findProductsByCreatorName(String creatorUsername){
        return productRepository.findByCreatorUsername(creatorUsername).stream().map(offeringsMapper::transformToProductDTO).collect(Collectors.toList());
    }

    public ProductDTO findProductsById(String id){
        ProductModel product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        return offeringsMapper.transformToProductDTO(product);
    }

    public List<ProductDTO> findProductsByPrice(Double price){
        return productRepository.findByPrice(price).stream().map(offeringsMapper::transformToProductDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> findProductsByName(String companyName){
        return productRepository.findByName(companyName).stream().map(offeringsMapper::transformToProductDTO).collect(Collectors.toList());
    }

}


