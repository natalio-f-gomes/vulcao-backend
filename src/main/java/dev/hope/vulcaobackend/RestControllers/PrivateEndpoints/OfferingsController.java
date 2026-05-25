package dev.hope.vulcaobackend.RestControllers.PrivateEndpoints;

import dev.hope.vulcaobackend.Models.Offerings.BookModel;
import dev.hope.vulcaobackend.Models.Offerings.MovieModel;
import dev.hope.vulcaobackend.Models.Offerings.MusicModel;
import dev.hope.vulcaobackend.Models.Offerings.ProductModel;
import dev.hope.vulcaobackend.Services.Offerings.BookService;
import dev.hope.vulcaobackend.Services.Offerings.MovieService;
import dev.hope.vulcaobackend.Services.Offerings.MusicService;
import dev.hope.vulcaobackend.Services.Offerings.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/api/v1/offering")
@RestController
public class OfferingsController {

    @Autowired
    private BookService bookService;
    @Autowired private MovieService movieService;
    @Autowired private MusicService musicService;
    @Autowired private ProductService productService;

    // REGISTER PRODUCT
    @PostMapping("/product/employer/add")
    public ResponseEntity<?> employerAddProduct(@RequestBody ProductModel newProduct, HttpServletRequest request){

        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.employerSaveProduct(newProduct,userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/product/staff/add")
    public ResponseEntity<?> staffAddProduct(@RequestBody ProductModel newProduct, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.staffSaveProduct(newProduct,userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // UPDATE PRODUCT
    @PutMapping("/product/employer/update/{id}")
    public ResponseEntity<?> employerUpdateProduct(@PathVariable String id, @RequestBody ProductModel newProduct, HttpServletRequest request){
        System.out.println("EMPLOYER UPDATE PRODUCT: "+ newProduct.getName());
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.employerUpdateProduct(id, newProduct,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/product/staff/update/{id}")
    public ResponseEntity<?> staffUpdateProduct(@PathVariable String id, @RequestBody ProductModel newProduct, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.staffUpdateProduct(id, newProduct,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // DELETE PRODUCT
    @DeleteMapping("/product/employer/delete/{id}")
    public ResponseEntity<?> employerDeleteProduct(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.employerDeleteProduct(id,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/product/staff/delete/{id}")
    public ResponseEntity<?> staffDeleteProduct(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        productService.staffDeleteProduct(id,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // ADD MOVIE
    @PostMapping("/movie/employer/add")
    public ResponseEntity<?> employerAddMovie(@RequestBody MovieModel newMovie, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        movieService.employerSaveMovie(newMovie,userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/movie/staff/add")
    public ResponseEntity<?> staffAddMovie(@RequestBody MovieModel newMovie, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        movieService.staffSaveMovie(newMovie,userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // UPDATE MOVIE
    @PutMapping("/movie/employer/update/{id}")
    public ResponseEntity<?> employerUpdateMovie(@PathVariable String id, @RequestBody MovieModel movie, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        movieService.employerUpdateMovie(movie,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/movie/staff/update/{id}")
    public ResponseEntity<?> staffUpdateMovie(@PathVariable String id, @RequestBody MovieModel movie, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        movieService.staffUpdateMovie(movie,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // DELETE PRODUCT
    @DeleteMapping("/movie/employer/delete/{id}")
    public ResponseEntity<?> employerDeleteMovie(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        movieService.employerDeleteMovie(id,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/movie/staff/delete/{id}")
    public ResponseEntity<?> staffDeleteMovie(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        movieService.staffDeleteMovie(id,userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // ========== MUSIC ENDPOINTS ==========

    // ADD MUSIC
    @PostMapping("/music/employer/add")
    public ResponseEntity<?> employerAddMusic(@RequestBody MusicModel newMusic, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        musicService.employerSaveMusic(newMusic, userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/music/staff/add")
    public ResponseEntity<?> staffAddMusic(@RequestBody MusicModel newMusic, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        musicService.staffSaveMusic(newMusic, userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // UPDATE MUSIC
    @PutMapping("/music/employer/update/{id}")
    public ResponseEntity<?> employerUpdateMusic(@PathVariable String id, @RequestBody MusicModel music, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        musicService.employerUpdateMusic(music, userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/music/staff/update/{id}")
    public ResponseEntity<?> staffUpdateMusic(@PathVariable String id, @RequestBody MusicModel music, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        musicService.staffUpdateMusic(music, userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // DELETE MUSIC
    @DeleteMapping("/music/employer/delete/{id}")
    public ResponseEntity<?> employerDeleteMusic(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        musicService.employerDeleteMusic(id, userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/music/staff/delete/{id}")
    public ResponseEntity<?> staffDeleteMusic(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        musicService.staffDeleteMusic(id, userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

// ========== BOOK ENDPOINTS ==========

    // ADD BOOK
    @PostMapping("/book/employer/add")
    public ResponseEntity<?> employerAddBook(@RequestBody BookModel newBook, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        bookService.employerSaveBook(newBook, userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/book/staff/add")
    public ResponseEntity<?> staffAddBook(@RequestBody BookModel newBook, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        bookService.staffSaveBook(newBook, userSession, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // UPDATE BOOK
    @PutMapping("/book/employer/update/{id}")
    public ResponseEntity<?> employerUpdateBook(@PathVariable String id, @RequestBody BookModel book, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        bookService.employerUpdateBook(book, userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/book/staff/update/{id}")
    public ResponseEntity<?> staffUpdateBook(@PathVariable String id, @RequestBody BookModel book, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        bookService.staffUpdateBook(book, userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // DELETE BOOK
    @DeleteMapping("/book/employer/delete/{id}")
    public ResponseEntity<?> employerDeleteBook(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        bookService.employerDeleteBook(id, userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/book/staff/delete/{id}")
    public ResponseEntity<?> staffDeleteBook(@PathVariable String id, HttpServletRequest request){
        String userSession = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        bookService.staffDeleteBook(id, userSession, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
