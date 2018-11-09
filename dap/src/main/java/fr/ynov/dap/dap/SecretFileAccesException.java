package fr.ynov.dap.dap;

/**
 * @author David_tepoche
 *
 */
public class SecretFileAccesException extends Exception {

    /**
     * custom exception throws when you try to get the info from the microsoft
     * client'secret file.
     *
     * @param message error's message
     * @param e       exception throws
     */
    public SecretFileAccesException(final String message, final Exception e) {
        super(message, e);
    }

    /**
     * custom exception throws when you try to get the info from the microsoft
     * client'secret file. with a default message.
     *
     * @param e exception throws
     */
    public SecretFileAccesException(final Exception e) {
        super("Une erreur c'est produit lors de la recuperation des informations dans le fichier de proprietés", e);

    }
}