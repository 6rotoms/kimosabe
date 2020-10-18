package kimosabe.api.controller;

import kimosabe.api.entity.SearchSummary;
import kimosabe.api.entity.GameSearchResponse;
import kimosabe.api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GameSearchResponse> results(
            @RequestParam(value="searchTerm") String searchTerm,
            @RequestParam(value="pageNum", defaultValue = "0") Integer pageNum) {
        return gameService.searchForGames(searchTerm, pageNum);
    }

    @GetMapping("/searchInfo")
    @ResponseStatus(HttpStatus.OK)
    public SearchSummary results(
            @RequestParam(value="searchTerm") String searchTerm) {
        return gameService.getSearchSummary(searchTerm);
    }
}
