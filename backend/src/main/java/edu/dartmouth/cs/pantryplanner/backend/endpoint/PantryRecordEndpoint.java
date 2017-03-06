package edu.dartmouth.cs.pantryplanner.backend.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import edu.dartmouth.cs.pantryplanner.backend.entity.PantryRecord;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "pantryRecordApi",
        version = "v1",
        resource = "pantryRecord",
        namespace = @ApiNamespace(
                ownerDomain = "entity.backend.pantryplanner.cs.dartmouth.edu",
                ownerName = "entity.backend.pantryplanner.cs.dartmouth.edu",
                packagePath = ""
        )
)
public class PantryRecordEndpoint {

    private static final Logger logger = Logger.getLogger(PantryRecordEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    /**
     * Returns the {@link PantryRecord} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code PantryRecord} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "pantryRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public PantryRecord get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting PantryRecord with ID: " + id);
        PantryRecord pantryRecord = ofy().load().type(PantryRecord.class).id(id).now();
        if (pantryRecord == null) {
            throw new NotFoundException("Could not find PantryRecord with ID: " + id);
        }
        return pantryRecord;
    }

    /**
     * Inserts a new {@code PantryRecord}.
     */
    @ApiMethod(
            name = "insert",
            path = "pantryRecord",
            httpMethod = ApiMethod.HttpMethod.POST)
    public PantryRecord insert(PantryRecord pantryRecord) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that pantryRecord.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(pantryRecord).now();
        logger.info("Created PantryRecord with ID: " + pantryRecord.getId());

        return ofy().load().entity(pantryRecord).now();
    }

    /**
     * Updates an existing {@code PantryRecord}.
     *
     * @param id           the ID of the entity to be updated
     * @param pantryRecord the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code PantryRecord}
     */
    @ApiMethod(
            name = "update",
            path = "pantryRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public PantryRecord update(@Named("id") Long id, PantryRecord pantryRecord) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(pantryRecord).now();
        logger.info("Updated PantryRecord: " + pantryRecord);
        return ofy().load().entity(pantryRecord).now();
    }

    /**
     * Deletes the specified {@code PantryRecord}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code PantryRecord}
     */
    @ApiMethod(
            name = "remove",
            path = "pantryRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(PantryRecord.class).id(id).now();
        logger.info("Deleted PantryRecord with ID: " + id);
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
            path = "pantryRecord",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<PantryRecord> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<PantryRecord> query = ofy().load().type(PantryRecord.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<PantryRecord> queryIterator = query.iterator();
        List<PantryRecord> pantryRecordList = new ArrayList<PantryRecord>(limit);
        while (queryIterator.hasNext()) {
            pantryRecordList.add(queryIterator.next());
        }
        return CollectionResponse.<PantryRecord>builder().setItems(pantryRecordList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    @ApiMethod(name = "listWith")
    public CollectionResponse<PantryRecord> listWith(@Named("email") String email) {
        List<PantryRecord> pantryRecords = ofy().load().type(PantryRecord.class).filter("email", email).list();
        logger.info("list with email " + email + "and found " + pantryRecords.size() + " record.");
        return CollectionResponse.<PantryRecord>builder().setItems(pantryRecords).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(PantryRecord.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find PantryRecord with ID: " + id);
        }
    }
}