/*
 * Faculdade de Tecnologia, UNICAMP
 * Professor responsável: Ivan L. M. Ricarte
 */

package resources;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ricarte at ft.unicamp.br
 */
public class MovieLog {
    public static final Logger LOG = Logger.getLogger("SI405-Logger");

    public static void initialize() {
        FileHandler logHandler;
        try {
            logHandler = new FileHandler("%h/SI405-Log-Atividade3-%u.log");
            LOG.addHandler(logHandler);
            LOG.setLevel(Level.FINER);
        } catch (IOException | SecurityException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    
}
