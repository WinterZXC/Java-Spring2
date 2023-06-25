package com.example.demo.controllers;

import com.example.demo.models.Travel;
import com.example.demo.repo.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class TravelController {

    @Autowired
    private TravelRepository travelRepository;

    @GetMapping("/travel")
    public String travelMain(Model model){
        Iterable<Travel> travels = travelRepository.findAll();
        model.addAttribute("travels", travels);
        return "travel-main";
    }
    @GetMapping("/travel/add")
    public String travelAdd(Model model){
        return "travel-add";
    }

    @PostMapping("/travel/add")
    public String travelDataAdd(@RequestParam String path,
                                @RequestParam String owner_name,
                                @RequestParam String vehicle,
                                @RequestParam Integer distance,
                                Model model){
        Travel travel = new Travel(path, owner_name, vehicle, distance);
        travelRepository.save(travel);
        return "redirect:/travel";
    }

    @GetMapping("/travel/{id}/edit")
    public String travelEdit(@PathVariable(value = "id") long id, Model model){
        if(!travelRepository.existsById(id)){
            return "redirect:/travel";
        }
        Optional<Travel> travel = travelRepository.findById(id);
        ArrayList<Travel> res = new ArrayList<>();
        travel.ifPresent(res::add);
        model.addAttribute("travel", res);
        return "travel-edit";
    }

    @PostMapping("/travel/{id}/edit")
    public String travelDataEdit(@PathVariable(value = "id") long id,@RequestParam String path,
                                @RequestParam String owner_name,
                                @RequestParam String vehicle,
                                @RequestParam Integer distance,
                                Model model){
        Travel travel = travelRepository.findById(id).orElseThrow();
        travel.setPath(path);
        travel.setOwner(owner_name);
        travel.setVehicle(vehicle);
        travel.setDistance(distance);
        travelRepository.save(travel);
        return "redirect:/travel";
    }

    @PostMapping("/travel/{id}/delete")
    public String travelDelete(@PathVariable(value = "id") long id, Model model){
        Travel travel = travelRepository.findById(id).orElseThrow();
        travelRepository.delete(travel);
        return "redirect:/travel";
    }
}
