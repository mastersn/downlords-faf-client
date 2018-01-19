package com.faforever.client.game;

import com.faforever.client.i18n.I18n;
import com.faforever.client.preferences.Preferences;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.theme.UiService;
import com.faforever.client.vault.replay.WatchButtonController;
import com.google.common.eventbus.EventBus;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomGamesControllerTest extends AbstractPlainJavaFxTest {

  private CustomGamesController instance;
  @Mock
  private GameService gameService;
  @Mock
  private PreferencesService preferencesService;
  @Mock
  private Preferences preferences;
  @Mock
  private UiService uiService;
  @Mock
  private GamesTableController gamesTableController;
  @Mock
  private EventBus eventBus;
  @Mock
  private GameDetailController gameDetailController;
  @Mock
  private WatchButtonController watchButtonController;
  @Mock
  private I18n i18n;
  @Mock
  private GamesTilesContainerController gamesTilesContainerController;

  private ObservableList games;

  @Before
  public void setUp() throws Exception {
    instance = new CustomGamesController(uiService, gameService, preferencesService, eventBus, i18n);

    games = FXCollections.observableArrayList();

    when(gameService.getGames()).thenReturn(games);
    when(preferencesService.getPreferences()).thenReturn(preferences);
    when(preferences.showModdedGamesProperty()).thenReturn(new SimpleBooleanProperty(true));
    when(preferences.showPasswordProtectedGamesProperty()).thenReturn(new SimpleBooleanProperty(true));
    when(preferences.getGamesViewMode()).thenReturn("tableButton");
    when(uiService.loadFxml("theme/play/games_table.fxml")).thenReturn(gamesTableController);
    when(uiService.loadFxml("theme/play/games_tiles_container.fxml")).thenReturn(gamesTilesContainerController);
    when(gamesTilesContainerController.getRoot()).thenReturn(new Pane());
    when(gamesTableController.getRoot()).thenReturn(new Pane());
    when(gamesTableController.selectedGameProperty()).thenReturn(new SimpleObjectProperty<>());
    when(gamesTilesContainerController.selectedGameProperty()).thenReturn(new SimpleObjectProperty<>());

    loadFxml("theme/play/custom_games.fxml", clazz -> {
      if (clazz == GameDetailController.class) {
        return gameDetailController;
      }
      if (clazz == WatchButtonController.class) {
        return watchButtonController;
      }
      return instance;
    });
  }

  @Test
  public void testSetSelectedGameShowsDetailPane() throws Exception {
    assertFalse(instance.gameDetailPane.isVisible());
    instance.setSelectedGame(GameBuilder.create().defaultValues().get());
    assertTrue(instance.gameDetailPane.isVisible());
  }

  @Test
  public void testSetSelectedGameNullHidesDetailPane() throws Exception {
    instance.setSelectedGame(GameBuilder.create().defaultValues().get());
    assertTrue(instance.gameDetailPane.isVisible());
    instance.setSelectedGame(null);
    assertFalse(instance.gameDetailPane.isVisible());
  }


  @Test
  public void testUpdateFilters() throws Exception {
    Game game = GameBuilder.create().defaultValues().get();
    Game gameWithMod = GameBuilder.create().defaultValues().get();
    Game gameWithPW = GameBuilder.create().defaultValues().get();
    Game gameWithModAndPW = GameBuilder.create().defaultValues().get();

    ObservableMap<String, String> simMods = FXCollections.observableHashMap();
    simMods.put("123-456-789", "Fake mod name");
    gameWithMod.setSimMods(simMods);
    gameWithModAndPW.setSimMods(simMods);

    gameWithPW.setPassword("password");
    gameWithPW.passwordProtectedProperty().set(true);
    gameWithModAndPW.setPassword("password");
    gameWithModAndPW.passwordProtectedProperty().set(true);

    ObservableList<Game> games = FXCollections.observableArrayList();
    games.addAll(game, gameWithMod, gameWithPW, gameWithModAndPW);
    instance.setFilteredList(games);

    instance.showModdedGamesCheckBox.setSelected(true);
    instance.showPasswordProtectedGamesCheckBox.setSelected(true);
    assertTrue(instance.filteredItems.size() == 4);

    instance.showModdedGamesCheckBox.setSelected(false);
    assertTrue(instance.filteredItems.size() == 2);

    instance.showPasswordProtectedGamesCheckBox.setSelected(false);
    assertTrue(instance.filteredItems.size() == 1);

    instance.showModdedGamesCheckBox.setSelected(true);
    assertTrue(instance.filteredItems.size() == 2);
  }

  @Test
  public void testTiles() throws Exception {
    instance.tilesButton.getOnAction().handle(new ActionEvent());
    WaitForAsyncUtils.waitForFxEvents();
    verify(gamesTilesContainerController).createTiledFlowPane(games, instance.chooseSortingTypeChoiceBox);
  }
}
