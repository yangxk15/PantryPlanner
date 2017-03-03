package edu.dartmouth.cs.pantryplanner.app.util;

import android.content.Context;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder;

import java.io.IOException;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.backend.entity.user.User;
import lombok.AllArgsConstructor;

/**
 * Created by yangxk15 on 3/1/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
public class ServiceBuilderHelper {
    public static <T extends Builder> T setup(Context context, T builder) {
        if (Constants.localMode) {
            builder.setRootUrl(
                    "http://" + Constants.LOCAL_SERVER_IP + ":8080/_ah/api/"
            );
            builder.setGoogleClientRequestInitializer(
                    new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(
                                AbstractGoogleClientRequest<?> abstractGoogleClientRequest
                        ) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    }
            );
        } else {
            builder.setRootUrl(
                    "https://" + context.getString(R.string.project_id) + ".appspot.com/_ah/api/"
            );
        }
        return builder;
    }
}
