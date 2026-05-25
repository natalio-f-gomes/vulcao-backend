package dev.hope.vulcaobackend.Services.Offerings;


import dev.hope.vulcaobackend.Authentication.IPService;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.BookDTO;
import dev.hope.vulcaobackend.Mappers.OfferingsMapper;
import dev.hope.vulcaobackend.Models.AccountEntities.AuthModel;
import dev.hope.vulcaobackend.Models.AccountEntities.EmployerModel;
import dev.hope.vulcaobackend.Models.AccountEntities.StaffModel;
import dev.hope.vulcaobackend.Models.CompanyModel;
import dev.hope.vulcaobackend.Models.Offerings.BookModel;
import dev.hope.vulcaobackend.Models.Properties.Permissions;
import dev.hope.vulcaobackend.Repositories.AuthRepository;
import dev.hope.vulcaobackend.Repositories.CompanyRepository;
import dev.hope.vulcaobackend.Repositories.EmployerRepository;
import dev.hope.vulcaobackend.Repositories.Offerings.*;
import dev.hope.vulcaobackend.Repositories.StaffRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

@Slf4j
@Service
public class BookService {
    @Autowired private EmployerRepository employerRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private AuthRepository authRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private TVShowRepository tvShowRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private MusicRepository musicRepository;
    @Autowired private OfferingsMapper offeringsMapper;

    private static final CharSequence[] SYMBOLS = {
            "[", "]", "{", "}", ",", "=", "...",
            "@", "#", "$", "%", "^", "&", "*", "(", ")", "!",
            "/", "\\", "|", ":", ";", "?", "<", ">",
            "~", "`", "'", "\"", "_",
            "->", "=>", "::", "!=", "==", "<=", ">=", "&&", "||",
            "++", "--", "+=", "-=", "*=", "/=", "%=",
            "/*", "*/", "//", "/**"
    };
    public boolean isBookValid(BookModel book) {
        // Null check for the book object itself
        if (book == null) {
            throw new RuntimeException("Book object cannot be null");
        }

        // Name validation
        if (book.getName() == null || book.getName().isBlank()) {
            throw new RuntimeException("Book name cannot be null or empty");
        }
        if (book.getName().length() > 50) {
            throw new RuntimeException("Book name cannot be over 50 characters long");
        }

        // Description validation
        if (book.getDescription() == null || book.getDescription().isBlank()) {
            throw new RuntimeException("Book description cannot be null or empty");
        }
        if (book.getDescription().length() > 2500) {
            throw new RuntimeException("Book description cannot be over 2500 characters long");
        }

        // Price validation
        if (book.getPricePerUnit() == null) {
            throw new RuntimeException("Book price cannot be null");
        }
        if (book.getPricePerUnit() <= 0) {
            throw new RuntimeException("Book price must be greater than 0");
        }

        // Unit type, currency, stock validation
        if (book.getUnitType() == null) {
            throw new RuntimeException("Book unit type cannot be null");
        }
        if (book.getCurrency() == null) {
            throw new RuntimeException("Book currency cannot be null");
        }
        if (book.getStock() == null) {
            throw new RuntimeException("Book stock cannot be null");
        }
        if (book.getStock() < 0) {
            throw new RuntimeException("Book stock cannot be negative");
        }

        // Company validation
        if (book.getCompanyName() == null || book.getCompanyName().isBlank()) {
            throw new RuntimeException("Book must be associated with a company (company name required)");
        }
        if (book.getCompanyId() == null || book.getCompanyId().isBlank()) {
            throw new RuntimeException("Book must be associated with a company (company ID required)");
        }

        // SBN validation (assuming this is ISBN/identifier)
        if (book.getSBN() == null || book.getSBN().isBlank()) {
            throw new RuntimeException("Book SBN cannot be null or empty");
        }
        if (book.getSBN().length() > 50) {
            throw new RuntimeException("Book SBN cannot be over 50 characters long");
        }

        // Authors validation
        if (book.getAuthors() == null || book.getAuthors().isEmpty()) {
            throw new RuntimeException("Book must have at least one author");
        }
        for (String author : book.getAuthors()) {
            if (author == null || author.isBlank()) {
                throw new RuntimeException("Author name cannot be null or empty");
            }
            if (author.length() > 100) {
                throw new RuntimeException("Author name cannot exceed 100 characters");
            }
        }

        // Volume validation (if it's required)
        if (book.getVolume() == null || book.getVolume().isBlank()) {
            throw new RuntimeException("Book volume cannot be null or empty");
        }

        // Page number validation
        if (book.getPageNumber() == null) {
            throw new RuntimeException("Book page number cannot be null");
        }
        if (book.getPageNumber() <= 0) {
            throw new RuntimeException("Book page number must be greater than 0");
        }
        if (book.getPageNumber() > 10000) {
            throw new RuntimeException("Book page number cannot exceed 10000");
        }

        // Publisher validation
        if (book.getPublisher() == null || book.getPublisher().isBlank()) {
            throw new RuntimeException("Book publisher cannot be null or empty");
        }

        // Additional validations for other fields
        if (book.getEdition() != null && book.getEdition().length() > 50) {
            throw new RuntimeException("Book edition cannot exceed 50 characters");
        }

        if (book.getGenres() == null || book.getGenres().isEmpty()) {
            throw new RuntimeException("Book must have at least one genre");
        }

        if (book.getLanguages() == null || book.getLanguages().isEmpty()) {
            throw new RuntimeException("Book must have at least one language");
        }

        if (book.getFormat() == null) {
            throw new RuntimeException("Book format cannot be null");
        }

        if (book.getPublicationDate() == null) {
            throw new RuntimeException("Book publication date cannot be null");
        }
        if (book.getPublicationDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Book publication date cannot be in the future");
        }

        // Weight validation (optional but if present, should be valid)
        if (book.getWeight() != null && book.getWeight() <= 0) {
            throw new RuntimeException("Book weight must be greater than 0 if specified");
        }

        // Keywords validation (optional)
        if (book.getKeywords() != null) {
            for (String keyword : book.getKeywords()) {
                if (keyword != null && keyword.length() > 100) {
                    throw new RuntimeException("Keyword cannot exceed 100 characters");
                }
            }
        }

        log("BOOK " + book.getName() + " is Valid");
        return true;
    }

