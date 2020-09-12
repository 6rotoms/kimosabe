package kimosabe.api.service;

import kimosabe.api.entity.GameSearchResponse;
import kimosabe.api.entity.GroupInfo;
import kimosabe.api.exception.MissingDatabaseEntryException;
import kimosabe.api.model.Group;
import kimosabe.api.model.User;
import kimosabe.api.repository.GameSearchRepository;
import kimosabe.api.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
public class GroupService {
    private GroupRepository groupRepository;
    private GameSearchRepository gameSearchRepository;

    @Autowired
    public GroupService(
            GroupRepository groupRepository,
            GameSearchRepository gameSearchRepository
    ) {
        this.groupRepository = groupRepository;
        this.gameSearchRepository = gameSearchRepository;
    }

    public void addUserToGroup(String groupName, User userToAdd) throws MissingDatabaseEntryException {
        Optional<Group> response = groupRepository.findById(groupName);
        if (response.isEmpty()) {
            throw new MissingDatabaseEntryException("Group");
        }
        Group group = response.get();
        group.addUser(userToAdd);
        groupRepository.save(group);
    }

    public void createGroup(String gameId) {
        Map<String, String> game = gameSearchRepository.getGameById(gameId);
        boolean gameExists = game.containsKey("name");
        if (!gameExists) {
            throw new MissingDatabaseEntryException("Game");
        }
        Group group = new Group(new GameSearchResponse(gameId, game));
        group.setGroupId(gameId);
        groupRepository.save(group);
    }

    public GroupInfo getGroupInfo(String gameId) {
        Optional<Group> entity = groupRepository.findById(gameId);
        if (entity.isEmpty()) {
            throw new MissingDatabaseEntryException("Group");
        }
        Map<String, String> game = gameSearchRepository.getGameById(gameId);
        boolean gameExists = game.containsKey("name");
        if (!gameExists) {
            throw new MissingDatabaseEntryException("Game");
        }
        GameSearchResponse response = new GameSearchResponse(gameId, game);
        return new GroupInfo(response, entity.get());
    }
}
