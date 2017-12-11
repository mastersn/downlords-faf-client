package com.faforever.client.vault;

import com.faforever.client.fx.AbstractViewController;
import com.faforever.client.main.NavigateEvent.Parameter;
import com.faforever.client.map.MapVaultController;
import com.faforever.client.mod.ModVaultController;
import com.faforever.client.replay.OnlineReplayVaultController;
import com.faforever.client.vault.replay.ReplayVaultController;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VaultController extends AbstractViewController<Node> {
  public TabPane vaultRoot;
  public MapVaultController mapVaultController;
  public ModVaultController modVaultController;
  public ReplayVaultController localReplayVaultController;
  public OnlineReplayVaultController onlineReplayVaultController;
  public Tab mapVaultTab;

  @Override
  public Node getRoot() {
    return vaultRoot;
  }

  @Override
  public void notifyOfParameter(Parameter parameter) {
    super.notifyOfParameter(parameter);
    if (parameter.equals(Parameter.SHOW_LADDER_MAPS)) {
      mapVaultController.notifyOfParameter(Parameter.SHOW_LADDER_MAPS);
      vaultRoot.getSelectionModel().select(mapVaultTab);
    }
  }
}
