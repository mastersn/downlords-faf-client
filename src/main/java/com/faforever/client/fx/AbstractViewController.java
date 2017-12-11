package com.faforever.client.fx;

import com.faforever.client.main.NavigateEvent.Parameter;
import javafx.scene.Node;


public abstract class AbstractViewController<ROOT extends Node> implements Controller<ROOT> {

  private Parameter parameter;

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
    if (parameter != null) {
      notifyOfParameter(parameter);
    }
  }

  /**
   * Subclasses may override in order to perform actions when the view is no longer being displayed.
   */
  protected void onHide() {

  }

  /**
   * Subclasses may override in order to be notified of wanted actions.
   *
   * @param parameter
   */
  public void notifyOfParameter(Parameter parameter) {
    //Override to be notified
  }

  public void setParamenterToBeNotified(Parameter parameter) {
    this.parameter = parameter;
  }
}
