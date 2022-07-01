package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.ItemService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@ThreadSafe
@Controller
public class ItemController {

    private final ItemService itemService;

    private final CategoryService categoryService;

    public ItemController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping("/index")
    public String index(Model model,
                        HttpSession sessionUser,
                        @RequestParam(name = "sort", required = false) String sort) {
        User user = getUser(sessionUser);
        model.addAttribute("sort", sort);
        model.addAttribute("user", user);
        model.addAttribute("items", itemService.findAllByIdUser(user.getId()));
        model.addAttribute("time", LocalDateTime.now());
        return "index";
    }

    @GetMapping("/indexAll")
    public String indexAll() {
        return "redirect:/index?sort=all";
    }

    @GetMapping("/indexDone")
    public String indexDone() {
        return "redirect:/index?sort=done";
    }

    @GetMapping("/indexNew")
    public String indexNew() {
        return "redirect:/index?sort=new";
    }

    @GetMapping("/addItem")
    public String addItem(Model model, HttpSession sessionUser) {
        model.addAttribute("user", getUser(sessionUser));
        model.addAttribute("categories", categoryService.findAll());
        return "formAddItem";
    }

    @PostMapping("/formAddItem")
    public String formAddItem(@ModelAttribute Item item, @RequestParam("category.id") List<Integer> catIdList) {
        for (var catId : catIdList) {
            item.addCategory(categoryService.findById(catId));
        }
        itemService.add(item);
        return "redirect:/index";
    }

    @GetMapping("/getDescription/{itemId}")
    public String getDescription(
            Model model, @PathVariable("itemId") int id, HttpSession sessionUser) {
        model.addAttribute("item", itemService.findById(id));
        model.addAttribute("user", getUser(sessionUser));
        return "description";
    }

    @GetMapping("/updateItem/{itemId}")
    public String updateItem(Model model, @PathVariable("itemId") int id, HttpSession sessionUser) {
        model.addAttribute("item", itemService.findById(id));
        model.addAttribute("user", getUser(sessionUser));
        return "formUpdateItem";
    }

    @PostMapping("/formUpdateItem")
    public String formUpdateItem(@ModelAttribute Item item) {
        itemService.updateById(item.getId(), item);
        return "redirect:/index";
    }

    @GetMapping("/deleteItem/{itemId}")
    public String deleteItem(@PathVariable("itemId") int id) {
        itemService.deleteById(id);
        return "redirect:/index";
    }

    @GetMapping("/doneItem/{itemId}")
    public String doneItem(@PathVariable("itemId") int id) {
        itemService.updateByIdWhenDone(id);
        return "redirect:/index";
    }

    private User getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUserName("Гость");
        }
        return user;
    }
}
