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

import edu.dartmouth.cs.pantryplanner.backend.entity.HistoryRecord;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "historyRecordApi",
        version = "v1",
        resource = "historyRecord",
        namespace = @ApiNamespace(
                ownerDomain = "entity.backend.pantryplanner.cs.dartmouth.edu",
                ownerName = "entity.backend.pantryplanner.cs.dartmouth.edu",
                packagePath = ""
        )
)
public class HistoryRecordEndpoint {

    private static final Logger logger = Logger.getLogger(HistoryRecordEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    /**
     * Returns the {@link HistoryRecord} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code HistoryRecord} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "historyRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public HistoryRecord get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting HistoryRecord with ID: " + id);
        HistoryRecord historyRecord = ofy().load().type(HistoryRecord.class).id(id).now();
        if (historyRecord == null) {
            throw new NotFoundException("Could not find HistoryRecord with ID: " + id);
        }
        return historyRecord;
    }

    /**
     * Inserts a new {@code HistoryRecord}.
     */
    @ApiMethod(
            name = "insert",
            path = "historyRecord",
            httpMethod = ApiMethod.HttpMethod.POST)
    public HistoryRecord insert(HistoryRecord historyRecord) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that historyRecord.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(historyRecord).now();
        logger.info("Created HistoryRecord with ID: " + historyRecord.getId());

        return ofy().load().entity(historyRecord).now();
    }

    /**
     * Updates an existing {@code HistoryRecord}.
     *
     * @param id            the ID of the entity to be updated
     * @param historyRecord the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code HistoryRecord}
     */
    @ApiMethod(
            name = "update",
            path = "historyRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public HistoryRecord update(@Named("id") Long id, HistoryRecord historyRecord) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(historyRecord).now();
        logger.info("Updated HistoryRecord: " + historyRecord);
        return ofy().load().entity(historyRecord).now();
    }

    /**
     * Deletes the specified {@code HistoryRecord}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code HistoryRecord}
     */
    @ApiMethod(
            name = "remove",
            path = "historyRecord/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(HistoryRecord.class).id(id).now();
        logger.info("Deleted HistoryRecord with ID: " + id);
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
            path = "historyRecord",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<HistoryRecord> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<HistoryRecord> query = ofy().load().type(HistoryRecord.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<HistoryRecord> queryIterator = query.iterator();
        List<HistoryRecord> historyRecordList = new ArrayList<HistoryRecord>(limit);
        while (queryIterator.hasNext()) {
            historyRecordList.add(queryIterator.next());
        }
        return CollectionResponse.<HistoryRecord>builder().setItems(historyRecordList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    @ApiMethod(
            name = "listWith"
    )
    public CollectionResponse<HistoryRecord> listWith(@Named("email") String email) {
        List<HistoryRecord> historyRecords = ofy().load().type(HistoryRecord.class).filter("email", email).list();
        logger.info("list with email " + email + "and found " + historyRecords.size() + " record.");
        return CollectionResponse.<HistoryRecord>builder().setItems(historyRecords).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(HistoryRecord.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find HistoryRecord with ID: " + id);
        }
    }
}