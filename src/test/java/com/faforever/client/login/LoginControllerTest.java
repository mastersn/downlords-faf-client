package com.faforever.client.login;

import com.faforever.client.config.ClientProperties;
import com.faforever.client.config.ClientProperties.Website;
import com.faforever.client.fx.PlatformService;
import com.faforever.client.preferences.Preferences;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.user.UserService;
import javafx.event.ActionEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest extends AbstractPlainJavaFxTest {
  private LoginController instance;
  @Mock
  private PreferencesService preferencesService;
  @Mock
  private UserService userService;
  @Mock
  private PlatformService platformService;
  @Mock
  private ClientProperties clientProperties;

  @Before
  public void setUp() throws Exception {
    instance = new LoginController(userService, preferencesService, platformService, clientProperties);
    loadFxml("theme/login.fxml", param -> instance);

    Website website = new Website();
    website.setCreateAccountUrl("create");
    website.setForgotPasswordUrl("forgot");

    when(clientProperties.getWebsite()).thenReturn(website);

    when(preferencesService.getPreferences()).thenReturn(new Preferences());
  }

  @Test
  public void testLoginNotCalledWhenNoUsernameAndPasswordSet() throws Exception {
    instance.display();

    verify(userService, never()).login(anyString(), anyString(), anyBoolean());
  }

  @Test
  public void testLoginButtonClicked() throws Exception {
    instance.usernameInput.setText("JUnit");
    instance.passwordInput.setText("password");
    instance.autoLoginCheckBox.setSelected(true);

    when(userService.login(anyString(), anyString(), anyBoolean())).thenReturn(CompletableFuture.completedFuture(null));

    instance.loginButtonClicked();

    verify(userService).login("JUnit", "password", true);
  }

  @Test
  public void testCreateAccountButtton() throws Exception {
    instance.createNewAccountClicked(new ActionEvent());

    verify(platformService).showDocument("create");
  }

  @Test
  public void testForgotPasswordButtton() throws Exception {
    instance.forgotLoginClicked(new ActionEvent());

    verify(platformService).showDocument("forgot");
  }
}
