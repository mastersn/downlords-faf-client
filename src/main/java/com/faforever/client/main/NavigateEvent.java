package com.faforever.client.main;

import lombok.Getter;

public class NavigateEvent {
  @Getter
  private final NavigationItem item;
  @Getter
  private final Parameter parameter;

  public NavigateEvent(NavigationItem item) {
    this(item, null);
  }

  public NavigateEvent(NavigationItem item, Parameter parameter) {
    this.item = item;
    this.parameter = parameter;
  }

  public enum Parameter {
    SHOW_LADDER_MAPS

  }
}
