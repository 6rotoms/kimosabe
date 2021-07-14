package kimosabe.api.controller;

import kimosabe.api.entity.GameSearchResponse;
import kimosabe.api.entity.SearchSummary;
import kimosabe.api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("games")
@Validated
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GameSearchResponse> search(
            @RequestParam(value="searchTerm") String searchTerm,
            @RequestParam(value="pageNum", defaultValue = "0") Integer pageNum,
            @Min(value=1, message="pageSize must be greater than 0")
            @Max(value=100, message="pageSize must be less than 101")
            @RequestParam(value="pageSize", defaultValue = "25") Integer pageSize
    ) {
            return gameService.searchForGames(searchTerm, pageNum, pageSize);
    }

    @GetMapping("/searchInfo")
    @ResponseStatus(HttpStatus.OK)
    public SearchSummary searchSummary(
            @RequestParam(value="searchTerm") String searchTerm,
            @Min(value=1, message="pageSize must be greater than 0")
            @Max(value=100, message="pageSize must be less than 101")
            @RequestParam(value="pageSize", defaultValue = "25") Integer pageSize
    ) {
        return gameService.getSearchSummary(searchTerm, pageSize);
    }
}
