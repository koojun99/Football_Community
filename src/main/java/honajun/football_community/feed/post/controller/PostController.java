package honajun.football_community.feed.post.controller;

import honajun.football_community.feed.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class PostController {

    private final PostService postService;
}
