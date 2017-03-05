package edu.dartmouth.cs.pantryplanner.app.util;

import android.content.Context;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.backend.entity.recipeRecordApi.RecipeRecordApi;
import edu.dartmouth.cs.pantryplanner.backend.entity.user.User;
import lombok.AllArgsConstructor;

/**
 * Created by yangxk15 on 3/1/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
public class ServiceBuilderHelper {
    public static <T extends Builder> T getBuilder(Context context, Class<T> cls) {
        T builder;

        try {
            builder = setup(
                    context,
                    cls.getConstructor(
                            HttpTransport.class,
                            JsonFactory.class,
                            HttpRequestInitializer.class
                    ).newInstance(
                            AndroidHttp.newCompatibleTransport(),
                            new AndroidJsonFactory(),
                            null
                    )
            );
        } catch (Exception e) {
            builder = null;
            Log.d("ServiceBuilderHelper", e.toString());
        }

        return builder;
    }

    private static <T extends Builder> T setup(Context context, T builder) {
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
