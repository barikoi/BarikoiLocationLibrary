package barikoi.barikoilocation.BarikoiException;

public class BarikoiAPIConfigurationException extends RuntimeException {
    /**
     * Creates a BarikoiAPI configuration exception thrown by BarikoiAPI when the API hasn't been properly initialised.
     */
    public BarikoiAPIConfigurationException() {
        super("\nUsing BarikoiAPI requires calling BarikoiAPI.getInstance(Context context, String accessToken) before "
                + "inflating or creating the view. The access token parameter is required when using a BarikoiAPI service."
                + "\nPlease see https://barikoi.com/#/business/dash to learn how to create one.");
    }
}
