package kimosabe.api.controller;

import kimosabe.api.entity.GameSearchResult;
import kimosabe.api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("games")
public class GameController {
    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/search")
    public List<GameSearchResult> results(@RequestParam("searchTerm") String searchTerm) {
        return gameService.searchForGames(searchTerm);
    }
}
