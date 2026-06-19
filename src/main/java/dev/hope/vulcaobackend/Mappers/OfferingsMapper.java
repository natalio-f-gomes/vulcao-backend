package dev.hope.vulcaobackend.Mappers;


import dev.hope.vulcaobackend.DataTransferObjects.Offerings.*;
import dev.hope.vulcaobackend.Models.Offerings.*;
import org.springframework.stereotype.Component;

@Component
public class OfferingsMapper {
    public ProductDTO transformToProductDTO(ProductModel product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setUpdatedAt(product.getUpdatedAt());
        productDTO.setCreatedAt(product.getCreatedAt());
        //
        productDTO.setExpirationDate(product.getExpirationDate());
        productDTO.setProductType(product.getProductType());
        productDTO.setName(product.getName());
        productDTO.setCompanyId(product.getCompanyId());
        productDTO.setPricePerUnit(product.getPricePerUnit());
        productDTO.setStock(product.getStock());
        productDTO.setUnitType(product.getUnitType());
        productDTO.setUpdatedBy(product.getUpdatedBy());
        productDTO.setCurrency(product.getCurrency());
        productDTO.setDescription(product.getDescription());
        productDTO.setCreatorId(product.getCreatorId());
        productDTO.setImgS3UrlKeys(product.getImgS3UrlKeys());
        return productDTO;
    }

    public BookDTO transformBookModelToDTO(BookModel bookModel) {
        if (bookModel == null) {
            return null;
        }

        BookDTO bookDTO = new BookDTO();

        // Set BaseModelDTO fields
        bookDTO.setId(bookModel.getId());
        bookDTO.setUpdatedAt(bookModel.getUpdatedAt());
        bookDTO.setCreatedAt(bookModel.getCreatedAt());

        bookDTO.setName(bookModel.getName());
        bookDTO.setCompanyId(bookModel.getCompanyId());
        bookDTO.setPricePerUnit(bookModel.getPricePerUnit());
        bookDTO.setUnitType(bookModel.getUnitType());
        bookDTO.setUpdatedBy(bookModel.getUpdatedBy());
        bookDTO.setCurrency(bookModel.getCurrency());
        bookDTO.setDescription(bookModel.getDescription());
        bookDTO.setCreatorId(bookModel.getCreatorId());
        bookDTO.setStock(bookModel.getStock());

        // Set Book-specific fields
        bookDTO.setSBN(bookModel.getSBN());
        bookDTO.setAuthors(bookModel.getAuthors());
        bookDTO.setPublisher(bookModel.getPublisher());
        bookDTO.setEdition(bookModel.getEdition());
        bookDTO.setVolume(bookModel.getVolume());
        bookDTO.setPageNumber(bookModel.getPageNumber());
        bookDTO.setWeight(bookModel.getWeight());
        bookDTO.setKeywords(bookModel.getKeywords());
        bookDTO.setGenres(bookModel.getGenres());
        bookDTO.setLanguages(bookModel.getLanguages());
        bookDTO.setPublicationDate(bookModel.getPublicationDate());
        bookDTO.setFormat(bookModel.getFormat());

        return bookDTO;
    }
    public MovieDTO transformMovieModelToDTO(MovieModel movie) {
        if (movie == null) {
            return null;
        }

        MovieDTO movieDTO = new MovieDTO();

        // BaseModelDTO fields
        movieDTO.setId(movie.getId());
        movieDTO.setCreatedAt(movie.getCreatedAt());
        movieDTO.setUpdatedAt(movie.getUpdatedAt());

        // Common fields
        movieDTO.setName(movie.getName());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setPricePerUnit(movie.getPricePerUnit());
        movieDTO.setUnitType(movie.getUnitType());
        movieDTO.setCurrency(movie.getCurrency());
        movieDTO.setStock(movie.getStock());

        movieDTO.setCreatorId(movie.getCreatorId());
        movieDTO.setUpdatedBy(movie.getUpdatedBy());
        movieDTO.setCompanyId(movie.getCompanyId());


        // Movie-specific fields
        movieDTO.setDirectors(movie.getDirectors());
        movieDTO.setLeadActors(movie.getLeadActors());
        movieDTO.setProducers(movie.getProducers());
        movieDTO.setGenres(movie.getGenres());
        movieDTO.setLanguages(movie.getLanguages());
        movieDTO.setReleaseDate(movie.getReleaseDate());
        movieDTO.setCountry(movie.getCountry());
        movieDTO.setAgeRating(movie.getAgeRating());

        return movieDTO;
    }

    public TVShowDTO transformTVShowModelToDTO(TVShowModel tvShow) {
        if (tvShow == null) {return null;}

        TVShowDTO dto = new TVShowDTO();

        // BaseModelDTO fields
        dto.setId(tvShow.getId());
        dto.setCreatedAt(tvShow.getCreatedAt());
        dto.setUpdatedAt(tvShow.getUpdatedAt());

        // Common fields
        dto.setName(tvShow.getName());
        dto.setDescription(tvShow.getDescription());
        dto.setPricePerUnit(tvShow.getPricePerUnit());
        dto.setUnitType(tvShow.getUnitType());
        dto.setCurrency(tvShow.getCurrency());
        dto.setStock(tvShow.getStock());

        dto.setCreatorId(tvShow.getCreatorId());
        dto.setUpdatedBy(tvShow.getUpdatedBy());
        dto.setCompanyId(tvShow.getCompanyId());


        // Media / shared cinematic fields
        dto.setDirectors(tvShow.getDirectors());
        dto.setLeadActors(tvShow.getLeadActors());
        dto.setProducers(tvShow.getProducers());
        dto.setTitle(tvShow.getTitle());
        dto.setGenres(tvShow.getGenres());
        dto.setLanguages(tvShow.getLanguages());
        dto.setReleaseDate(tvShow.getReleaseDate());
        dto.setCountry(tvShow.getCountry());
        dto.setAgeRating(tvShow.getAgeRating());

        // TV-specific fields
        dto.setTrailerUrl(tvShow.getTrailerUrl());
        dto.setRuntime(tvShow.getRuntime());
        dto.setSeasons(tvShow.getSeasons());
        dto.setEpisodes(tvShow.getEpisodes());
        dto.setWriters(tvShow.getWriters());
        dto.setStatus(tvShow.getStatus());
        dto.setMediaType(tvShow.getMediaType());
        dto.setEpisodeRuntime(tvShow.getEpisodeRuntime());

        return dto;
    }

    public MusicDTO transformMusicModelToDTO(MusicModel music) {
        if (music == null) {return null;}

        MusicDTO dto = new MusicDTO();

        // BaseModelDTO fields
        dto.setId(music.getId());
        dto.setCreatedAt(music.getCreatedAt());
        dto.setUpdatedAt(music.getUpdatedAt());

        // Common fields
        dto.setName(music.getName());
        dto.setDescription(music.getDescription());
        dto.setPricePerUnit(music.getPricePerUnit());
        dto.setUnitType(music.getUnitType());
        dto.setCurrency(music.getCurrency());
        dto.setStock(music.getStock());
        dto.setCreatorId(music.getCreatorId());
        dto.setUpdatedBy(music.getUpdatedBy());
        dto.setCompanyId(music.getCompanyId());

        // Media / shared cinematic fields
        dto.setAlbumName(music.getAlbumName());
        dto.setArtistName(music.getArtistName());
        dto.setFeaturedArtists(music.getFeaturedArtists());
        dto.setRecordLabel(music.getRecordLabel());
        dto.setWriters(music.getWriters());
        return dto;
    }
}
