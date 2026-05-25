package dev.hope.vulcaobackend.Repositories.Offerings;


import dev.hope.vulcaobackend.Models.Offerings.BookModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    Logger logger = LoggerFactory.getLogger(BookRepository.class);

    private final DynamoDbTable<BookModel> bookTable;

    public BookRepository(DynamoDbEnhancedClient enhancedClient) {
        logger.info("BookRepository init() running, creating Book table");
        this.bookTable = enhancedClient.table("Books", TableSchema.fromBean(BookModel.class));
    }

    @PostConstruct
    public void init() {
        try {
            bookTable.createTable(CreateTableEnhancedRequest.builder()
                    .globalSecondaryIndices(
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("creator-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("company-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-price-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("item-name-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("offering-id-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build(),
                            EnhancedGlobalSecondaryIndex.builder()
                                    .indexName("book-publisher-index")
                                    .projection(p -> p.projectionType(ProjectionType.ALL))
                                    .build()
                            )
                    .build());
            logger.info("Book table created successfully with all indexes");
        } catch (ResourceInUseException e) {
            // table already exists
            logger.debug("TABLE Book ALREADY EXISTS" + e);
        }
    }

    public void save(BookModel newBook) {  bookTable.putItem(r -> r.item(newBook).conditionExpression(Expression.builder().expression("atribute_not_exists(id").build())); }

    public void update(BookModel updatedBook) { bookTable.putItem(updatedBook); }

    public List<BookModel> findByCreatorUsername(String creatorName) {
        return bookTable.index("creator-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(creatorName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }
    public List<BookModel> findByCreatorId(String id) {
        return bookTable.index("creator-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<BookModel> findByName(String productName) {
        return bookTable.index("item-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(productName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public Optional<BookModel> findById(String id) {
        return bookTable.index("offering-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public List<BookModel> findByCompanyName(String companyName) {
        return bookTable.index("company-name-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyName)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<BookModel> findByCompanyId(String companyId) {
        return bookTable.index("company-id-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(companyId)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<BookModel> findByPrice(Integer itemPrice) {
        return bookTable.index("item-price-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(itemPrice)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<BookModel> findByPublisher(String publisher) {
        return bookTable.index("book-publisher-index")
                .query(QueryConditional.keyEqualTo(k -> k.partitionValue(publisher)))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public void deleteBook(BookModel book){
        bookTable.deleteItem(book);
    }

    public List<BookModel> findAll() {
        return bookTable.scan()
                .items()
                .stream()
                .toList();
    }

}
