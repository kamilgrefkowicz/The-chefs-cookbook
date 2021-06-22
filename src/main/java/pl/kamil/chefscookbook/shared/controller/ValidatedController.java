package pl.kamil.chefscookbook.shared.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pl.kamil.chefscookbook.shared.response.Response;

import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.ERROR;

public abstract class ValidatedController<T> {

     protected boolean querySuccessful(Response<T> response, Model model) {
        if (!response.isSuccess()) {
            model.addAttribute(ERROR, response.getError());
            return false;
        }
        return true;
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
