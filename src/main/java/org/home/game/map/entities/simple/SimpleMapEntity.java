package org.home.game.map.entities.simple;

import org.home.game.map.entities.MapEntity;
import org.home.game.map.entities.MapEntityType;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class SimpleMapEntity implements MapEntity {

    private final String name;

    private final MapEntityType type;

    private final int attackPower;

    private int health;

    private boolean defended;

    public SimpleMapEntity(@Nonnull String name, @Nonnull MapEntityType type, @Nonnegative int attackPower) {
        this.name = name;
        this.type = type;
        this.attackPower = attackPower;
        this.health = 100;
        this.defended = false;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getAttackPower() {
        return attackPower;
    }

    @Nonnull
    @Override
    public MapEntityType getType() {
        return type;
    }

    @Override
    public boolean isUser() {
        return false;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public boolean canContainAnotherEntity() {
        return false;
    }

    @Override
    public boolean containAnotherEntity() {
        return getInnerEntity().isPresent();
    }

    @Override
    public boolean containUserCharacter() {
        return getInnerEntity()
                .filter(entity -> entity.isUser() || entity.containUserCharacter())
                .isPresent();
    }

    @Override
    public boolean containTasks(@Nonnull Predicate<MapEntity> condition) {
        return condition.test(this) && !isUser()
                || getInnerEntity().filter(entity -> entity.containTasks(condition)).isPresent();
    }

    @Override
    public MapEntity findEntity(@Nonnull Predicate<MapEntity> condition) {
        return getInnerEntity()
                .map(entity -> condition.test(entity) ? entity : entity.findEntity(condition))
                .orElseThrow(() -> new IllegalStateException("There is no entities with such condition"));
    }

    @Nonnull
    @Override
    public Optional<MapEntity> getInnerEntity() {
        return Optional.empty();
    }

    @Override
    public void take(@Nonnull MapEntity anotherEntity) {
        throw new UnsupportedOperationException("This method is not supported.");
    }

    @Override
    public void isBeatenBy(@Nonnull MapEntity anotherEntity) {
        if (defended) {
            defended = false;
        } else {
            health -= anotherEntity.getAttackPower();
        }
    }

    @Override
    public void defense() {
        defended = true;
    }

    @Override
    public boolean isDefended() {
        return defended;
    }

    @Override
    public void relax() {
        defended = false;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("This method is not supported.");
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (this == anotherObject) {
            return true;
        }

        if (anotherObject == null || getClass() != anotherObject.getClass()) {
            return false;
        }

        SimpleMapEntity anotherEntity = (SimpleMapEntity) anotherObject;
        return Objects.equals(name, anotherEntity.name)
                && type == anotherEntity.type
                && attackPower == anotherEntity.attackPower;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, attackPower);
    }

    @Override
    public String toString() {
        return "SimpleMapEntity{"
                + "name='" + name + '\''
                + ", type=" + type
                + ", health=" + health
                + ", attackPower=" + attackPower
                + ", defended=" + defended
                + '}';
    }
}
