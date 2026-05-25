package dev.hope.vulcaobackend.RestControllers.PublicEndpoints;


import dev.hope.vulcaobackend.DataTransferObjects.Offerings.BookDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MovieDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.MusicDTO;
import dev.hope.vulcaobackend.DataTransferObjects.Offerings.ProductDTO;
import dev.hope.vulcaobackend.Services.Offerings.BookService;
import dev.hope.vulcaobackend.Services.Offerings.MovieService;
import dev.hope.vulcaobackend.Services.Offerings.MusicService;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/public/offering")
@RestController
public class PublicOfferingsController {
    @Autowired private BookService bookService;
    @Autowired private MovieService movieService;
    @Autowired private MusicService musicService;
    @Autowired private ProductService productService;

    // ========== PRODUCT ENDPOINTS ==========
    @GetMapping("/product/get/all")
    public List<ProductDTO> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/product/company/{companyId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCompanyId(@PathVariable String companyId) {
        System.out.println("Get products from company: "+ companyId+ " requested");
        return ResponseEntity.ok(productService.findProductsByCompanyId(companyId));
    }

    @GetMapping("/product/company/name/{companyName}")
    public ResponseEntity<List<ProductDTO>> getProductsByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(productService.findProductsByCompanyName(companyName));
    }

    @GetMapping("/product/creatorId/{creatorId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCreatorId(@PathVariable String creatorId) {
        return ResponseEntity.ok(productService.findCreatorProductsByCreatorId(creatorId));
    }

    @GetMapping("/product/creator/username/{username}")
    public ResponseEntity<List<ProductDTO>> getProductsByCreatorUsername(@PathVariable String username) {
        return ResponseEntity.ok(productService.findProductsByCreatorName(username));
    }

    @GetMapping("/product/get/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findProductsById(id));
    }

    @GetMapping("/product/search/name")
    public ResponseEntity<List<ProductDTO>> getProductsByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.findProductsByName(name));
    }

    @GetMapping("/product/search/price")
    public ResponseEntity<List<ProductDTO>> getProductsByPrice(@RequestParam Double price) {
        return ResponseEntity.ok(productService.findProductsByPrice(price));
    }

    // ========== MOVIE ENDPOINTS ==========
    @GetMapping("/movie/get/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable String id) {
        return ResponseEntity.ok(movieService.findMovieById(id));
    }

    @GetMapping("/movie/company/{companyId}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCompanyId(@PathVariable String companyId) {
        return ResponseEntity.ok(movieService.findMoviesByCompanyId(companyId));
    }

    @GetMapping("/movie/company/name/{companyName}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(movieService.findMoviesByCompanyName(companyName));
    }

    @GetMapping("/movie/creator/{creatorId}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCreatorId(@PathVariable String creatorId) {
        return ResponseEntity.ok(movieService.findMoviesByCreatorId(creatorId));
    }

    @GetMapping("/movie/creator/username/{username}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCreatorUsername(@PathVariable String username) {
        return ResponseEntity.ok(movieService.findMoviesByCreatorName(username));
    }

    @GetMapping("/movie/search/name")
    public ResponseEntity<List<MovieDTO>> getMoviesByName(@RequestParam String name) {
        return ResponseEntity.ok(movieService.findMovieByName(name));
    }

    @GetMapping("/movie/search/price")
    public ResponseEntity<List<MovieDTO>> getMoviesByPrice(@RequestParam Integer price) {
        return ResponseEntity.ok(movieService.findMoviesByPrice(price));
    }

    // ========== MUSIC ENDPOINTS ==========
    @GetMapping("/music/get/all")
    public ResponseEntity<List<MusicDTO>> getAllMusic() {
        return ResponseEntity.ok(musicService.findAll());
    }

    @GetMapping("/music/{id}")
    public ResponseEntity<MusicDTO> getMusicById(@PathVariable String id) {
        return ResponseEntity.ok(musicService.findMusicById(id));
    }

    @GetMapping("/music/company/{companyId}")
    public ResponseEntity<List<MusicDTO>> getMusicByCompanyId(@PathVariable String companyId) {
        return ResponseEntity.ok(musicService.findMusicByCompanyId(companyId));
    }

    @GetMapping("/music/company/name/{companyName}")
    public ResponseEntity<List<MusicDTO>> getMusicByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(musicService.findMusicByCompanyName(companyName));
    }

    @GetMapping("/music/creator/{creatorId}")
    public ResponseEntity<List<MusicDTO>> getMusicByCreatorId(@PathVariable String creatorId) {
        return ResponseEntity.ok(musicService.findMusicByCreatorId(creatorId));
    }

    @GetMapping("/music/creator/username/{username}")
    public ResponseEntity<List<MusicDTO>> getMusicByCreatorUsername(@PathVariable String username) {
        return ResponseEntity.ok(musicService.findMusicByCreatorName(username));
    }

    @GetMapping("/music/search/name")
    public ResponseEntity<List<MusicDTO>> getMusicByName(@RequestParam String name) {
        return ResponseEntity.ok(musicService.findMusicByName(name));
    }

    @GetMapping("/music/search/price")
    public ResponseEntity<List<MusicDTO>> getMusicByPrice(@RequestParam Integer price) {
        return ResponseEntity.ok(musicService.findMusicByPrice(price));
    }

    // ========== BOOK ENDPOINTS ==========
    @GetMapping("/book/get/all")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.findBooksById(id));
    }

    @GetMapping("/book/company/{companyId}")
    public ResponseEntity<List<BookDTO>> getBooksByCompanyId(@PathVariable String companyId) {
        return ResponseEntity.ok(bookService.findBooksByCompanyId(companyId));
    }

    @GetMapping("/book/company/name/{companyName}")
    public ResponseEntity<List<BookDTO>> getBooksByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(bookService.findBooksByCompanyName(companyName));
    }

    @GetMapping("/book/creator/{creatorId}")
    public ResponseEntity<List<BookDTO>> getBooksByCreatorId(@PathVariable String creatorId) {
        return ResponseEntity.ok(bookService.findBooksByCreatorId(creatorId));
    }

    @GetMapping("/book/creator/username/{username}")
    public ResponseEntity<List<BookDTO>> getBooksByCreatorUsername(@PathVariable String username) {
        return ResponseEntity.ok(bookService.findBooksByCreatorName(username));
    }

    @GetMapping("/book/search/name")
    public ResponseEntity<List<BookDTO>> getBooksByName(@RequestParam String name) {
        return ResponseEntity.ok(bookService.findBooksByName(name));
    }

    @GetMapping("/book/search/price")
    public ResponseEntity<List<BookDTO>> getBooksByPrice(@RequestParam Integer price) {
        return ResponseEntity.ok(bookService.findBooksByPrice(price));
    }

    @GetMapping("/book/search/publisher")
    public ResponseEntity<List<BookDTO>> getBooksByPublisher(@RequestParam String publisher) {
        return ResponseEntity.ok(bookService.findBooksByPublisher(publisher));
    }
}