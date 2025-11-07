package com.upx.RD.controllers;

import com.upx.RD.model.PostSobra;
import com.upx.RD.services.PostSobraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedController {

    private final PostSobraService postSobraService;


    @GetMapping("/feed")
    public String exibirFeed(Model model) {

        List<PostSobra> posts = postSobraService.listarPostsDisponiveis();
        model.addAttribute("posts", posts);
        return "feed";
    }
}