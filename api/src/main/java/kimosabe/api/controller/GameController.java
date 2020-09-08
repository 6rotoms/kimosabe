package kimosabe.api.controller;

import kimosabe.api.entity.GameSearchResult;
import kimosabe.api.entity.SearchSummary;
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
    public List<GameSearchResult> results(
            @RequestParam(value="searchTerm") String searchTerm,
            @RequestParam(value="pageNum", defaultValue = "0") Integer pageNum) {
        return gameService.searchForGames(searchTerm, pageNum);
    }

    @GetMapping("/searchInfo")
    public SearchSummary results(
            @RequestParam(value="searchTerm") String searchTerm) {
        return gameService.getSearchSummary(searchTerm);
    }
}