    public boolean canEmployerPerformAction(EmployerModel employer, HttpServletRequest request){
        String ip = IPService.getClientIpAddress(request);
        List<AuthModel> authData = authRepository.findByUserIpAddress(ip);
        if(authData.isEmpty()){throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");}
        for(AuthModel auth: authData){
            if(auth.getUsername().equals(employer.getUsername()) && auth.getEmail().equals(employer.getEmail())){
               return true;
            }
        }
        throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");
    }

    public boolean canStaffPerformAction(StaffModel staff, HttpServletRequest request, Permissions permission){
        String ip = IPService.getClientIpAddress(request);
        List<AuthModel> authData = authRepository.findByUserIpAddress(ip);
        if(authData.isEmpty()){throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");}
        if(!staff.getAssignedPermissions().contains(permission)){throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");}
        for(AuthModel auth: authData){
            if(auth.getUsername().equals(staff.getUsername()) && auth.getEmail().equals(staff.getEmail())){
                return true;
            }
        }
        throw new AccessDeniedException("You do not have the permission to add a new Product. Please login");
    }

    public void employerSaveBook(BookModel newBook, String sessionUser, HttpServletRequest request){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        CompanyModel company = companyRepository.findById(newBook.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newBook.setId(UUID.randomUUID().toString());
        newBook.setCompanyName(company.getName());
        newBook.setCompanyId(company.getId());
        newBook.setCreatorUsername(employer.getUsername());
        newBook.setCreatorId(employer.getId());
        newBook.onCreate();
        isBookValid(newBook);
        if(canEmployerPerformAction(employer, request)){bookRepository.save(newBook);}
    }

    public void staffSaveBook(BookModel newBook, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        CompanyModel company = companyRepository.findById(newBook.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        newBook.setId(UUID.randomUUID().toString());
        newBook.setCompanyName(company.getName());
        newBook.setCompanyId(company.getId());
        newBook.setCreatorUsername(staff.getUsername());
        newBook.setCreatorId(staff.getId());
        newBook.onCreate();
        isBookValid(newBook);
        if(canStaffPerformAction(staff, request, Permissions.ADD_PRODUCT)){bookRepository.save(newBook);}
        companyRepository.save(company);
    }


    public void employerUpdateBook(BookModel book, String sessionUser, HttpServletRequest request){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        CompanyModel company = companyRepository.findById(book.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        isBookValid(book);
        book.onUpdate();
        if(canEmployerPerformAction(employer, request)){bookRepository.update(book);}

    }

    public void staffUpdateBook(BookModel book, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        CompanyModel company = companyRepository.findById(book.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        isBookValid(book);
        book.onUpdate();
        if(canStaffPerformAction(staff, request, Permissions.UPDATE_PRODUCT)){bookRepository.update(book);}
    }

    public void employerDeleteBook(String bookId, String sessionUser, HttpServletRequest request){
        EmployerModel employer = employerRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no employer with this name"));
        BookModel book = bookRepository.findById(bookId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(book.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        if(canEmployerPerformAction(employer, request)){bookRepository.deleteBook(book);}
    }

    public void staffDeleteBook(String bookId, String sessionUser, HttpServletRequest request){
        StaffModel staff = staffRepository.findByUsername(sessionUser).orElseThrow(()-> new UsernameNotFoundException("There is no Staff with this name"));
        BookModel book = bookRepository.findById(bookId).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        CompanyModel company = companyRepository.findById(book.getCompanyId()).orElseThrow(()-> new RuntimeException("There is no company with this id"));
        if(canStaffPerformAction(staff, request, Permissions.DELETE_PRODUCT)){bookRepository.deleteBook(book);}
    }

    public List<BookDTO> findAll(){
        return bookRepository.findAll().stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByCompanyId(String companyId){
        return bookRepository.findByCompanyId(companyId).stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByCompanyName(String companyName){
        return bookRepository.findByCompanyId(companyName).stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksProductsById(String creatorId){
        return bookRepository.findByCreatorId(creatorId).stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByCreatorName(String creatorUsername){
        return bookRepository.findByCreatorId(creatorUsername).stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByCreatorId(String creatorUsername){
        return bookRepository. findByCreatorId(creatorUsername).stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

    public BookDTO findBooksById(String id){
        BookModel book = bookRepository.findById(id).orElseThrow(()-> new RuntimeException("There is no product with this id"));
        return offeringsMapper.transformBookModelToDTO(book);
    }


    public List<BookDTO> findBooksByPrice(Integer price){
        return bookRepository.findByPrice(price).stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByName(String companyName){
        return bookRepository.findByName(companyName).stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

    public List<BookDTO> findBooksByPublisher(String publisher){
        return bookRepository.findByPublisher(publisher).stream().map(offeringsMapper::transformBookModelToDTO).collect(Collectors.toList());
    }

}


