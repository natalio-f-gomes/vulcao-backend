package dev.hope.vulcaobackend;


import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.EmployerDTO;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.StaffDTO;
import dev.hope.vulcaobackend.DataTransferObjects.AccountDTOs.UserDTO;
import dev.hope.vulcaobackend.DataTransferObjects.CompanyDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.BookDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MovieDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MusicDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Models.Cart.CartItemModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


public class RequestHandler {
    private int port;

    private static final String userRegisterEndpoint ="/api/v1/auth/user/register";
    private static final String userLoginEndpoint ="/api/v1/auth/user/login";
    private static final String userLogoutEndpoint ="/api/v1/auth/user/logout";

    private static final String employerRegisterEndpoint ="/api/v1/auth/employer/register";
    private static final String employerLoginEndpoint ="/api/v1/auth/employer/login";
    private static final String employerLogoutEndpoint ="/api/v1/auth/employer/logout";

    private static final String staffRegisterEndpoint ="/api/v1/auth/staff/register";
    private static final String staffLoginEndpoint ="/api/v1/auth/staff/login";
    private static final String staffLogoutEndpoint ="/api/v1/auth/staff/logout";
    WebClient webClient = WebClient.create();



    private RestTestClient restTestClient;
    public RequestHandler(RestTestClient restTestClient) {
        this.restTestClient = restTestClient;
    }



    public RestTestClient.BodyContentSpec userRegisterRequest(String entity, int status,int port){
        String URL = "http://localhost:%d".formatted(port);
        return restTestClient.post().uri(URL + userRegisterEndpoint)
                .header("Content-Type", "application/json")
                .body(entity).exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }


    public String userLoginRequest ( String entity, int status, int port){
        String URL = "http://localhost:%d".formatted(port);
        var cookies =  restTestClient.post().uri(URL + userLoginEndpoint)
                .header("Content-Type", "application/json")
                .body(entity)
                .exchange()
                .expectStatus().isEqualTo(status)
                .returnResult(String.class)
                .getResponseCookies();
        var authCookie = cookies.getFirst("AUTH_TOKEN");
        return authCookie != null ? authCookie.getValue() : null;
    }

