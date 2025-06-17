package com.wwe.superstars.controller;

import com.wwe.superstars.model.Superstar;
import com.wwe.superstars.repository.SuperstarRepository;

import java.nio.file.Files;
import java.io.File;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller // inyeccion de dependencias
public class SuperstarController {

    @Autowired
    private SuperstarRepository repository;

    // ruta de las imagenes
    @Value("${upload.path}")
    private String uploadPath;

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
    public String saveSuperstar(@ModelAttribute Superstar superstar,
            @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                String originalFileName = imageFile.getOriginalFilename();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

                // Construimos ruta absoluta usando la variable inyecta
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // se crea un carpeta si no hay

                }

                Path filePath = Paths.get(uploadPath + uniqueFileName);
                Files.copy(imageFile.getInputStream(), filePath);

                superstar.setImage(uniqueFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (superstar.getId() != null) {
                repository.findById(superstar.getId()).ifPresent(s -> superstar.setImage(s.getImage()));

            }
        }

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
