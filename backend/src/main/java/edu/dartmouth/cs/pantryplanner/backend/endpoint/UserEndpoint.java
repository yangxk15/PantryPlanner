package edu.dartmouth.cs.pantryplanner.backend.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.ConflictException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import edu.dartmouth.cs.pantryplanner.backend.OfyService;
import edu.dartmouth.cs.pantryplanner.backend.entity.UserRecord;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "user",
        version = "v1",
        resource = "userRecord",
        namespace = @ApiNamespace(
                ownerDomain = "entity.backend.pantryplanner.cs.dartmouth.edu",
                ownerName = "entity.backend.pantryplanner.cs.dartmouth.edu",
                packagePath = ""
        )
)
public class UserEndpoint {

    private static final Logger logger = Logger.getLogger(UserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    /**
     * Returns the {@link UserRecord} with the corresponding ID.
     *
     * @param email the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     */
    @ApiMethod(name = "login")
    public UserRecord get(
            @Named("email") String email,
            @Named("password") String password
    ) throws NotFoundException, ConflictException {
        logger.info("Getting UserRecord with email: " + email);
        UserRecord userRecord = findRecord(email);

        if (userRecord == null) {
            String msg = "This email is not found";
            logger.info(msg);
            throw new NotFoundException(msg);
        }
        if (!userRecord.getPassword().equals(password)) {
            String msg = "This password is incorrect";
            logger.info(msg);
            throw new ConflictException(msg);
        }

        logger.info("Login success");

        return userRecord;
    }

    /**
     * Inserts a new {@code UserRecord}.
     */
    @ApiMethod(name = "register")
    public UserRecord insert(
            @Named("email") String email,
            @Named("firstName") String firstName,
            @Named("lastName") String lastName,
            @Named("password") String password
    ) throws ConflictException {
        if (findRecord(email) != null) {
            String msg = "This email has already existed";
            logger.info(msg);
            throw new ConflictException(msg);
        }

        UserRecord userRecord = new UserRecord(email, firstName, lastName, password);
        ofy().save().entity(userRecord).now();
        logger.info("Created UserRecord.");

        return ofy().load().entity(userRecord).now();
    }

    private UserRecord findRecord(String email) {
        return OfyService.ofy().load().type(UserRecord.class).filter("email", email).first().now();
    }
}