    public void userLogoutRequest(String token, int status,int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + userLogoutEndpoint)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token).exchange()
                .expectStatus().isEqualTo(status);
    }

    public RestTestClient.BodyContentSpec employerRegisterRequest(String entity, int status,int port){
        String URL = "http://localhost:%d".formatted(port);
        return restTestClient.post().uri(URL + employerRegisterEndpoint)
                .header("Content-Type", "application/json")
                .body(entity).exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public String employerLoginRequest(String entity, int status, int port) {
        String URL = "http://localhost:%d".formatted(port);
        var cookies = restTestClient.post().uri(URL + employerLoginEndpoint)
                .header("Content-Type", "application/json")
                .body(entity)
                .exchange()
                .expectStatus().isEqualTo(status)
                .returnResult(String.class)
                .getResponseCookies();

        var authCookie = cookies.getFirst("AUTH_TOKEN");
        return authCookie != null ? authCookie.getValue() : null;
    }

    public void employerLogoutRequest(String token, int status,int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + employerLogoutEndpoint)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .exchange()
                .expectStatus().isEqualTo(status);
    }

    public RestTestClient.BodyContentSpec staffRegisterRequest ( String entity, int status,String token, int port){
        String URL = "http://localhost:%d".formatted(port);
        return restTestClient.post().uri(URL + staffRegisterEndpoint)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(entity).exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public String staffLoginRequest ( String entity, int status, int port){
        String URL = "http://localhost:%d".formatted(port);
        var cookie =  restTestClient.post().uri(URL + staffLoginEndpoint)
                .header("Content-Type", "application/json")
                .body(entity)
                .exchange()
                .expectStatus().isEqualTo(status)
                .returnResult(String.class)
                .getResponseCookies();
        var authCookie = cookie.getFirst("AUTH_TOKEN");
        return authCookie != null ? authCookie.getValue() : null;
    }

    public void staffLogoutRequest(String token, int status,int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + staffLogoutEndpoint)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .exchange()
                .expectStatus().isEqualTo(status);
    }

    public void registerCompany(String token, String company,int status, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + "/api/v1/company/register")
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(company)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public void employerUpdateProduct(String token, String product,String productId, int status, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.put().uri(URL + "/api/v1/offering/product/employer/update/"+ productId)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(product)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    // OFFERINGS PUBLIC ENDPOINT]
    private static final String BASE = "/api/v1/public/offering";

    private String baseUrl(int port) {
        return "http://localhost:%d".formatted(port) + BASE;
    }

    // ========== PRODUCT ==========
    public List<ProductDTO> getAllProducts(int port) {
        return webClient.get().uri(baseUrl(port) + "/product/get/all")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductDTO>>() {})
                .block();
    }

    public List<ProductDTO> getProductByCompanyId(String companyId, int port) {
        return webClient.get().uri(baseUrl(port) + "/product/company/" + companyId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductDTO>>() {})
                .block();
    }

    public List<ProductDTO> getProductByCompanyName(String companyName, int port) {
        return webClient.get().uri(baseUrl(port) + "/product/company/name/" + companyName)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductDTO>>() {})
                .block();
    }

    public List<ProductDTO> getProductsByCreatorId(String creatorId, int port) {
         return webClient.get().uri(baseUrl(port) + "/product/creatorId/" + creatorId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    return response.bodyToMono(String.class).flatMap(body -> {
                        // This 'body' usually contains the specific validation error message
                        System.err.println("Error Body: " + body);
                        return Mono.error(new RuntimeException("Bad Request: " + body));
                    });
                })
                .bodyToMono(new ParameterizedTypeReference<List<ProductDTO>>() {})
                .block();
    }

    public List<ProductDTO> getProductsByCreatorUsername(String username, int port) {
        return webClient.get().uri(baseUrl(port) + "/product/creator/username/" + username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductDTO>>() {})
                .block();
    }

    public ProductDTO getProductById(String id, int port) {
        return webClient.get().uri(baseUrl(port) + "/product/get/" + id)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();
    }

    public List<ProductDTO> getProductsByName(String name, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/product/search/name")
                        .queryParam("name", name).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductDTO>>() {})
                .block();
    }

    public List<ProductDTO> getProductsByPrice(Double price, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/product/search/price")
                        .queryParam("price", price).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductDTO>>() {})
                .block();
    }

    // ========== MOVIE ==========
    public List<MovieDTO> getAllMovies(int port) {
        return webClient.get().uri(baseUrl(port) + "/movie/get/all")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MovieDTO>>() {})
                .block();
    }

    public MovieDTO getMovieById(String id, int port) {
        return webClient.get().uri(baseUrl(port) + "/movie/" + id)
                .retrieve()
                .bodyToMono(MovieDTO.class)
                .block();
    }

    public List<MovieDTO> getMoviesByCompanyId(String companyId, int port) {
        return webClient.get().uri(baseUrl(port) + "/movie/company/" + companyId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MovieDTO>>() {})
                .block();
    }

    public List<MovieDTO> getMoviesByCompanyName(String companyName, int port) {
        return webClient.get().uri(baseUrl(port) + "/movie/company/name/" + companyName)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MovieDTO>>() {})
                .block();
    }

    public List<MovieDTO> getMoviesByCreatorId(String creatorId, int port) {
        return webClient.get().uri(baseUrl(port) + "/movie/creator/" + creatorId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MovieDTO>>() {})
                .block();
    }

    public List<MovieDTO> getMoviesByCreatorUsername(String username, int port) {
        return webClient.get().uri(baseUrl(port) + "/movie/creator/username/" + username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MovieDTO>>() {})
                .block();
    }

    public List<MovieDTO> getMoviesByName(String name, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/movie/search/name")
                        .queryParam("name", name).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MovieDTO>>() {})
                .block();
    }

    public List<MovieDTO> getMoviesByPrice(Integer price, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/movie/search/price")
                        .queryParam("price", price).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MovieDTO>>() {})
                .block();
    }

    // ========== MUSIC ==========
    public List<MusicDTO> getAllMusic(int port) {
        return webClient.get().uri(baseUrl(port) + "/music/get/all")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MusicDTO>>() {})
                .block();
    }

    public MusicDTO getMusicById(String id, int port) {
        return webClient.get().uri(baseUrl(port) + "/music/" + id)
                .retrieve()
                .bodyToMono(MusicDTO.class)
                .block();
    }

    public List<MusicDTO> getMusicByCompanyId(String companyId, int port) {
        return webClient.get().uri(baseUrl(port) + "/music/company/" + companyId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MusicDTO>>() {})
                .block();
    }

    public List<MusicDTO> getMusicByCompanyName(String companyName, int port) {
        return webClient.get().uri(baseUrl(port) + "/music/company/name/" + companyName)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MusicDTO>>() {})
                .block();
    }

    public List<MusicDTO> getMusicByCreatorId(String creatorId, int port) {
        return webClient.get().uri(baseUrl(port) + "/music/creator/" + creatorId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MusicDTO>>() {})
                .block();
    }

    public List<MusicDTO> getMusicByCreatorUsername(String username, int port) {
        return webClient.get().uri(baseUrl(port) + "/music/creator/username/" + username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MusicDTO>>() {})
                .block();
    }

    public List<MusicDTO> getMusicByName(String name, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/music/search/name")
                        .queryParam("name", name).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MusicDTO>>() {})
                .block();
    }

    public List<MusicDTO> getMusicByPrice(Integer price, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/music/search/price")
                        .queryParam("price", price).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MusicDTO>>() {})
                .block();
    }

    // ========== BOOK ==========
    public List<BookDTO> getAllBooks(int port) {
        return webClient.get().uri(baseUrl(port) + "/book/get/all")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookDTO>>() {})
                .block();
    }

    public BookDTO getBookById(String id, int port) {
        return webClient.get().uri(baseUrl(port) + "/book/" + id)
                .retrieve()
                .bodyToMono(BookDTO.class)
                .block();
    }

    public List<BookDTO> getBooksByCompanyId(String companyId, int port) {
        return webClient.get().uri(baseUrl(port) + "/book/company/" + companyId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookDTO>>() {})
                .block();
    }

    public List<BookDTO> getBooksByCompanyName(String companyName, int port) {
        return webClient.get().uri(baseUrl(port) + "/book/company/name/" + companyName)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookDTO>>() {})
                .block();
    }

    public List<BookDTO> getBooksByCreatorId(String creatorId, int port) {
        return webClient.get().uri(baseUrl(port) + "/book/creator/" + creatorId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookDTO>>() {})
                .block();
    }

    public List<BookDTO> getBooksByCreatorUsername(String username, int port) {
        return webClient.get().uri(baseUrl(port) + "/book/creator/username/" + username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookDTO>>() {})
                .block();
    }

    public List<BookDTO> getBooksByName(String name, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/book/search/name")
                        .queryParam("name", name).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookDTO>>() {})
                .block();
    }

    public List<BookDTO> getBooksByPrice(Integer price, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/book/search/price")
                        .queryParam("price", price).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookDTO>>() {})
                .block();
    }

    public List<BookDTO> getBooksByPublisher(String publisher, int port) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(BASE + "/book/search/publisher")
                        .queryParam("publisher", publisher).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookDTO>>() {})
                .block();
    }

    //COMPANIES
    public List<CompanyDTO> getAllCompanies(int port) {
        return webClient.get().uri(baseUrl(port) + "/public/company/get/all")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CompanyDTO>>() {})
                .block();
    }

    public List<CompanyDTO> getAllCompaniesByCreatorUsername(String creatorUsername, int port) {
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http").host("localhost").port(port)
                        .path(URL + "/public/compamy/get/creatorName")
                        .queryParam("creatorName", creatorUsername).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CompanyDTO>>() {
                })
                .block();
    }

    public CompanyDTO getCompanyById(String id, int port) {
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get().uri(URL + "/public/company/get/" + id)
                .retrieve()
                .bodyToMono(CompanyDTO.class)
                .block();
    }

    public CompanyDTO getCompanyByEmail(String email, int port) {
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get().uri(URL + "/api/v1/public/company/get/byEmail/" + email)
                .retrieve()
                .bodyToMono(CompanyDTO.class)
                .block();
    }

    public EmployerDTO getEmployerById(String id, int port) {
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get().uri( URL+ "/api/v1/public/employer/get/byId/" + id)
                .retrieve()
                .bodyToMono(EmployerDTO.class)
                .block();
    }

    public EmployerDTO getEmployerByUsername(String username, int port) {
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get().uri(URL+ "/api/v1/public/employer/get/byUsername/" + username)
                .retrieve()
                .bodyToMono(EmployerDTO.class)
                .block();
    }

    public StaffDTO getStaffByUsername(String username, int port) {
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get().uri(URL+ "/api/v1/public/staff/get/byUsername/" + username)
                .retrieve()
                .bodyToMono(StaffDTO.class)
                .block();
    }

    public void employerAddProductRequest(String token, int status, String newProduct, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + "/api/v1/offering/product/employer/add")
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(newProduct)
                .exchange()
                .expectStatus().isEqualTo(status);
    }

    public void staffAddProductRequest(String token, int status, String newProduct, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + "/api/v1/offering/product/staff/add")
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(newProduct)
                .exchange()
                .expectStatus().isEqualTo(status);
    }

    public void staffUpdateProduct(String token, String product,String productId, int status, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.put().uri(URL + "/api/v1/offering/product/staff/update/"+ productId)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(product)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }


    public void employerUpdateStaffPermissions(String token, String staffPermissions,String staffId, int status, int port){
        System.out.println("STAFF PERMISSIONS: "+ staffPermissions);
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.put().uri(URL + "/api/v1/staff/update/permissions/"+ staffId)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(staffPermissions)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public void updateStaffContact(String token, String contact,String staffUsername, int status, int port){
        System.out.println("STAFF CONTACT INFO: "+ contact);
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.put().uri(URL + "/api/v1/staff/update/"+ staffUsername + "/contact")
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(contact)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public void updateStaffCredentials(String token, String contact,String staffCredentials, int status, int port){
        System.out.println("STAFF CONTACT INFO: "+ contact);
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.put().uri(URL + "/api/v1/staff/update/"+ staffCredentials + "/creentials")
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(contact)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public void updateStaffAddress(String token, String contact,String staffAddress, int status, int port){
        System.out.println("STAFF CONTACT INFO: "+ contact);
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.put().uri(URL + "/api/v1/staff/update/"+ staffAddress + "/address")
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(contact)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public void updateStaffProfile(String token, String username,String staffProfile, int status, int port){
        System.out.println("STAFF CONTACT INFO: "+ staffProfile);
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.put().uri(URL + "/api/v1/staff/update/"+ username + "/profile")
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .body(staffProfile)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public void userAddItemToCart(String token, String productId, Integer productQuantity,  int status, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.post().uri(URL + "/api/v1/user/cart/add/product/" + productId + "/"+ productQuantity)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(System.out::println);
    }

    public List<CartItemModel>  getUserCart(String token, int port){
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get().uri(URL + "/api/v1/user/get/cart" )
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CartItemModel>>() {})
                .block();
    }


    public UserDTO getUserProfile(String token, int port){
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get().uri(URL + "/api/v1/user/get/my-profile")
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    public CartItemModel getCartItemById(String token, String itemId, int port) {
        String URL = "http://localhost:%d".formatted(port);
        return webClient.get().uri(URL + "/api/v1/user/get/cart/item/" + itemId)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .retrieve()
                .bodyToMono(CartItemModel.class)
                .block();
    }

    public void userDeletesItemFromCart(String token, String itemId, int status, int port){
        String URL = "http://localhost:%d".formatted(port);
        restTestClient.delete().uri(URL + "/api/v1/user/delete/cart/item/"+ itemId)
                .header("Content-Type", "application/json")
                .cookie("AUTH_TOKEN", token)
                .exchange()
                .expectStatus().isEqualTo(status);
    }




}


