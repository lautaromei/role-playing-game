package org.home.game.map.painter

import static org.home.game.map.GameMapBuilder.map
import static org.home.game.map.entities.MapEntityFactory.character
import static org.home.game.map.entities.MapEntityFactory.road
import static org.home.game.map.entities.MapEntityFactory.stone
import static org.home.game.map.entities.MapEntityFactory.tree
import static org.home.game.map.entities.MapEntityFactory.userCharacter
import static org.home.game.map.entities.MapEntityFactory.wolf
import static org.home.game.map.entities.character.Race.HUMAN
import static org.home.game.map.entities.character.Race.ORC
import static org.home.game.map.entities.character.Sex.FEMALE
import static org.home.game.map.entities.character.Sex.MALE

import org.home.game.map.GameMap
import org.junit.Rule
import org.junit.contrib.java.lang.system.SystemOutRule
import spock.lang.Specification
import spock.lang.Subject

class ConsoleMapPainterSpec extends Specification {

    @Rule
    SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests()

    @Subject
    ConsoleMapPainter painter = new ConsoleMapPainter()

    GameMap map = map()
            .line(road(), wolf(), tree())
            .line(road(), road(userCharacter('Andrii', HUMAN, MALE)), tree())
            .line(stone(), road(character('ORC', ORC, FEMALE)), tree())
            .create()

    void 'map should be drawn'() {
        given:
            String expectedOutput = """\
MAP
-----
| WT|
| UT|
|SOT|
-----
"""
        when:
            painter.draw(map)
        then:
            systemOutRule.getLog() == expectedOutput
    }

    void 'map should be re-drawn'() {
        given:
            painter.draw(map)
        and:
            map.entities[2][1].clear()
        and:
            systemOutRule.clearLog()
        when:
            painter.refresh()
        then:
            systemOutRule.getLog() == """\
MAP
-----
| WT|
| UT|
|S T|
-----
"""
    }

    void 'NullPointerException should be thrown when draw method was not called before refresh method'() {
        when:
            painter.refresh()
        then:
            NullPointerException exception = thrown(NullPointerException)
            exception.message == 'draw method with map has to be called before refresh method'
        and:
            systemOutRule.getLog().isEmpty()
    }
}