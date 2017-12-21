package com.faforever.client.fx;

import com.faforever.client.main.event.NavigateEvent;
import javafx.scene.Node;


public abstract class AbstractViewController<ROOT extends Node> implements Controller<ROOT> {

  private NavigateEvent event;

  public AbstractViewController() {
  }

  public void initialize() {
    ROOT root = getRoot();
    root.sceneProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null && root.isVisible()) {
        onDisplay();
      } else {
        onHide();
      }
    });
    root.visibleProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue && root.getScene() != null) {
        onDisplay();
      } else {
        onHide();
      }
    });
  }

  /**
   * Subclasses may override in order to perform actions when the view is being displayed.
   */
  protected void onDisplay() {
    if (event != null) {
      onEvent(event);
      event = null;
    }
  }

  /**
   * Subclasses may override in order to perform actions when the view is no longer being displayed.
   */
  protected void onHide() {

  }

  public void setEvent(NavigateEvent event) {
    this.event = event;
  }

  /**
   * Subclasses may override in order to be notified of Events.
   *
   * @param event
   */
  public void onEvent(NavigateEvent event) {
    //to be overwritten
  }
}
