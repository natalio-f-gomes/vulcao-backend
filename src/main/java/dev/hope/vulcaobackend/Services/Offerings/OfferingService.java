package dev.hope.vulcaobackend.Services.Offerings;

import dev.hope.vulcaobackend.Models.Offerings.OfferingModel;
import dev.hope.vulcaobackend.Models.UserModel;
import dev.hope.vulcaobackend.Repositories.OfferingRepository;
import dev.hope.vulcaobackend.Services.ExceptionHandler.ApiException;
import dev.hope.vulcaobackend.Services.GlobalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OfferingService {
    @Autowired
    GlobalServices globalServices;
    @Autowired
    OfferingRepository offeringRepository;

    public void delete(String id) {
        String sessionUser = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserModel user = globalServices.validateAuthorization(sessionUser);
        OfferingModel offering = offeringRepository.findById(id).orElseThrow(()->
                new ApiException(HttpStatus.NOT_FOUND, "A oferta solicitada não foi encontrada."));
        offeringRepository.delete(offering);
    }
}
