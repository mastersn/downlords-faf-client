package com.faforever.client.chat;

import java.util.Locale;

public enum ChatFormat {
  EXTENDED("settings.chat.chatFormat.extended"),
  COMPACT("settings.chat.chatFormat.compact");

  private String i18nKey;

  ChatFormat(String i18nKey) {
    this.i18nKey = i18nKey;
  }

  public String getI18nKey() {
    return i18nKey;
  }

}
