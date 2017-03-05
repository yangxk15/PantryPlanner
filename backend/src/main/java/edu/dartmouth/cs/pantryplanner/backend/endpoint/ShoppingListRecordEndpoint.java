package edu.dartmouth.cs.pantryplanner.backend.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import edu.dartmouth.cs.pantryplanner.backend.entity.ShoppingListRecord;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "shoppingListRecordApi",
        version = "v1",
        resource = "shoppingListRecord",
        namespace = @ApiNamespace(
                ownerDomain = "entity.backend.pantryplanner.cs.dartmouth.edu",
                ownerName = "entity.backend.pantryplanner.cs.dartmouth.edu",
                packagePath = ""
        )
)
public class ShoppingListRecordEndpoint {

    private static final Logger logger = Logger.getLogger(ShoppingListRecordEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    /**
     * Returns the {@link ShoppingListRecord} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code ShoppingListRecord} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "shoppingListRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ShoppingListRecord get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting ShoppingListRecord with ID: " + id);
        ShoppingListRecord shoppingListRecord = ofy().load().type(ShoppingListRecord.class).id(id).now();
        if (shoppingListRecord == null) {
            throw new NotFoundException("Could not find ShoppingListRecord with ID: " + id);
        }
        return shoppingListRecord;
    }

    /**
     * Inserts a new {@code ShoppingListRecord}.
     */
    @ApiMethod(
            name = "insert",
            path = "shoppingListRecord",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ShoppingListRecord insert(ShoppingListRecord shoppingListRecord) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that shoppingListRecord.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(shoppingListRecord).now();
        logger.info("Created ShoppingListRecord with ID: " + shoppingListRecord.getId());

        return ofy().load().entity(shoppingListRecord).now();
    }

    /**
     * Updates an existing {@code ShoppingListRecord}.
     *
     * @param id                 the ID of the entity to be updated
     * @param shoppingListRecord the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ShoppingListRecord}
     */
    @ApiMethod(
            name = "update",
            path = "shoppingListRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ShoppingListRecord update(@Named("id") Long id, ShoppingListRecord shoppingListRecord) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(shoppingListRecord).now();
        logger.info("Updated ShoppingListRecord: " + shoppingListRecord);
        return ofy().load().entity(shoppingListRecord).now();
    }

    /**
     * Deletes the specified {@code ShoppingListRecord}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code ShoppingListRecord}
     */
    @ApiMethod(
            name = "remove",
            path = "shoppingListRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(ShoppingListRecord.class).id(id).now();
        logger.info("Deleted ShoppingListRecord with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "shoppingListRecord",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ShoppingListRecord> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<ShoppingListRecord> query = ofy().load().type(ShoppingListRecord.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<ShoppingListRecord> queryIterator = query.iterator();
        List<ShoppingListRecord> shoppingListRecordList = new ArrayList<ShoppingListRecord>(limit);
        while (queryIterator.hasNext()) {
            shoppingListRecordList.add(queryIterator.next());
        }
        return CollectionResponse.<ShoppingListRecord>builder().setItems(shoppingListRecordList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    @ApiMethod(
            name = "listWith"
    )
    public CollectionResponse<ShoppingListRecord> listWith(@Named("email") String email) {
        List<ShoppingListRecord> mealPlanRecordList = ofy().load().type(ShoppingListRecord.class).filter("email", email).list();
        logger.info("list with email " + email + "and found " + mealPlanRecordList.size() + " record.");
        return CollectionResponse.<ShoppingListRecord>builder().setItems(mealPlanRecordList).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(ShoppingListRecord.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find ShoppingListRecord with ID: " + id);
        }
    }
}