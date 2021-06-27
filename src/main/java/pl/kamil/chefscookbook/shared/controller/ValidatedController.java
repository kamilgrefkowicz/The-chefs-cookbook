package pl.kamil.chefscookbook.shared.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kamil.chefscookbook.shared.exceptions.NotAuthorizedException;
import pl.kamil.chefscookbook.shared.exceptions.NotFoundException;
import pl.kamil.chefscookbook.shared.response.Response;

import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.ERROR;

public abstract class ValidatedController<T> {

    @ExceptionHandler({NotFoundException.class, NotAuthorizedException.class})
    public String handleException(Model model, Exception exception) {

        String cause = exception.getCause().getMessage();
        model.addAttribute("error", cause);
        return ERROR;
    }


     protected boolean validationSuccessful(BindingResult bindingResult, Model model, T object) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("object", object);
            return false;
        }
        return true;
    }

     protected void resolveModification(Response<T> modification, Model model, T object) {
        if (!modification.isSuccess()) {
            model.addAttribute("object", object);
            model.addAttribute(ERROR, modification.getError());
        } else {
            model.addAttribute("object", modification.getData());
            model.addAttribute("message", modification.getMessage());
        }
    }

}
