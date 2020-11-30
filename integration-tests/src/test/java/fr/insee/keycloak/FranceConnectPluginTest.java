package fr.insee.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Container;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class FranceConnectPluginTest {

    private static final String PLUGIN_FILE = "keycloak-franceconnect.jar";

    private static final String PLUGIN_FILE_PATH_IN_HOST = "../plugin/target/" + PLUGIN_FILE;

    private static final String PLUGINS_FOLDER_IN_CONTAINER = "/opt/jboss/keycloak/standalone/deployments/";
    private static final String PLUGIN_FILE_PATH_IN_CONTAINER = PLUGINS_FOLDER_IN_CONTAINER + PLUGIN_FILE;

    private static final String PLUGIN_SUCCESS_DEPLOYMENT_FILE = PLUGIN_FILE + ".deployed";

    private static KeycloakContainer keycloakContainer;

    @BeforeAll
    static void setup() {
        keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:9.0.2")
            .withRealmImportFile("test-realm.json")
            .withCopyFileToContainer(MountableFile.forHostPath(PLUGIN_FILE_PATH_IN_HOST), PLUGIN_FILE_PATH_IN_CONTAINER);

        keycloakContainer.start();
    }

    @Test
    void check_if_france_connect_plugin_is_correctly_deployed() throws IOException, InterruptedException {
        assertThat(keycloakContainer.isRunning()).isTrue();

        Container.ExecResult commandResult = keycloakContainer.execInContainer(
            "cat", PLUGINS_FOLDER_IN_CONTAINER + PLUGIN_SUCCESS_DEPLOYMENT_FILE
        );

        assertThat(commandResult.getExitCode()).isZero();

        String keycloakLogs = keycloakContainer.getLogs();
        assertThat(keycloakLogs).contains("Deployed \"" + PLUGIN_FILE + "\"");
    }
}
