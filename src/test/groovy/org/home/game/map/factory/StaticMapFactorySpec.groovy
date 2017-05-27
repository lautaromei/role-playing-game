package org.home.game.map.factory

import static org.home.game.map.entities.MapEntityFactory.userCharacter
import static org.home.game.map.entities.MapEntityType.BEAR
import static org.home.game.map.entities.MapEntityType.CHARACTER
import static org.home.game.map.entities.MapEntityType.ROAD
import static org.home.game.map.entities.MapEntityType.STONE
import static org.home.game.map.entities.MapEntityType.TREE
import static org.home.game.map.entities.MapEntityType.WOLF
import static org.home.game.map.entities.character.Race.HUMAN
import static org.home.game.map.entities.character.Sex.MALE

import org.home.game.map.GameMap
import org.home.game.map.entities.MapEntity
import org.home.game.map.entities.character.create.NewCharacterPresenter
import spock.lang.Specification
import spock.lang.Subject

class StaticMapFactorySpec extends Specification {

    NewCharacterPresenter newCharacterPresenter = Mock()

    @Subject
    StaticMapFactory factory = new StaticMapFactory(newCharacterPresenter)

    void 'map should be created and user should provide information about his character'() {
        given:
            MapEntity character = userCharacter('Andrii', HUMAN, MALE)
        when:
            GameMap map = factory.create()
        then:
            1 * newCharacterPresenter.show()
            1 * newCharacterPresenter.getGameCharacter() >> Optional.of(character)
        and:
            with(map) {
                containsUserCharacter()
                !containsTasks()

                entities[0]*.type == [ROAD, ROAD, WOLF, TREE, STONE]
                entities[1]*.type == [ROAD, ROAD, ROAD, TREE, TREE]
                entities[2]*.type == [ROAD, ROAD, ROAD, ROAD, BEAR]
                entities[3]*.type == [ROAD, STONE, ROAD, ROAD, ROAD]
                entities[4]*.type == [ROAD, TREE, ROAD, ROAD, ROAD]

                entities[2][2].innerEntity.isPresent()
                entities[2][2].innerEntity.get() == character

                entities[4][0].innerEntity.isPresent()
                entities[4][0].innerEntity.get().type == CHARACTER
            }
    }
}