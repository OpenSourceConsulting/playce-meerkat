package com.athena.dolly.controller.web.user;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QUserRole2 is a Querydsl query type for UserRole2
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUserRole2 extends EntityPathBase<UserRole2> {

    private static final long serialVersionUID = -2064743108L;

    public static final QUserRole2 userRole2 = new QUserRole2("userRole2");

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final StringPath name = createString("name");

    public final ListPath<User2, QUser2> users = this.<User2, QUser2>createList("users", User2.class, QUser2.class, PathInits.DIRECT2);

    public QUserRole2(String variable) {
        super(UserRole2.class, forVariable(variable));
    }

    public QUserRole2(Path<? extends UserRole2> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRole2(PathMetadata<?> metadata) {
        super(UserRole2.class, metadata);
    }

}

