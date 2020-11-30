package fr.insee.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Test;

class FranceConnectPluginTest {

    @Test
    void should_start_keycloak_container_with_installed_france_connect_plugin() {

        KeycloakContainer keycloakContainer = new KeycloakContainer();
        keycloakContainer.start();
    }
}
