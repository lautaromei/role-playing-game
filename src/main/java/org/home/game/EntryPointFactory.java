package org.home.game;

import org.home.game.character.create.NewCharacterConsoleConsoleView;
import org.home.game.character.create.NewCharacterPresenter;
import org.home.game.common.mvp.Presenter;
import org.home.game.map.MapFactory;
import org.home.game.menu.MenuConsoleConsoleView;
import org.home.game.menu.MenuPresenter;

import javax.annotation.Nonnull;

class EntryPointFactory {
    @Nonnull
    static Presenter newEntryPoint() {
        return new MenuPresenter(new MenuConsoleConsoleView(), mapFactory());
    }

    @Nonnull
    private static MapFactory mapFactory() {
        return new MapFactory(newCharacterPresenter());
    }

    @Nonnull
    private static NewCharacterPresenter newCharacterPresenter() {
        return new NewCharacterPresenter(new NewCharacterConsoleConsoleView());
    }
}