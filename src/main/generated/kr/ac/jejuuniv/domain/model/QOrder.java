package kr.ac.jejuuniv.domain.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -1300090244;

    public static final QOrder order = new QOrder("order1");

    public final StringPath customer = createString("customer");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final CollectionPath<Item, QItem> items = this.<Item, QItem>createCollection("items", Item.class, QItem.class, PathInits.DIRECT2);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QOrder(Path<? extends Order> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata<?> metadata) {
        super(Order.class, metadata);
    }

}

