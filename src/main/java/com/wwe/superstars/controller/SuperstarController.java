package com.wwe.superstars.controller;

import com.wwe.superstars.model.Superstar;
import com.wwe.superstars.repository.SuperstarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SuperstarController {

    @Autowired
    private SuperstarRepository repository;

    // mostrar la lista de superstrellas
    @GetMapping("/")
    public String listarSuperstars(Model model) {
        model.addAttribute("superstars", repository.findAll());
        return "index";
    }

    // mostrar formulario para agregar
    @GetMapping("/nuevo")
    public String mostFormRegis(Model model) {
        model.addAttribute("superstar", new Superstar());
        return "formulario";
    }

    // guardar nuevo o edit
    @PostMapping("/guardar")
    public String saveSuperstar(@ModelAttribute Superstar superstar) {
        repository.save(superstar);
        return "redirect:/";
    }

    // mostrar formulario de edicion
    @GetMapping("/editar/{id}")
    public String mostFormEdit(@PathVariable("id") Long id, Model model) {
        Superstar superstar = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("superstar", superstar);
        return "formulario";
    }

    // para eliminar
    @GetMapping("/eliminar/{id}")
    public String elimSupers(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "redirect:/";
    }
}
